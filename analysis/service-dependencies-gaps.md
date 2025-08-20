## Service Dependencies — Gaps Analysis and Remediation Plan

Reviewed `docs/services/service-dependencies.md` against actual code usage across `server/*`.

### 1) Inter-service Calls (HTTP)

- Found Feign client:
  - `user-service` -> `notification-service` via `NotificationClient`.
- No other Feign/WebClient/RestTemplate usages in production code (tests use RestTemplate/TestRestTemplate only).
- Docs currently state minimal findings; likely incomplete relative to individual service docs (e.g., content needs user profiles, ecommerce needs notifications, etc.).
  - Remediation: Either update docs to reflect current minimal coupling or implement intended clients:
    - content-service -> user-service (author profiles) and social-service (engagement)
    - ecommerce-service -> user-service (seller profile) and notification-service (order notifications)
    - subscription-service -> notification-service (receipts), payment providers (webhooks)
    - analytics-service -> primarily event-driven, no sync deps

### 2) Messaging (Kafka)

- Producers present:
  - content-service (`ContentEventsProducer`), user-service (`UserEventPublisher`), notification-service (`NotificationProducer`), social-service (`SocialEventPublisher`), ecommerce-service (`EcommerceEventPublisher`).
- Tests indicate analytics Kafka usage; ensure actual listeners/producers exist for ingestion pipeline.
- Remediation: Add a concrete Kafka topics matrix (producer/consumer, topic names, key/value types) to `service-dependencies.md` and ensure infra provisioning matches.

### 3) Messaging (RabbitMQ)

- realtime-service defines RabbitMQ config and listeners (`PresenceListener`, `NotificationListener`, `ChatMessageListener`). The dependency doc should list exchanges/queues/bindings.
  - Remediation: Document exchange/queue names from `RabbitMQConfig` and align infra.

### 4) Data Stores per Service

- The dependency doc broadly matches code. Improve with specifics:
  - Elasticsearch: index names and mapping expectations for content/analytics.
  - Redis: what is cached (feeds, presence, dashboards) and key patterns.
  - Kafka: topic names per service.

### 5) Discovery & Gateway

- All services register with Eureka per app configs. realtime-service WebSocket endpoint is `/api/v1/realtime/connect` (code). Clarify this path in the doc.

### 6) Concrete Gaps to Address in Doc

- Enumerate actual inter-service calls (currently only user->notification). Add planned dependencies or TODOs per service.
- Add Kafka topics matrix and consumer groups per service.
- Add RabbitMQ topology for realtime-service.
- Add Elasticsearch indices used by content/analytics and mapping expectations.
- Add configuration environment variables for external deps to standardize across services.

---

## Remediation Plan

1) Update `docs/services/service-dependencies.md` to include:
   - Verified inter-service clients and planned ones (with owners/timelines)
   - Kafka topics and consumer groups per service
   - RabbitMQ topology (exchanges/queues/bindings)
   - Elasticsearch indices and templates
   - Precise realtime WebSocket endpoint path
2) Add TODO markers per service for missing intended deps and link to corresponding gap docs in `analysis/`.
3) Optionally add a Mermaid dependency diagram to the doc.








