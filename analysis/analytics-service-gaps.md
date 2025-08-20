## Analytics Service — Gaps Analysis and Remediation Plan

Compared `server/analytics-service` with `docs/services/analytics-service.md` across controllers, services, models, repositories, configs, schedulers, and search adapters.

### 1) API Coverage vs Docs

- Docs specify event ingestion endpoints: `/api/v1/analytics/events` and `/api/v1/analytics/events/batch`.
  - Action: Verify `AnalyticsController` implements both single and batch ingest with validation and idempotence (Kafka key or dedupe). If missing, add batch endpoint and per-event response semantics.

- User analytics dashboards: `/users/{userId}/dashboard`, `/performance`, `/engagement`.
  - Action: Ensure `MetricsController` exposes these routes with proper aggregation windows and filters; add missing endpoints if any.

- Content analytics: `/content/{contentId}` should include performance, demographics, timeline, and sources.
  - Action: Validate controller returns this shape; add enrichment if missing.

- Rankings: `/analytics/rankings`, `/history`, `/categories`.
  - `RankingsController` exists; confirm query params and response match docs (period, category, prize pool, leaderboard entries, user rank). Add categories and history endpoints if absent.

- Business analytics (admin): `/analytics/business/overview`.
  - Action: If not implemented in `ReportsController` or `AnalyticsController`, add this endpoint and aggregate across services via cached snapshots.

- Reports: POST `/analytics/reports/generate`.
  - Action: Ensure `ReportsController` accepts filter-rich payload and returns report metadata or inline data as per docs; add async generation and retrieval if needed.

### 2) Data Models and Storage (MongoDB and Elasticsearch)

- Docs define `analytics_events`, `user_analytics`, `content_analytics` with indexes and TTL.
  - Models present: `AnalyticsEvent`, `UserMetrics`/`UserAnalytics`, `ContentMetrics`/`ContentAnalytics` plus ES documents and repos.
  - Gaps to address:
    - TTL indexes for raw events and retention windows (config-driven) on Mongo collections.
    - Flexible `eventData` schema validation per `EventType`.
    - Aggregation pipelines to materialize `user_analytics` and `content_analytics` on schedules.
    - Elasticsearch sync/backfill for search endpoints.

### 3) Ingestion and Streaming

- Kafka consumption/production per docs; ensure consumer group, idempotence, and backpressure handling.
  - Add dead-letter handling for malformed events.
  - Enforce schema versioning within `eventData`.

### 4) Rankings System

- Verify period handling (monthly/weekly), categories, faculties filters; prize pool and currency from config; API includes user rank and points-to-next.

### 5) Controller Path Alignment

- Validate `AnalyticsController`, `MetricsController`, `ReportsController`, and `RankingsController` route paths match `/api/v1/analytics/...`.
  - If any differ, add route aliases to maintain docs compatibility.

### 6) Validation and Rate Limits

- Event rate limiting not specified; consider per-user safeguards for abuse using Redis counters.

### 7) Configuration and Schedules

- Ensure YAML exposes batch schedules, ranking schedules, and retention periods per docs; wire into `RetentionScheduler` and `RankingsScheduler`.
  - Confirm Redis caching for dashboards.

### 8) Security and Privacy

- Enforce access controls: users can see only their analytics; admins for business reports.
  - Anonymize or protect IP and sensitive fields.

### 9) Testing and Observability

- Extend tests for event ingestion validation, retention TTL behavior, and dashboard aggregations.
  - Add metrics for ingestion throughput, lag, and aggregation durations.

---

## Remediation Plan

1) Align controller paths and add missing endpoints: events batch, user performance/engagement, rankings history/categories, business overview, reports generation.
2) Add Mongo TTL indexes and retention jobs driven by config.
3) Implement event validation per `EventType` with schema versioning; add DLQ for malformed events.
4) Ensure aggregation pipelines materialize `user_analytics` and `content_analytics` and are scheduled.
5) Add Redis caching for dashboards with proper invalidation.
6) Harden Kafka consumers with idempotence and error handling.
7) Scope access to own data; admin-only for business reports.
8) Expand integration and unit tests for ingestion, aggregation, and controllers.








