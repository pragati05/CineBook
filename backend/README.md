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
