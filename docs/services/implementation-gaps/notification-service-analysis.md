# notification-service — Implementation Analysis

Purpose
- Email/SMS/push notifications; Kafka consumption/production; SMTP and Twilio integration; Mongo primary (staging shows Postgres).

Current implementation (observed)
- Repositories: NotificationTemplateRepository (MongoRepository with rich queries)
- application.yml: Kafka topics configured; SMTP/Twilio placeholders; Redis configured; staging profile references Postgres + Flyway

Gaps / TODOs
- Confirm controllers/services for sending notifications; Kafka consumers/producers (NotificationConsumer/Producer per structure) exist and are wired
- Resolve data store inconsistency across profiles (Mongo vs Postgres); pick one model and align infra
- Add tests for template rendering and delivery flows; mock SMTP/Twilio

Recommended actions
- Implement/verify NotificationConsumer and Producer, configure Kafka template and listener containers
- Decide persistence (Mongo or Postgres) and standardize configs, migrations if needed
- Add rate limiting and retries with backoff for providers

