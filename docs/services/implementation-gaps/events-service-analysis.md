# events-service — Implementation Analysis

Purpose
- Event sourcing/audit/domain events; PostgreSQL + JPA/Flyway.

Current implementation (observed)
- application.yml sets SPRING_DATASOURCE_URL; minimal config visible

Gaps / TODOs
- Verify existence of controllers/services/repositories
- Ensure Flyway migrations and entity mappings
- Define event schema and retention policies

Recommended actions
- Inventory endpoints and DB schema; add tests
- Align Kafka topics (if producing/consuming)

