## Realtime Service — Gaps Analysis and Remediation Plan

This document compares the implemented realtime service (`server/realtime-service`) against the specifications in the docs (`docs/services/realtime-service.md`, `docs/services/realtime-service-auth.md`). It details every identified gap and provides concrete remediation steps.

### 1) API Surface (REST + WebSocket) vs Docs

- **Gap — WebSocket connect endpoint path shape OK; REST prefixes differ from docs**
  - Docs specify WebSocket connect at `/api/v1/realtime/connect` and REST endpoints under `/api/v1/realtime/...` for conversations, messages, presence.
  - Implementation registers STOMP endpoint correctly at `/api/v1/realtime/connect`:
    ```35:39:server/realtime-service/src/main/java/com/raved/realtime/config/WebSocketConfig.java
    registry.addEndpoint("/api/v1/realtime/connect")
            .setAllowedOriginPatterns(allowedOrigins)
            .withSockJS();
    ```
  - REST controllers use different base paths:
    ```14:18:server/realtime-service/src/main/java/com/raved/realtime/controller/ChatController.java
    @RestController
    @RequestMapping("/api/v1/chat")
    ```
    ```8:18:server/realtime-service/src/main/java/com/raved/realtime/controller/WebSocketController.java
    @RestController
    @RequestMapping("/api/v1/ws")
    ```
  - Docs require endpoints like `GET /api/v1/realtime/conversations`, `GET /api/v1/realtime/conversations/{id}/messages`, `PUT /api/v1/realtime/messages/{messageId}/read`, `GET /api/v1/realtime/presence/{userId}`.

  - **Remediation**:
    - Unify REST base path to `/api/v1/realtime`.
    - Add missing endpoints for conversations listing, conversation messages with pagination, mark-as-read, and presence read APIs as per docs.
    - Keep `/api/v1/chat` and `/api/v1/ws` temporarily with deprecation headers or 301 redirects until clients migrate.

- **Gap — Notification broadcasting endpoint is a stub**
  - Implementation:
    ```6:15:server/realtime-service/src/main/java/com/raved/realtime/controller/NotificationController.java
    @RequestMapping("/api/v1/notifications")
    ...
    // TODO: inject message broker and dispatch broadcast
    ```
  - **Remediation**: Inject `MessageBroker` and implement broadcast logic. Align path under `/api/v1/realtime/notifications` or keep as a distinct service per architecture.

### 2) STOMP Destinations & Interceptor (Auth, Membership, Rate Limits)

- **OK — STOMP mapping aligns with docs**
  - Handler uses `/app/chat/rooms/{roomId}/send` and broadcasts to `/topic/room/{roomId}`:
    ```16:19:server/realtime-service/src/main/java/com/raved/realtime/websocket/ChatWebSocketHandler.java
    @MessageMapping("/chat/rooms/{roomId}/send")
    messageBroker.broadcastToRoom(roomId, "NEW_MESSAGE", payload);
    ```

- **OK — Auth enforcement present; header contract aligns**
  - Interceptor enforces JWT for `SEND` and `SUBSCRIBE`, supports `authorization: Bearer <jwt>` and optional `x-user-id`:
    ```71:83:server/realtime-service/src/main/java/com/raved/realtime/websocket/WebSocketChannelInterceptor.java
    if (command == StompCommand.SEND || command == StompCommand.SUBSCRIBE) {
        if (token == null) return null;
        var optUser = jwtAuthenticator.authenticate(token);
        if (optUser.isEmpty()) return null;
    }
    ```
  - Matches docs:
    ```18:23:docs/services/realtime-service-auth.md
    A valid JWT is required for SEND and SUBSCRIBE frames.
    ... checks ChatService.isUserInChatRoom(roomId, userId)
    ```

- **Gap — Membership validation depends on unimplemented participant management**
  - Interceptor calls `chatService.isUserInChatRoom(roomId, uid)` but `ChatServiceImpl` has TODOs and no actual persistence updates for membership:
    ```352:366:server/realtime-service/src/main/java/com/raved/realtime/service/impl/ChatServiceImpl.java
    private void addParticipant(Long chatRoomId, Long userId) {
        // TODO: Implement participant management
    }
    ```
  - **Remediation**: Implement `ChatRoomMemberRepository` and actual CRUD of membership (join/leave, admin roles, last_read_at). Update `ChatServiceImpl` to write to `chat_room_members` consistently.

- **OK — Rate limiting present (local + Redis-backed)**
  - `RateLimiterService` supports Redis and local fallback:
    ```19:27:server/realtime-service/src/main/java/com/raved/realtime/websocket/RateLimiterService.java
    if (redis != null) {
        Long count = redis.opsForValue().increment(key);
        ...
    }
    ```

### 3) Security & JWT

- **OK — JWT secret configuration and parsing**
  - JWT secret from config with sensible default:
    ```21:24:server/realtime-service/src/main/java/com/raved/realtime/security/JwtAuthenticator.java
    Claims claims = JwtUtils.parseClaimsWithRawSecret(token, jwtSecret);
    ```
  - Config:
    ```21:24:server/realtime-service/src/main/resources/application.yml
    jwt:
      secret: ${JWT_SECRET:mySecretKey}
    ```

### 4) Messaging Backbone (Docs vs Code)

- **Gap — Docs specify Kafka; code uses RabbitMQ (and SimpMessagingTemplate)**
  - Docs dependencies include Kafka:
    ```439:446:docs/services/realtime-service.md
    <dependency>
        <groupId>org.springframework.kafka</groupId>
    ```
  - Code references RabbitMQ config and listeners:
    ```15:19:server/realtime-service/src/main/java/com/raved/realtime/messaging/ChatMessageListener.java
    @RabbitListener(queues = RabbitMQConfig.CHAT_QUEUE)
    ```
  - **Remediation**: Decide on a single backbone. Either:
    - Switch to Kafka per docs (replace RabbitMQ config/listeners, add Kafka templates/topics), or
    - Update docs to RabbitMQ and remove Kafka from the service spec.

### 5) Data Models vs Migrations

- **Gap — `chat_rooms` entity vs migration (missing columns + naming)**
  - Entity expects several columns not in migration (e.g., `room_id`, `type`, `is_private`, `max_participants`, `current_participants`, `last_activity_at`):
    ```24:67:server/realtime-service/src/main/java/com/raved/realtime/model/ChatRoom.java
    @Column(name = "room_id", unique = true, nullable = false)
    ...
    @Column(name = "is_private", nullable = false)
    @Column(name = "max_participants")
    @Column(name = "current_participants")
    @Column(name = "last_activity_at")
    ```
  - Migration creates a minimal table without these fields:
    ```1:14:server/realtime-service/src/main/resources/db/migration/V1__Create_chat_rooms_table.sql
    CREATE TABLE IF NOT EXISTS chat_rooms (
        id BIGSERIAL PRIMARY KEY,
        room_type VARCHAR(20) NOT NULL,
        name VARCHAR(255),
        description TEXT,
        avatar_url TEXT,
        created_by BIGINT NOT NULL,
        faculty_id BIGINT,
        is_active BOOLEAN DEFAULT true,
        last_message_at TIMESTAMP,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
    ```
  - **Remediation**: Add Flyway migration to alter `chat_rooms` to include missing columns and indexes; align enum storage (`room_type` vs `type`), and add `room_id` unique identifier.

- **Gap — `messages` entity vs migration (status, delivered_at/read_at, attachment, metadata)**
  - Entity fields used by service:
    ```271:333:server/realtime-service/src/main/java/com/raved/realtime/model/Message.java
    private LocalDateTime deliveredAt; private LocalDateTime readAt;
    private MessageStatus status; private String attachmentUrl; private String metadata;
    ```
  - Migration lacks these columns:
    ```1:17:server/realtime-service/src/main/resources/db/migration/V3__Create_messages_table.sql
    CREATE TABLE IF NOT EXISTS messages (
        id BIGSERIAL PRIMARY KEY,
        room_id BIGINT NOT NULL ...
        message_type VARCHAR(20) DEFAULT 'TEXT',
        content TEXT,
        media_url TEXT,
        media_metadata JSONB,
        reply_to_message_id BIGINT ...,
        is_edited BOOLEAN DEFAULT false,
        is_deleted BOOLEAN DEFAULT false,
        edited_at TIMESTAMP,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
    ```
  - Service writes `DELIVERED` and uses `deliveredAt`, `readAt`, `status`:
    ```387:395:server/realtime-service/src/main/java/com/raved/realtime/service/impl/MessageServiceImpl.java
    message.setStatus(MessageStatus.DELIVERED);
    message.setDeliveredAt(LocalDateTime.now());
    ```
  - **Remediation**: Add migration to add `status`, `delivered_at`, `read_at`, `updated_at`, `attachment_url`, `metadata` columns; ensure defaults and indexes on `(room_id, created_at)` remain.

- **Gap — `message_reactions` entity vs migration (emoji + unique key)**
  - Entity includes `emoji` and unique constraint on `(message_id, user_id, reaction_type)`, with emoji as optional:
    ```31:38:server/realtime-service/src/main/java/com/raved/realtime/model/MessageReaction.java
    @Column(name = "emoji", length = 10)
    ```
  - Repository queries by emoji:
    ```22:26:server/realtime-service/src/main/java/com/raved/realtime/repository/MessageReactionRepository.java
    Optional<MessageReaction> findByMessageIdAndUserIdAndEmoji(...)
    ```
  - Migration has no `emoji` and unique index only on `(message_id, user_id)`:
    ```1:15:server/realtime-service/src/main/resources/db/migration/V4__Create_message_reactions_table.sql
    CREATE TABLE ... reaction_type VARCHAR(20) NOT NULL;
    CREATE UNIQUE INDEX ... (message_id, user_id);
    ```
  - **Remediation**: Add `emoji` column, adjust unique index to `(message_id, user_id, emoji)` or keep `reaction_type` and align repository accordingly. Drop/replace the existing unique index.

- **Gap — `user_presence` entity exists; no migration**
  - Entity:
    ```12:21:server/realtime-service/src/main/java/com/raved/realtime/model/UserPresence.java
    @Entity @Table(name = "user_presence" ...)
    ```
  - No Flyway migration for `user_presence` table.
  - **Remediation**: Add a Flyway migration to create `user_presence` with indexes on `status`, `is_online`, `last_active_at`.

- **Gap — Docs specify `websocket_connections`; not implemented**
  - Docs schema includes `websocket_connections` and `conversation_*` tables using UUIDs. Code uses numeric IDs and does not persist websocket connections.
  - **Remediation**: Either (a) add `websocket_connections` table and persistence hooks on connect/disconnect, or (b) update docs to remove this if not part of MVP.

### 6) Repository & Type Issues

- **Gap — Type mismatch for roomId parameters**
  - Several repository methods accept `String roomId` but query against `ChatRoomMember.roomId` which is `Long` (FK to `chat_rooms.id`):
    ```45:55:server/realtime-service/src/main/java/com/raved/realtime/repository/ChatRoomRepository.java
    List<Long> findParticipantsByRoomId(@Param("roomId") String roomId);
    boolean isUserParticipant(@Param("roomId") String roomId, @Param("userId") Long userId);
    ```
  - **Remediation**: Change these method signatures to take `Long roomId` and adapt callers to resolve external `roomId` string to internal numeric `id` first.

- **Gap — Missing `ChatRoomMemberRepository`**
  - Entity exists (`ChatRoomMember`) but no repository interface to manage it; `ChatServiceImpl` has TODOs for participants.
  - **Remediation**: Add `ChatRoomMemberRepository` with CRUD and helper queries (find active by room, by user, last_read_at updates, role checks).

### 7) Services & Business Logic

- **Gap — Participant management TODOs**
  - `ChatServiceImpl` does not actually persist participants on create/join/leave:
    ```358:372:server/realtime-service/src/main/java/com/raved/realtime/service/impl/ChatServiceImpl.java
    // TODO: Implement participant management
    ```
  - **Remediation**: Implement add/remove/update using `ChatRoomMemberRepository` and keep `current_participants` in sync.

- **Gap — Edit/Delete/Read flows depend on missing DB columns**
  - `MessageServiceImpl` writes `status`, `deliveredAt`, `readAt`, soft-deletes with content changes, but migration lacks necessary columns. See “Data Models vs Migrations”.

- **Gap — Presence APIs not exposed**
  - `PresenceService` exists with rich methods, but no REST endpoints to update/read presence per docs.
  - **Remediation**: Add REST controller under `/api/v1/realtime/presence` to expose `getUserPresence`, `updatePresence`, etc.

### 8) Configuration & Environments

- **Gap — Profile naming inconsistent**
  - `application.yml` uses `on-profile: local` blocks, while `application-dev.yml` sets `spring.profiles.active: development`:
    ```1:7:server/realtime-service/src/main/resources/application-dev.yml
    spring:
      profiles:
        active: development
    ```
  - **Remediation**: Standardize profiles (`local`, `staging`, `production`). Remove `application-dev.yml` or rename to `application-local.yml` if intended.

- **OK — Allowed origins configuration flexible**
  - Supports both `websocket.allowed-origins` and `websocket.allowed.origins`:
    ```17:21:server/realtime-service/src/main/java/com/raved/realtime/config/WebSocketConfig.java
    @Value("${websocket.allowed-origins:${websocket.allowed.origins:*}}")
    ```

### 9) Docs vs Implementation Naming & Domain

- **Gap — Docs use `conversations` nomenclature and UUIDs; code uses `chat_rooms` with numeric IDs**
  - **Remediation**: Either refactor code to match docs (UUID primary keys, `conversations` and `conversation_participants`), or update docs to match the implementation naming (`chat_rooms`, `chat_room_members`, external `roomId` string, internal numeric pk).

### 10) Testing & Metrics

- Tests exist for websocket flows (per file list), but coverage for membership persistence, presence APIs, and REST contracts required by docs is missing.
  - **Remediation**: Add tests for new controllers/endpoints, repository behavior, and migration alignment.

---

## Remediation Plan (Actionable Tasks)

Prioritize schema alignment first to avoid production breakages.

1) Database migrations
- Add `V6__Alter_chat_rooms_add_missing_columns.sql` to add: `room_id` (unique), `type`, `is_private`, `max_participants`, `current_participants`, `last_activity_at`, `updated_at` default.
- Add `V7__Alter_messages_add_status_and_timestamps.sql` to add: `status` (enum or varchar), `delivered_at`, `read_at`, `updated_at`, `attachment_url`, `metadata` (JSONB). Backfill defaults.
- Add `V8__Alter_message_reactions_add_emoji_and_unique.sql` to add `emoji` (length 10), replace unique index with `(message_id, user_id, emoji)` (or adapt repository to use `reaction_type`).
- Add `V9__Create_user_presence.sql` to create `user_presence` table with indexes.
- Optional (if keeping docs): `V10__Create_websocket_connections.sql` for tracking connections.

2) Repositories & Services
- Create `ChatRoomMemberRepository` with CRUD and helper queries: by room, by user, active filter, last_read_at updates, role checks.
- Implement participant management in `ChatServiceImpl` (add/remove/cleanup), enforce max participants, maintain `current_participants`.
- Fix `ChatRoomRepository` method signatures to use `Long roomId` where appropriate; add helper to map external `roomId` string → internal `id`.

3) REST Controllers
- Introduce `/api/v1/realtime` controller(s):
  - `GET /conversations` (list user’s rooms), `GET /conversations/{id}/messages` (paginated), `POST /conversations` (create), `PUT /messages/{id}/read` (mark read), `GET /presence/{userId}`.
- Move/alias `/api/v1/chat` and `/api/v1/ws` under `/api/v1/realtime`.
- Implement `NotificationController.broadcast` via `MessageBroker` and consider topic partitioning if RabbitMQ/Kafka is used.

4) Messaging Backbone Decision
- Choose Kafka (per docs) or RabbitMQ (current code). If Kafka:
  - Replace RabbitMQ config/listeners with Kafka producer/consumer, define topics, update docs/examples.
  - If RabbitMQ retained, update docs (`realtime-service.md`) to reference RabbitMQ and remove Kafka dependency snippet.

5) Presence APIs
- Add a `PresenceController` to expose presence read/update endpoints and integrate with `PresenceServiceImpl`.
- Optionally persist presence updates into `user_presence` and broadcast via `/topic/presence` as implemented in `MessageBroker`.

6) Configuration & Profiles
- Normalize profiles: use `local`, `staging`, `production`. Remove `application-dev.yml` or adapt to `local` to avoid conflicts.
- Validate `jwt.secret` externally configured; remove default secret in production.

7) Tests & CI
- Add integration tests for new endpoints and membership flows.
- Add tests validating that DB schema matches entities (e.g., JPA schema validation or a Flyway/JPA integration test).

8) Docs Alignment
- If keeping current domain (`chat_rooms`), update `docs/services/realtime-service.md` schemas and REST path examples to match, including numeric IDs and field names.
- If aligning code to docs, plan a refactor: rename entities/repositories, transition to UUID PKs, and write data migrations.

---

## Notable Strengths (No Action Required)
- WebSocket endpoint and STOMP mapping align with docs.
- Robust WebSocket interceptor with JWT validation, membership check hook, and rate limiting.
- `MessageBroker` abstraction over `SimpMessagingTemplate` with presence broadcasting channels aligns with real-time UX.








