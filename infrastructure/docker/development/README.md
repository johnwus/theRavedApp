# TheRavedApp Development Infrastructure

This directory contains the Docker Compose configuration for running all the infrastructure services needed for TheRavedApp polyglot persistence architecture.

## Services Overview

### Core Infrastructure
- **PostgreSQL 15** - Primary relational database for transactional services
- **MongoDB 6** - Document database for content and analytics services
- **Redis 7** - Caching and session storage
- **Elasticsearch 8.11** - Search and analytics engine
- **Kafka** - Message streaming platform
- **RabbitMQ 3** - Message broker with management UI

### Monitoring & Observability
- **Prometheus** - Metrics collection and storage
- **Grafana** - Metrics visualization and dashboards
- **Jaeger** - Distributed tracing

## Polyglot Persistence Strategy

### PostgreSQL Services (ACID Compliance Required)
- **User Service** - Users, authentication, profiles, verification
- **Ecommerce Service** - Products, orders, payments, inventory
- **Events Service** - Event details, registrations, attendance
- **Subscription Service** - Plans, billing, usage tracking

### MongoDB Services (Flexible Schema, High Volume)
- **Content Service** - Posts, media, comments, tags
- **Analytics Service** - User behavior, metrics, events
- **Social Service** - Social interactions, feeds
- **Notification Service** - Templates, delivery logs

### Hybrid Services (Strategic Mix)
- **Realtime Service** - PostgreSQL (chat rooms) + MongoDB (messages)
- **API Gateway** - PostgreSQL (routing) + Redis (rate limiting)

## Quick Start

### 1. Start Infrastructure Services
```bash
# Start all infrastructure services
start-infrastructure.bat

# Or manually with Docker Compose
docker-compose up -d
```

### 2. Start Microservices
```bash
# Start all microservices in the correct order
start-microservices.bat
```

## Service Ports

| Service | Port | URL | Description |
|---------|------|-----|-------------|
| PostgreSQL | 5432 | localhost:5432 | Primary Database |
| MongoDB | 27017 | localhost:27017 | Document Database |
| Redis | 6379 | localhost:6379 | Cache |
| Elasticsearch | 9200 | localhost:9200 | Search & Analytics |
| Kafka | 9092 | localhost:9092 | Streaming |
| RabbitMQ | 5672 | localhost:5672 | Message Broker |
| RabbitMQ Management | 15672 | localhost:15672 | Management UI |
| Prometheus | 9090 | localhost:9090 | Metrics |
| Grafana | 3000 | localhost:3000 | Dashboards (admin/admin) |
| Jaeger | 16686 | localhost:16686 | Tracing UI |

## Microservices Architecture

### Service Discovery & Configuration
1. **Eureka Server** (8761) - Service registry
2. **Config Server** (8888) - Centralized configuration

### API Gateway
3. **API Gateway** (8080) - Route requests to services

### Business Services
4. **User Service** (8081) - User management (PostgreSQL)
5. **Content Service** (8082) - Content management (MongoDB)
6. **Social Service** (8083) - Social features (MongoDB)
7. **Realtime Service** (8084) - WebSocket/real-time (Hybrid)
8. **Ecommerce Service** (8085) - E-commerce features (PostgreSQL)
9. **Notification Service** (8086) - Notifications (MongoDB)
10. **Analytics Service** (8087) - Analytics & reporting (MongoDB)
11. **Events Service** (8088) - Event management (PostgreSQL)
12. **Subscription Service** (8089) - Subscription management (PostgreSQL)

## Data Consistency Strategy

### Event-Driven Architecture
- **Kafka** for event streaming between services
- **Event sourcing** for data synchronization
- **Transactional outbox pattern** for reliable event publishing
- **Saga pattern** for distributed transactions

### Consistency Levels
- **Strong consistency** for critical business data (PostgreSQL)
- **Eventual consistency** for content and analytics (MongoDB)
- **Caching consistency** via Redis with TTL and invalidation

## Dependencies Added

### Analytics Service
- MongoDB for analytics data
- Elasticsearch for search and analytics
- Prometheus metrics for monitoring

### Content Service
- MongoDB for content storage
- Elasticsearch for search capabilities
- Prometheus metrics for monitoring

### Social Service
- MongoDB for social interactions
- Prometheus metrics for monitoring

### All Services
- Micrometer for Prometheus metrics
- Event streaming capabilities via Kafka
- Database-specific starters (JPA for PostgreSQL, MongoDB for document stores)

## Monitoring

### Prometheus
- Scrapes metrics from all services via `/actuator/prometheus`
- Monitors infrastructure services (PostgreSQL, MongoDB, Redis, Elasticsearch)
- Stores time-series data for analysis

### Grafana
- Pre-configured with Prometheus datasource
- Ready for custom dashboards
- Database performance monitoring

### Jaeger
- Distributed tracing for microservices
- Helps debug request flows across services
- Performance bottleneck identification

## Database Connections

### PostgreSQL
- Database: `raved_db`
- User: `raved_user`
- Password: `raved_password`

### MongoDB
- Database: `raved_content`
- User: `raved_admin`
- Password: `raved_password`

### Redis
- No authentication (development only)
- Configuration: `./redis/redis.conf`

### Elasticsearch
- No authentication (development only)
- HTTP API: `http://localhost:9200`

## Troubleshooting

### Service Won't Start
1. Check if required infrastructure services are running
2. Verify ports are not already in use
3. Check service logs: `docker-compose logs <service-name>`

### Connection Issues
1. Ensure services are fully started (wait 15-20 seconds)
2. Check network connectivity between containers
3. Verify service discovery registration in Eureka
4. Check database connection strings and credentials

### Performance Issues
1. Monitor resource usage: `docker stats`
2. Check Prometheus metrics
3. Review Grafana dashboards
4. Monitor database performance metrics

## Development Workflow

1. **Start Infrastructure**: Run `start-infrastructure.bat`
2. **Wait for Services**: Ensure all containers are healthy (15-20 seconds)
3. **Start Microservices**: Run `start-microservices.bat`
4. **Monitor**: Check Eureka (8761) and Grafana (3000)
5. **Develop**: Make changes and restart individual services as needed

## Next Steps

- [x] Infrastructure setup with polyglot persistence
- [x] Service dependencies updated
- [x] Monitoring configuration
- [ ] Create custom Grafana dashboards
- [ ] Implement event-driven data synchronization
- [ ] Add health checks and readiness probes
- [ ] Implement Saga pattern for distributed transactions
- [ ] Set up CI/CD pipeline
