# backend

Java 21 / Spring Boot 4 microservices, one module per service plus a shared library.

Modules: `api-gateway`, `user-service`, `catalog-service`, `booking-service`, `ticket-service`, `notification-service`, `insights-service`, `common`. See [docs/architecture.md](../docs/architecture.md).

| Module | Port |
|---|---|
| `api-gateway` | 8080 |
| `user-service` | 8081 |
| `catalog-service` | 8082 |
| `booking-service` | 8083 |
| `ticket-service` | 8084 |
| `notification-service` | 8085 |
| `insights-service` | 8086 |

`common` holds the Kafka event DTOs (`com.cinebook.common.events`) and the shared error format (`com.cinebook.common.error`).

## Build and test

Requires JDK 21 and Docker (tests start MongoDB, Kafka and Redis via Testcontainers).

```bash
cd backend
./mvnw verify                          # build + test all modules, checks formatting
./mvnw spotless:apply                  # auto-format (google-java-format)
./mvnw -pl booking-service -am spring-boot:run   # run one service
```

## Local infrastructure

`docker compose up -d` (from `backend/`) starts MongoDB as a single-node replica set `rs0`, Kafka (KRaft, no ZooKeeper), Redis and Mailhog (web UI at http://localhost:8025) on the default ports below. Stop any existing containers using those ports first. `docker compose down` stops them; add `-v` to also delete the data.

## Configuration

Each service has `application.yml` (name, port) and `application-local.yml`. `local` is the default profile and needs no AWS credentials. It targets the MongoDB, Kafka and Redis containers already running on your machine. A `prod` profile (AWS) will be added after the POC.

Select a profile with `SPRING_PROFILES_ACTIVE` (default `local`). Every variable below is optional locally; the default is used when it is unset.

| Variable | Used by | Default |
|---|---|---|
| `SERVER_PORT` | all | port from the table above |
| `MONGODB_URI` | all except `api-gateway` | `mongodb://localhost:27017/?replicaSet=rs0` |
| `KAFKA_BOOTSTRAP_SERVERS` | catalog, booking, ticket, notification | `localhost:9092` |
| `REDIS_HOST` | catalog, booking | `localhost` |
| `REDIS_PORT` | catalog, booking | `6379` |
| `MAIL_HOST` | notification | `localhost` (Mailhog) |
| `MAIL_PORT` | notification | `1025` |

**Mongo must run as a replica set** (`rs0`), even locally, because seat booking uses multi-document transactions. A standalone `mongod` will not work.

To give each service its own database, put the name in the URI path, e.g. `mongodb://localhost:27017/booking?replicaSet=rs0`.
