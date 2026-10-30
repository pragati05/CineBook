# CineBook architecture

Diagrams are Mermaid and render on GitHub. The same diagrams appear in the shareable overview page.

## System architecture

Every request enters through the gateway. Services never read each other's databases. They talk through Kafka events or the gateway-facing APIs.

```mermaid
flowchart LR
  subgraph client["Clients"]
    UI["React SPA<br/>customer, staff scanner, admin"]
  end
  subgraph edge["Edge"]
    GW["api-gateway<br/>JWT check, rate limits, routing"]
  end
  subgraph svc["Microservices (Spring Boot 4, Java 21)"]
    USR["user-service"]
    CAT["catalog-service<br/>cities, theatres, screens, shows"]
    BKG["booking-service<br/>seat holds, bookings"]
    TKT["ticket-service<br/>QR, PDF, check-in"]
    NTF["notification-service<br/>email"]
    INS["insights-service<br/>no-show risk, NL search"]
  end
  subgraph data["State and messaging"]
    MDB[("MongoDB<br/>one database per service")]
    RDS[("Redis<br/>read cache")]
    KFK{{"Apache Kafka"}}
  end
  subgraph ext["External"]
    SES["Email (SES locally Mailhog)"]
    S3[("Ticket PDFs (S3)")]
    LLM["LLM (Claude API or Bedrock)"]
  end
  UI -->|HTTPS| GW
  GW --> USR
  GW --> CAT
  GW --> BKG
  GW --> TKT
  GW --> INS
  USR --> MDB
  CAT --> MDB
  BKG --> MDB
  TKT --> MDB
  INS --> MDB
  CAT -.->|read cache| RDS
  BKG -.->|read cache| RDS
  CAT -->|show.updated| KFK
  BKG -->|booking.created, cancelled, expired| KFK
  KFK -->|booking.*| TKT
  TKT -->|ticket.generated, checked-in| KFK
  KFK -->|ticket.generated, booking.expired| NTF
  KFK -->|booking.*, ticket.checked-in| INS
  TKT --> S3
  NTF --> SES
  INS --> LLM
```

## Booking flow

Seats are held first with a short TTL, then converted to a booking in one transaction that also writes the outbox row. Ticket and email work happens asynchronously.

```mermaid
sequenceDiagram
  autonumber
  actor U as Customer
  participant UI as React SPA
  participant GW as api-gateway
  participant BK as booking-service
  participant DB as MongoDB
  participant K as Kafka
  participant TK as ticket-service
  participant NT as notification-service
  U->>UI: Pick seats for a show
  UI->>GW: POST /holds with showId and seats
  GW->>BK: forward with validated JWT
  BK->>DB: atomic update, seats AVAILABLE to HELD, expiresAt = now + 5 min
  BK-->>UI: 201 holdId and expiresAt
  U->>UI: Confirm booking, pay at venue
  UI->>GW: POST /bookings with holdId
  BK->>DB: one transaction, seats HELD to BOOKED, booking row, outbox row
  BK-->>UI: 201 bookingRef
  BK--)K: booking.created, key = showId
  K--)TK: booking.created
  TK->>TK: sign token, render QR and PDF, store in S3
  TK--)K: ticket.generated
  K--)NT: ticket.generated
  NT->>U: Email with ticket PDF and QR
```

## Check-in at the theatre

The conditional update is what makes a QR single-use: two scanners racing on one ticket, only one wins.

```mermaid
sequenceDiagram
  autonumber
  actor S as Gate staff
  participant UI as Scanner PWA
  participant TK as ticket-service
  participant DB as MongoDB
  participant K as Kafka
  participant BK as booking-service
  S->>UI: Scan customer QR
  UI->>TK: POST /checkin with signed token
  TK->>TK: verify signature, show, and time window
  TK->>DB: update ticket VALID to USED only if still VALID
  alt first scan
    TK-->>UI: 200 valid, seats and name
    TK--)K: ticket.checked-in
    K--)BK: ticket.checked-in
    BK->>BK: booking CONFIRMED to ATTENDED
  else already used or forged
    TK-->>UI: 409 or 401 with reason
  end
```

## Reminders and seat release

Pay-at-venue has no payment barrier, so unconfirmed bookings are released shortly before the show. The outcome feeds the no-show model.

```mermaid
sequenceDiagram
  autonumber
  participant SCH as booking-service scheduler
  participant DB as MongoDB
  participant K as Kafka
  participant NT as notification-service
  participant IN as insights-service
  actor U as Customer
  NT->>U: Reminder 3 h before show, confirm attendance link
  SCH->>DB: find CONFIRMED bookings, unconfirmed, show starts in 30 min
  SCH->>DB: release seats, booking to RELEASED
  SCH--)K: booking.expired
  K--)NT: booking.expired
  NT->>U: Email, your seats were released
  K--)IN: booking.expired
  IN->>IN: log outcome for the no-show model
```

## Booking lifecycle

Terminal states are EXPIRED, CANCELLED, RELEASED, ATTENDED and NO_SHOW.

```mermaid
stateDiagram-v2
  [*] --> HELD: select seats
  HELD --> CONFIRMED: confirm within TTL
  HELD --> EXPIRED: TTL elapsed
  CONFIRMED --> CANCELLED: customer cancels before cutoff
  CONFIRMED --> RELEASED: no attendance confirmation by T-30 min
  CONFIRMED --> ATTENDED: QR checked in at gate
  CONFIRMED --> NO_SHOW: show started, never scanned
  EXPIRED --> [*]
  CANCELLED --> [*]
  RELEASED --> [*]
  ATTENDED --> [*]
  NO_SHOW --> [*]
```

## AWS production deployment

Local development uses the Mongo and Kafka containers already running on the developer machine. Production swaps in the managed services shown here, selected by Spring profile.

```mermaid
flowchart TB
  U["Users"] --> R53["Route 53"]
  R53 --> CF["CloudFront"]
  CF -->|static assets| S3W[("S3 web bucket<br/>React build")]
  CF -->|/api| ALB["Application Load Balancer<br/>TLS via ACM"]
  subgraph vpc["VPC, private subnets"]
    ALB --> GW["api-gateway (ECS Fargate)"]
    GW --> SVC["user, catalog, booking,<br/>ticket, notification, insights<br/>(ECS Fargate services)"]
    SVC --> DOC[("MongoDB Atlas on AWS<br/>or DocumentDB")]
    SVC --> MSK{{"Amazon MSK"}}
    SVC --> EC[("ElastiCache Redis")]
  end
  SVC --> S3T[("S3 ticket bucket")]
  SVC --> SES["Amazon SES"]
  SVC --> SM["Secrets Manager"]
  SVC --> BED["LLM: Claude API or Bedrock"]
  SVC -.-> CW["CloudWatch logs, metrics, alarms"]
```


## Design decisions

| Decision | Why |
|---|---|
| Database per service, one Mongo cluster | Services own their data. No cross-service reads, so each can be scaled or moved independently. |
| Seat state is one document per show and seat | A conditional update (`status == AVAILABLE`) is atomic; multi-seat holds use a transaction, which needs a replica set locally and in prod. |
| Hold, then book | A hold has a short TTL (default 5 minutes) so an abandoned selection frees itself. Booking converts the hold in one transaction. |
| Transactional outbox | The booking row and the `booking.created` event commit together, so a crash cannot lose the event. |
| Kafka partition key = `showId` | All events for one show stay ordered, and parallelism comes from many shows. |
| Idempotent consumers | Every event carries an ID. Replays and retries never produce a second ticket or email. |
| Signed QR token | The gate can reject forged tickets without a database lookup, then does one conditional update to make it single-use. |
| Pay at venue means no-show risk | Unconfirmed bookings are released before the show (per-theatre policy), and the outcome trains the no-show estimate. |
| LLM only behind `insights-service` | One place to manage prompts, model IDs, timeouts, cost and fallbacks. Core booking never depends on an LLM call. |

## Kafka topics

| Topic | Producer | Consumers | Key |
|---|---|---|---|
| `booking.created` | booking-service | ticket-service, insights-service | showId |
| `booking.cancelled` | booking-service | ticket-service, notification-service, insights-service | showId |
| `booking.expired` | booking-service | notification-service, insights-service | showId |
| `ticket.generated` | ticket-service | notification-service | bookingRef |
| `ticket.checked-in` | ticket-service | booking-service, insights-service | showId |
| `show.updated` | catalog-service | booking-service, notification-service | showId |
| `<topic>.dlq` | any consumer | operators | original key |

## Environments

| | Local | Production |
|---|---|---|
| MongoDB | Container already running on the developer machine (replica set) | MongoDB Atlas on AWS or DocumentDB (see spike issue) |
| Kafka | Container already running on the developer machine | Amazon MSK |
| Email | Mailhog (optional) | Amazon SES |
| Secrets | Environment variables | AWS Secrets Manager |
| Spring profile | `local` (default) | `prod` |
