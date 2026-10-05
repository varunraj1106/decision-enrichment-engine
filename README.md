# Decision Enrichment Engine

A real-time transaction decisioning and enrichment platform built with Spring Boot. Evaluates multiple risk signals concurrently using Virtual Threads and applies a weighted scoring model to approve, flag, or reject transactions.

## Tech Stack

- Java 21 with Virtual Threads
- Spring Boot 3.3.5
- Spring Data JPA + H2 (in-memory)
- Spring Security
- Spring Actuator + Prometheus Metrics
- Docker
- GitHub Actions CI/CD

## Architecture
Transaction Request → Risk Signal Evaluator (5 concurrent signals)
↓
Decision Engine (weighted scoring)
↓
APPROVED / FLAGGED / REJECTED


### Risk Signals Evaluated Concurrently

| Signal | Trigger | Weight |
|--------|---------|--------|
| Amount Anomaly | > £10,000 | 20-35 |
| Geo Anomaly | Cross-border + high-risk country | 30 |
| Velocity | High transaction frequency | 25 |
| Merchant Risk | Gambling, Crypto, etc. | 20 |
| Channel Risk | Card-not-present (online) | 10 |

### Decision Thresholds

- **APPROVED**: Score < 25
- **FLAGGED**: Score 25-59
- **REJECTED**: Score ≥ 60

## Running Locally

```bash
mvn clean install
mvn spring-boot:run
```

## API Endpoints

### Enrich & Decide
```bash
curl -X POST http://localhost:8080/api/v1/enrichment/decide \
  -H "Content-Type: application/json" \
  -d '{"merchantId":"M1","customerId":"C1","amount":75000,"currency":"USD","merchantCategory":"CRYPTO","sourceCountry":"GB","destinationCountry":"KP","channel":"ONLINE"}'
```

### Get High-Risk Transactions
```bash
curl http://localhost:8080/api/v1/transactions/high-risk?minScore=50
```

### Health & Metrics
```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/prometheus
```

## Running Tests

```bash
mvn test
```

## Docker

```bash
mvn clean package -DskipTests
docker build -t decision-enrichment-engine .
docker run -p 8080:8080 decision-enrichment-engine
```