# User Service — Phase A Gap Assessment

This document summarizes the current state of server/user-service against specs in docs/services/user-service.md, DOCTYPE.html expectations, and PROTOTYPE_DATABASE_MODEL.md.

## Folder Structure (observed)
- config/: DatabaseConfig, JwtConfig, RedisConfig, SecurityConfig, UserServiceConfig
- controller/: AuthController (/api/auth), ProfileController (/api/profile), UserController (/api/users)
- dto/: request (RegisterRequest, LoginRequest, UpdateProfileRequest, etc.), response (AuthResponse, UserResponse, ProfileResponse, etc.)
- event/: UserEventPublisher (present)
- exception/: InvalidCredentialsException, UserNotFoundException, VerificationFailedException
- mapper/: UserMapper, StudentVerificationMapper
- model/: User, University, Faculty, StudentVerification, UserSession, UserStatus
- repository/: UserRepository, StudentVerificationRepository, FacultyRepository
- security/: JwtAuthenticationFilter, JwtTokenProvider, CustomUserDetailsService
- service/: interfaces (AuthService, JwtService, ProfileService, StudentVerificationService, UserService); impl/ implementations
- resources/: application.yml (local/staging/production profiles), Flyway migrations V1..V4

## application.yml & Dependencies
- DataSource: PostgreSQL with standardized creds defaults (username raved_admin, password theRAVEDapp#123). OK per preference.
- JPA: ddl-auto validate; Flyway enabled with migrations present.
- Eureka/Actuator/Logging/JWT configured.
- POM includes: spring-web, data-jpa, security, validation, actuator, redis, eureka-client, kafka, postgresql, jjwt, mapstruct.

## API vs Spec (docs/services/user-service.md)
Documented endpoints use /api/v1/... while current controllers use older paths.

Missing or mismatched endpoints:
1) Auth
- Spec: POST /api/v1/auth/register, /api/v1/auth/verify-email, /api/v1/auth/verify-phone, /api/v1/auth/login, /api/v1/auth/refresh, /api/v1/auth/logout
- Current: /api/auth/register, /api/auth/login, /api/auth/refresh (query param refreshToken), /api/auth/logout; verify email/phone minimal or missing; forgot/reset password present but not versioned.

2) Profile
- Spec: GET /api/v1/users/profile (current user), PUT /api/v1/users/profile, POST /api/v1/users/avatar
- Current: ProfileController at /api/profile/{userId} for GET/PUT, /{userId}/picture for upload/delete; lacks /api/v1/users/... pattern and auth-context profile endpoint; DTOs exist.

3) Settings
- Spec: GET /api/v1/users/settings, PUT /api/v1/users/settings/privacy, PUT /api/v1/users/settings/preferences
- Current: No dedicated Settings endpoints or model; user_settings table not present in migrations.

4) Validation
- Spec: GET /api/v1/users/validate/username/{username}, GET /api/v1/users/validate/email/{email}
- Current: /api/users/exists/username/{username}, /api/users/exists/email/{email}; different paths.

5) Verification flows
- Spec: verify-email and verify-phone endpoints with code; verification_codes table expected.
- Current: StudentVerification entity exists for student ID; no verification_codes table/migration; AuthController has placeholder verifyEmail(token) only; no phone verification.

## Models vs Prototype DB
- Users table: present and close to prototype; uses BIGSERIAL vs UUID in spec doc, but PROTOTYPE_DATABASE_MODEL uses BIGSERIAL too — OK.
- user_settings: Missing table and model.
- user_verification / verification_codes: Missing (only student_verifications exists).
- user_sessions: Present (but columns differ slightly from service-doc’s hash-based tokens vs current session_token). Acceptable but may need hash storage later.
- universities, faculties: Present with migrations — OK.

## Repositories/Queries
- Basic repositories exist; no custom queries surfaced for username/email/phone availability suggestions as per docs; rate-limiting not implemented.

## Business Logic Gaps
- Multi-step registration flow not implemented (RegisterRequest is single step).
- Verify email/phone: no code send/verify flows; no integration with Notification Service for codes.
- JWT RBAC: roles exist, but RBAC enforcement not audited in controllers.
- Event publishing (user.created/user.updated) not observed in AuthServiceImpl.
- Resilience: No Feign clients or Resilience4j usage observed.

## API Layer Gaps
- Versioning: endpoints not under /api/v1/*; backward compatibility needed while adding v1.
- Swagger/OpenAPI: not found.
- Rate limiting: not found.
- Error format standardization: custom exceptions exist; no global error handler noted.

## Config & Infra Gaps
- Kafka topics config not present in application.yml; no producers/consumers wired.
- Micrometer/Prometheus exposure minimal; consider structured JSON logging & correlation IDs.

## Testing
- No unit/integration tests enumerated in module; coverage status unknown.

## Summary — Action Items (Phases B–D setup)
- Add versioned controllers (/api/v1/*) implementing spec while keeping existing routes for compatibility.
- Introduce user_settings entity + migration; DTOs and mapper updates.
- Add verification_codes table + flows (send via Notification Service, verify endpoints); phone/email.
- Add validation endpoints under /api/v1/users/validate/* with suggestions.
- Implement event publishing for user.created/user.updated via KafkaTemplate; configure Kafka.
- Add Feign clients (Notification, Content, Ecommerce as needed) with Resilience4j configs.
- Add Settings endpoints and services.
- Wire Swagger/OpenAPI and rate limiting.
- Add MapStruct mappings for new DTOs; adjust AuthService to support multi-step registration (session token for in-progress).
- Write unit and integration tests (Testcontainers PG + Embedded Kafka).

