# Service Dependency Map

This document maps inter-service communication and external dependencies discovered in code.

Legend: -> indicates a call/dependency on another service.

## Inter-service calls
- api-gateway -> routes to: user-service, content-service, social-service, realtime-service, ecommerce-service, notification-service, analytics-service
- user-service -> notification-service (via Feign client NotificationClient)
- Others: Not explicitly found in code yet (no Feign clients/WebClient/RestTemplate usages discovered beyond tests)

Notes:
- Discovery: All business services register with Eureka (defaults now point to http://eureka-server:8761/eureka/)
- Protocols: Gateway uses HTTP; realtime-service exposes WebSocket under /ws/**

## External dependencies per service (from application.yml and code)
- user-service: PostgreSQL (JPA/Hibernate/Flyway)
- content-service: MongoDB, Redis, Elasticsearch, Kafka
- social-service: MongoDB, Redis
- realtime-service: PostgreSQL (Flyway), RabbitMQ
- ecommerce-service: PostgreSQL (Flyway)
- notification-service: MongoDB (primary), Redis, Kafka, Mail (SMTP), Twilio; staging profile references PostgreSQL (Flyway)
- analytics-service: MongoDB, Redis, Elasticsearch, Kafka
- events-service: PostgreSQL (Flyway)
- subscription-service: PostgreSQL (Flyway)
- api-gateway: JWT resource server, Eureka; optional Redis if enabling rate limiting
- config-server: Git/native config backend, optional Eureka registration
- eureka-server: Discovery service (no external deps)

## Gaps to confirm
- Additional inter-service calls (Feign/WebClient/RestTemplate) beyond user -> notification
- Kafka topics, consumer groups, and serializers in services; match with infra provisioning
- Elasticsearch index templates/mappings for content/analytics
- RabbitMQ exchanges/queues/bindings declared by realtime-service

