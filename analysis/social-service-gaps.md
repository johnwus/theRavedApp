## Social Service — Gaps Analysis and Remediation Plan

Compared `server/social-service` against `docs/services/social-service.md`.

### 1) API Paths and Coverage

- **Base paths differ from docs**
  - Docs: `/api/v1/social/...`
  - Code: `/api/v1/follows`, `/api/v1/likes`, `/api/v1/comments`, `/api/v1/activity`.
  - Evidence:
    ```11:14:server/social-service/src/main/java/com/raved/social/controller/FollowController.java
    @RequestMapping("/api/v1/follows")
    ```
  - Remediation: Either introduce a unified `/api/v1/social/*` gateway controller or update docs to reflect current resource-based paths. Optionally add sub-paths: `/api/v1/social/follows`, `/likes`, etc.

- **Missing endpoints vs docs**
  - Bookmarks: No controller/repo/model for bookmarks; docs define bookmark toggle and listing.
  - Shares: No explicit `/share` endpoint; sharing recorded only as activity.
  - Recommendations: No recommendation endpoints or service.
  - Social feed endpoint not present here (feed lives in content service). Docs have `/api/v1/social/feed`.
  - Privacy/settings endpoints missing.
  - Remediation: Implement BookmarkController/Service/Model, ShareController or integrate with Activity + Content; add Recommendation endpoints and service; add SettingsController and persistence; clarify feed ownership (content vs social) and update docs accordingly.

### 2) Data Models vs Docs (MongoDB)

- **Follows model present** with compound unique index and status.
  - Matches connections concept but lacks fields like `mutualFollow` computation and connection strength updates.
  - Remediation: Add periodic jobs or triggers to compute `isMutual` and relationship strength; expose in responses.

- **Interactions model missing**
  - Docs: generic `social_interactions` for like/bookmark/share with uniqueness and metadata.
  - Code: `Like` has its own collection; bookmarks and shares missing as collections.
  - Remediation: Either keep separate collections and update docs, or create unified `SocialInteraction` collection and refactor `Like` into it, plus add `Bookmark` and `Share` handling.

- **Comments model exists**, with repository and service implemented; aligns with docs’ comment operations but API path differs.

### 3) Controllers and Services

- **FollowController/Service**: follow/unfollow implemented via body payloads; docs specify `POST /follow` and `DELETE /follow/{userId}`. Paths and payload differ.
  - Remediation: Add DELETE by path variant; add list endpoints for followers/following with pagination per docs.

- **LikeController/Service**: like/unlike generic target supported; aligns with docs conceptually, but path differs and bookmark/share flows are absent.

- **CommentController/Service**: create/update/delete/list by post present; good.

- **ActivityController/Service**: generic social activity with publishing; not explicitly in docs but useful. Map doc events to activity emission.

### 4) Rate Limiting

- Docs specify per-action hourly limits; no limiter visible in controllers/services.
  - Remediation: Add Redis-backed rate limiter middleware or service for follow/like/bookmark/share/feed endpoints with configurable thresholds.

### 5) Eventing & Integration

- SocialEventPublisher used in activities; docs include engagement analytics and cross-service events. Verify producers/consumers for like/comment/follow.
  - Remediation: Ensure publishing on follow/like/comment controllers, not only via ActivityService; add consumers if needed for analytics.

### 6) Repository and ID Conventions

- Code stores IDs as strings for Mongo; controllers sometimes accept numeric IDs and convert via `MongoIdConverter`.
  - Remediation: Standardize ID usage in API (strings per Mongo or numeric consistently with gateway conversion).

### 7) Docs Alignment on Ownership

- Docs place bookmarks and shares under social; currently missing. Feed in docs under social, but implementation exists in content service.
  - Remediation: Decide ownership: if content service owns feeds, update docs; if social owns, move/duplicate minimal endpoint proxying to content feed.

---

## Remediation Plan

1) Paths and Aggregation
- Introduce `/api/v1/social` base with sub-resources (`/follows`, `/likes`, `/comments`, `/bookmarks`, `/shares`) or update docs to resource paths.

2) Implement Missing Features
- Bookmark: Add model/repo/service/controller; list bookmarks with pagination and optional content enrichment.
- Share: Add endpoint to record shares; reuse ActivityService for event publishing.
- Recommendations: Implement service and endpoints; leverage follow graph and possibly user-service data.
- Settings: Add model and endpoints for social privacy settings.

3) Rate Limiting
- Implement Redis-based limiter for follow/like/bookmark/share/feed requests matching docs limits.

4) Events
- Ensure like, comment, follow, bookmark, share publish engagement events for analytics and notifications.

5) Tests
- Add integration tests for new endpoints and rate limiting; validate repository uniqueness and pagination.

6) Docs Sync
- Update docs to reflect actual ownership (feeds in content) and path structure or refactor to match docs.








