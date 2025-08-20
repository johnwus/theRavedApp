# RAvED App - Server Microservices

## Overview

This directory contains all the Spring Boot microservices for the RAvED App platform, including infrastructure services and business logic services.

## Services Architecture

### Infrastructure Services
- **eureka-server** (Port 8761) - Service Discovery
- **config-server** (Port 8888) - Configuration Management
- **api-gateway** (Port 8080) - API Gateway & Routing

### Business Services

#### PostgreSQL-Based Services
- **user-service** (Port 8081) - Authentication & User Management
- **ecommerce-service** (Port 8085) - Marketplace & Transactions
- **realtime-service** (Port 8084) - WebSocket & Live Communication
- **events-service** (Port 8088) - Campus Events Management
- **subscription-service** (Port 8089) - Premium Features & Billing

#### MongoDB-Based Services
- **content-service** (Port 8082) - Posts, Media, Feeds
- **social-service** (Port 8083) - Likes, Comments, Follows
- **notification-service** (Port 8086) - Push & Email Notifications
- **analytics-service** (Port 8087) - Metrics & Business Intelligence

## Technology Stack

- **Java 21** - Programming Language
- **Spring Boot 3.1.5** - Application Framework
- **Spring Cloud 2022.0.4** - Microservices Framework
- **PostgreSQL 15** - Primary Database
- **MongoDB 6** - Document Database
- **Redis 7** - Caching Layer
- **Kafka** - Event Streaming
- **Maven** - Build Tool

## Development Setup

### Prerequisites
- Java 21
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL 15
- MongoDB 6

### Quick Start
```bash
# Start infrastructure services
docker-compose up -d postgres redis mongo kafka

# Start Eureka Server
cd eureka-server && mvn spring-boot:run

# Start Config Server
cd config-server && mvn spring-boot:run

# Start API Gateway
cd api-gateway && mvn spring-boot:run

# Start business services
cd user-service && mvn spring-boot:run
```

## Build & Test

### Build All Services
```bash
mvn clean compile -f pom.xml
```

### Run Tests
```bash
mvn test -f pom.xml
```

### Build Docker Images
```bash
mvn clean package -f pom.xml
docker-compose build
```

## Monitoring & Health Checks

All services expose actuator endpoints:
- Health: `http://localhost:{port}/actuator/health`
- Metrics: `http://localhost:{port}/actuator/prometheus`
- Info: `http://localhost:{port}/actuator/info`

## Documentation

- Service specifications: `../docs/services/`
- API documentation: Available via Swagger UI on each service
- Database models: `../docs/PROTOTYPE_DATABASE_MODEL.md`

---

## CI/CD Test Information

**Test Timestamp**: 2025-08-20 (CI/CD Pipeline Validation)
**Purpose**: This README was created to test and validate the CI/CD integration for the RAvED App project.
**Branch**: test/ci-cd-validation-20250820
**Expected CI/CD Behaviors**:
- ✅ GitHub Actions workflows should trigger on push/PR
- ✅ Server CI should run tests for all microservices
- ✅ Client CI should run frontend tests and linting
- ✅ Security scans should execute
- ✅ Code quality checks should run
- ✅ Docker build processes should complete
- ✅ Infrastructure validation should pass

This is a non-breaking test change to validate CI/CD pipeline functionality.
