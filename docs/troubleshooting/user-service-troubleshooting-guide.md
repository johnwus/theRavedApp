# User Service — Troubleshooting Guide

Use this guide to diagnose and resolve common issues in user-service. It is based on fixes applied on 2025-08-15.

## A. Repository/Entity Mismatch Errors
- Symptoms:
  - Spring Data `PropertyReferenceException` or query failures at startup.
  - Methods like `findByUserIdAndStatus` fail to resolve.
- Checklist:
  1. Confirm entity field names (e.g., `verificationStatus` vs `status`).
  2. Use nested property traversal for relationships: `findByUser_Id`, `findByUniversity_Id`.
  3. Align JPQL to entity fields: `sv.verificationStatus`.
  4. Update service calls after renames.
- Quick test:
  - Run: `mvn -q -Dtest=UsersEventsIT#publishesUserCreatedEvent test` to validate context + event flow.

## B. JWT WeakKey / Signing Failures
- Symptoms:
  - `WeakKeyException` or similar when building HS256 keys.
- Fix:
  - Ensure `jwt.secret` ≥ 32 bytes (Base64-encoded for HS256). Example test value:
    - `bXktdGVzdC1zZWNyZXQtbXktdGVzdC1zZWNyZXQtbXktdGVzdC1zZWNyZXQ=`

## C. DataIntegrityViolation on users.student_id
- Symptoms:
  - Creation of users without studentId fails.
- Fix:
  - Flyway migration: `ALTER TABLE users ALTER COLUMN student_id DROP NOT NULL;`
  - Entity: `@Column(nullable = true)` for `studentId`.

## D. Event Payload Gaps (user.updated)
- Symptoms:
  - ITs expecting keys like `bio` or `phoneNumber` fail.
- Fix:
  - Include these fields in `ProfileServiceImpl` payload before publishing.

## E. Benign test logs
- `LEADER_NOT_AVAILABLE` around topic creation is normal with Testcontainers/Embedded Kafka.
- Spring Data Redis scanning without Redis repositories is informational.

## F. Verification flow
- Minimal loop:
  1. Run failing/scope-focused test.
  2. Apply smallest safe fix.
  3. Re-run targeted test, then full suite with `-Pwith-it`.
  4. Commit with descriptive message.

