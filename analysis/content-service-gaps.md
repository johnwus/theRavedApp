## Content Service — Gaps Analysis and Remediation Plan

This compares `server/content-service` with `docs/services/content-service.md` and lists gaps with fixes.

### 1) REST API Paths vs Docs

- **Gap — Base paths differ**: Controllers use `/api/posts`, `/api/feed`, `/api/media`, `/api/tags`. Docs expect `/api/v1/...` (e.g., `/api/v1/posts`, `/api/v1/posts/feed`, etc.).
  - Evidence:
    ```19:22:server/content-service/src/main/java/com/raved/content/controller/PostController.java
    @RequestMapping("/api/posts")
    ```
  - Remediation: Rename bases to `/api/v1/posts`, `/api/v1/feed`, `/api/v1/media`, `/api/v1/tags`; add compatibility redirects.

- **Gap — Missing endpoints from docs**:
  - Stories endpoints (`/api/v1/stories/*`) not present.
  - Comments endpoints (`/api/v1/posts/{postId}/comments`, comment CRUD) not present.
  - Engagement endpoints (`/like`, `/bookmark`, `/share`, engagement read) not exposed here; may live in Social Service, but docs place them under content.
  - Search endpoints use `/api/feed/*` and `/api/posts/search`; docs specify `/api/v1/content/search`, `/trending`, `/explore`.
  - Remediation: Implement controllers or update docs to reflect split of responsibilities. If staying in content service, add StoriesController, CommentsController, EngagementController and `/api/v1/content` search/explore endpoints.

### 2) Persistence Layer vs Docs (MongoDB)

- **OK — MongoDB used as primary**: `Post` is a MongoDB `@Document`, repository is `MongoRepository`.

- **Gap — Data model fields vs docs**:
  - Docs model emphasizes media arrays, saleDetails, engagement object. `Post` document uses denormalized fields (`likesCount`, `commentsCount`, etc.) and lacks embedded `saleDetails` and media URLs; instead uses `mediaFileIds` references.
  - Remediation: Decide modeling: keep references to a dedicated `media_files` collection (preferred) and update docs; or add fields to match docs if stored together.

- **Gap — Stories and Comments collections absent**: No models/repos for stories/comments. Remediation: Add Mongo documents/repositories and controllers per docs.

### 3) Search & Discovery

- **OK — Elasticsearch integration**: Config and repository for `PostSearchDocument` exists; `PostServiceImpl.searchPosts` uses ES with Mongo fallback.

- **Gap — Docs define `/api/v1/content/search/trending/explore` endpoints**: Current endpoints are under `/api/feed` and `/api/posts/search`.
  - Remediation: Provide `/api/v1/content/search`, `/trending`, `/explore` routes delegating to existing services.

### 4) Media Upload & Processing

- **OK — MediaController and services exist**: Upload, update, process, analytics endpoints present under `/api/media`.
- **Gap — Validation/rate limits in docs not enforced**: No explicit request validators for file size/type or per-user rate limits.
  - Remediation: Add validation (max sizes, types), and a Redis-backed rate limiter per user with config thresholds.

### 5) Feed Generation

- **OK — Rich feed APIs implemented** under `/api/feed` with Redis caching and personalized/trending/university/faculty/hashtag/popular/mixed/scheduled/featured/pinned.
- **Gap — Path alignment**: Should be `/api/v1/posts/feed` per docs for base feed; or update docs to reflect `/api/feed` family.

### 6) Kafka Events

- **OK — Kafka producer for content.created**: `ContentEventsProducer.publishPostCreated` exists.
- **Gap — Additional topics from docs not implemented**: `content.updated`, `content.deleted`, `content.engagement`, `content.moderation.required`. No Kafka listeners for `user.updated`, `social.follow`, `moderation.decision`.
  - Remediation: Implement additional producers and consumers per docs.

### 7) Controllers/DTOs vs Docs Payloads

- **Gap — CreatePostRequest fields differ**: Docs example includes caption, tags, location, mediaUrls, saleDetails; current `CreatePostRequest` has `content`, `contentType`, tags, seo fields, and `mediaFileIds`, no saleDetails/location.
  - Remediation: Extend DTOs to include `caption` aliasing `content`, `location`, optional `saleDetails` object; map appropriately.

- **Gap — PostResponse author details/engagement flags**: Docs response includes nested user info and engagement flags; `PostResponse` has placeholders (author fields, liked/bookmarked flags) but no population path shown.
  - Remediation: Populate via user-service and social-service clients or aggregation layer; document eventual consistency.

### 8) Validation & Limits

- **Gap — Data validation rules not enforced**: Max caption length, tags count/length, media sizes, story duration, comment length from docs not enforced.
  - Remediation: Add validators (e.g., `ContentValidator`) for DTOs and enforce in controllers/services.

### 9) Configuration

- **OK — Mongo/Redis/Elasticsearch/Kafka present** and eureka/management configured.
- **Gap — Profiles**: `application.yml` uses `local/staging/production` blocks; `application-dev.yml` also exists and may conflict. Align naming (`local` vs `dev`).

### 10) Algorithms

- Repositories and services include trending/engagement/virality score usage but algorithms classes listed exist (`algorithm/*`). Ensure they’re integrated where needed.

---

## Remediation Plan

1) API Path Alignment
- Change controller base paths to `/api/v1/...` or update docs to match current split.

2) Implement Missing Domains
- Add Stories: `Story` document, repo, service, controller (`/api/v1/stories`).
- Add Comments: `Comment` document, repo, service, controller; endpoints under posts.
- Add Engagement endpoints if content service owns them; otherwise, update docs to Social Service ownership and add internal sync.

3) DTO and Mapping Enhancements
- Extend `CreatePostRequest`/`UpdatePostRequest` with `caption`, `location`, `saleDetails` and map in `PostMapper`.
- Populate `PostResponse` author and engagement flags via clients to user/social services or via aggregator.

4) Validation & Rate Limiting
- Add request validators for caption length, tags count/length, and media constraints.
- Add Redis-backed rate limiter for post/story/comment creation/search per user as per docs limits.

5) Kafka Events & Listeners
- Add producers for `content.updated`, `content.deleted`, `content.engagement`, `content.moderation.required`.
- Add listeners for `user.updated`, `social.follow`, `moderation.decision` to keep content cache/search updated.

6) Search/Explore Endpoints
- Add `/api/v1/content/search`, `/api/v1/content/trending`, `/api/v1/content/explore` routing to existing services.

7) Profiles & Config
- Standardize on `local`, `staging`, `production`. Remove or rename `application-dev.yml`.

8) Tests & CI
- Add tests for new controllers, validators, and Kafka flows; verify Mongo/ES queries and pagination.

9) Docs Sync
- If keeping reference-based media model and external services for engagement, update docs accordingly.








