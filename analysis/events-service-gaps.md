## Events Service — Gaps Analysis and Remediation Plan

Compared `server/events-service` with `docs/services/events-service.md` across controllers, services, models, repositories, mappers, configs, and migrations.

### 1) API Coverage vs Docs

- Core CRUD endpoints exist: create/get/update/delete under `/api/v1/events`.
- Doc endpoints missing or partial:
  - Filtering: docs include rich filters (faculty, category, date range, location, tags, status, sort by distance/popularity). Current controller supports category, location (name contains), date range, upcoming, featured, search; lacks faculty, tags, status, distance sorting.
  - Attendance: docs specify RSVP POST `/events/{eventId}/attendance` and GET attendees, plus `/users/{userId}/events`. No attendance controller/service found.
  - Locations: docs specify `/locations/campus` and `/locations/nearby`. Not present.
  - Categories: docs specify `/events/categories`. Not present.
  - Response shapes: docs include organizer, ticketInfo, attendance metrics, distance/timeUntilStart. Current responses likely lack these enrichments.
  - Remediation: Implement Attendance endpoints and persistence, location suggestion/nearby endpoints (with geospatial queries), categories endpoint, and extend filtering/sorting.

### 2) Models vs Docs

- `Event` entity vs docs `events` table:
  - Current entity uses numeric `id` (BIGSERIAL), JSONB `location_geo`, simple `category`, no arrays for images/tags, no ticket fields (`is_free`, `ticket_price`, `ticket_url`), no `status`, `is_public`, `requires_approval`, no `slug`, no `timezone`, no `published_at`.
  - Migration V1 matches current `Event` fields; does not include many doc fields (images, tags, status, ticketing, public/approval, timezone, slug, faculty, coordinates index, etc.).
  - Remediation: Add a migration to extend schema per docs (columns for faculty, timezone, is_public, requires_approval, status, images[], tags[], ticket fields, slug unique, published_at, capacity constraints), and update `Event` entity, DTOs, mapper, repository queries accordingly. Consider PostGIS or POINT for coordinates and GIST index.

- `EventAttendee` entity vs docs `event_attendance`:
  - Current table named `event_attendees` with composite PK; status enum values differ in case and set (`INTERESTED, GOING, CHECKED_IN, CANCELLED`) vs docs (`going, interested, not_going`), and docs include `checked_in` fields; current model lacks `checked_in`, `checked_in_at`, guest_count, special_requirements.
  - Remediation: Align table name to `event_attendance` or update docs, add missing columns and indexes (by status, user), extend model to include `checkedIn`, `checkedInAt`, `guestCount`, `specialRequirements`, and normalize status values.

### 3) DTOs/Mappers

- `CreateEventRequest`/`EventResponse` need to support docs fields (location object with name/address/coordinates/capacity; ticketInfo; images array; tags; isPublic; requiresApproval; faculty). Mapper should translate between nested structures and flat columns/JSONB.
  - Remediation: Extend DTOs and `EventMapper`; convert `location_geo` JSONB to/from structured type; include attendance summary in response.

### 4) Repositories/Queries

- Need full-text search (title/description), filters for tags, faculty, status; distance sorting and geospatial queries for nearby/campus.
  - Remediation: Add columns and indexes; add custom repository methods with native queries for distance using PostGIS or compute via Haversine on POINT; add filters for tags (GIN) and status.

### 5) Attendance Management

- Missing service/controller to RSVP, list attendees with filters, and fetch a user's events.
  - Remediation: Implement `AttendanceController`, `AttendanceService`, and repository for `EventAttendee` with methods: upsert RSVP, counts per status, attendees listing with pagination, user-event queries, and check-in handling.

### 6) Location Services

- Campus suggestions and nearby endpoints absent.
  - Remediation: Add endpoints and a `locations` table if following docs; or integrate an external catalog. Implement geospatial indices and queries.

### 7) Config/Infra

- application.yml not reviewed here, but ensure Flyway enabled and Postgres configured; add PostGIS if using spatial.

### 8) Validation

- Add validators for title length, dates (end after start), capacity positive, allowed categories/status values, and ticket constraints.

### 9) Response Enrichment

- Docs require organizer info, attendance metrics, distance/timeUntilStart, images URLs. Current responses likely return basic event fields only.
  - Remediation: Enrich response via joins/service calls (user-service) and computed fields; cache where needed.

---

## Remediation Plan

1) Schema updates: add missing columns per docs; add PostGIS POINT and GIST index for coordinates; add arrays and GIN indexes.
2) Update `Event` entity, DTOs, and mapper to handle new fields and nested structures.
3) Implement Attendance endpoints and persistence with proper status semantics and counts.
4) Add Locations endpoints and data source with geospatial queries.
5) Extend repository methods for rich filtering and distance sorting; add full-text indexes if used.
6) Add validation annotations and service-level checks.
7) Enrich responses with organizer info and attendance summaries; compute distance/timeUntilStart.
8) Add tests for RSVP flows, filters, and geospatial queries.








