## Raved Services - Local Development and Integration Test Setup

This guide helps you start all external dependencies via Docker and run the complete test suite (including integration tests) against the containerized infrastructure.

### 1) Prerequisites
- Docker Desktop 4.0+
- Docker Compose v2
- Java 21 (local builds)
- Maven 3.9+

### 2) Start infrastructure with Docker Compose

We provide docker-compose.yml with the following services:
- Postgres (5432)
- Redis (6379)
- Kafka + Zookeeper (9092)
- MongoDB (27017)
- Elasticsearch (9200)
- Optional: RabbitMQ mgmt (15672/5672), Loki/Promtail/Tempo/OTel/Grafana, Eureka, Config Server, API Gateway, Services

Commands:
- Start infra only
  docker compose up -d postgres redis zookeeper kafka mongo elasticsearch

- Start full stack core infra
  docker compose up -d postgres redis zookeeper kafka mongo elasticsearch eureka-server config-server api-gateway

- Stop all
  docker compose down -v

### 3) Environment variables and connectivity

Common env defaults used by services during local/it profiles:
- Database (Postgres)
  SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/raved_db (or per-service DB)
  DATABASE_USERNAME=raved_user
  DATABASE_PASSWORD=raved_password

- Kafka
  KAFKA_BOOTSTRAP_SERVERS=localhost:9092

- Redis
  SPRING_REDIS_HOST=localhost
  SPRING_REDIS_PORT=6379

- MongoDB
  SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/

- Elasticsearch
  ELASTICSEARCH_HOST=http://localhost:9200

- Eureka (only for runtime; tests disable eureka client in application-test.yml)
  EUREKA_SERVER_URL=http://localhost:8761/eureka/

### 4) Verify services are healthy
- Postgres: psql -h localhost -U raved_user -d raved_db -c "\l"
- Redis: redis-cli ping => PONG
- Kafka: docker exec -it <kafka-container> kafka-topics --bootstrap-server localhost:9092 --list
- Mongo: mongosh --eval 'db.runCommand({ ping: 1 })'
- Elasticsearch: curl http://localhost:9200/_cluster/health

### 5) Running tests

Unit tests only (fast):
- mvn -DskipITs -Dspring.profiles.active=test test

Integration verify (ITs):
- Ensure infra is up (section 2)
- Run: mvn -Dspring.profiles.active=it verify
  Notes:
  - If ITs are defined with Failsafe (*IT.java pattern), verify will execute them.
  - You can also target specific modules: mvn -pl user-service -am -Pit -Dspring.profiles.active=it verify

Coverage:
- After adding the 'coverage' profile (done), run:
  mvn -Pcoverage -Dspring.profiles.active=test verify
- Aggregated report will be placed under server/target/site/jacoco-aggregate

### 6) Startup sequence and dependencies
- Start Zookeeper -> Kafka
- Start Postgres/Redis/Mongo/Elasticsearch (order independent)
- Start Eureka -> Config Server -> API Gateway -> individual services
- Services depend on infra; docker-compose declares healthchecks and depends_on to order startup

### 7) Troubleshooting
- Kafka cannot connect: ensure zookeeper is healthy; verify KAFKA_ADVERTISED_LISTENERS include PLAINTEXT://localhost:9092
- Port already in use: change host port mapping in docker-compose.yml or stop the local process
- Elasticsearch fails to start: give it more memory or verify vm.max_map_count on Linux
- Tests still hit Eureka: ensure application-test.yml contains eureka.client.enabled=false for the module
- Redis or Mongo timeouts: confirm ports 6379/27017 are open and services started
- Windows path/line-ending issues: use LF and run Git Bash or WSL for scripts

### 8) Integration test tips
- Prefer Testcontainers for portable ITs; fall back to docker-compose if needed
- Disable auto-start listeners (Kafka consumers) in tests unless testing consumption
- Use Spring test slices (@DataJpaTest, @DataMongoTest, @WebMvcTest) to keep tests fast
- Mock external HTTP calls using MockRestServiceServer or WireMock in unit tests


