# social-service — Implementation Analysis

Purpose
- Manage likes/comments/follows/activity; MongoDB as primary; Redis for caching; potential ES for search/analytics.

Current implementation (observed)
- Main: com.raved.social.SocialServiceApplication (@EnableDiscoveryClient)
- Controllers: LikeController, CommentController, FollowController, ActivityController — currently placeholders without @RestController annotations
- Services: LikeServiceImpl, ActivityServiceImpl (impl present); other services likely present
- Repositories: LikeRepository, ActivityRepository (MongoRepository with rich queries)
- application.yml: Mongo and Redis configured; Eureka default updated; profiles (local/staging/production)

Gaps / TODOs
- Controllers are placeholder shells; need full @RestController implementations with endpoints, validation, and consistent responses per docs/services/social-service.md
- No explicit caching annotations or RedisTemplate usage surfaced; define caching strategy (key patterns, TTLs) for hot paths (counts, timelines)
- ES usage present in pom; actual integration not visible; decide scope (optional)
- Tests are missing for controllers/services

Recommended actions (priority)
1. High: Implement REST controllers with versioned paths (/api/v1/social, /api/v1/likes, /api/v1/comments, /api/v1/follows) and wire services
2. Medium: Add caching with @Cacheable/@CacheEvict or RedisTemplate where appropriate; document keys/TTLs
3. Medium: Add tests for services and controllers
4. Low: If ES is retained, add repositories/operations and index strategy; else remove dependency

