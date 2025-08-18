# User Service — Errors Encountered (2025-08-15)

This document captures the specific issues identified while stabilizing user-service and getting its unit/integration tests green.

## 1) StudentVerificationRepository method/entity mismatches
- Symptoms:
  - Spring Data failed to resolve repository methods or returned incorrect data.
  - Integration tests around student verification queries failed.
- Root causes:
  - Method names referenced non-existent fields (e.g., `status` vs `verificationStatus`).
  - Direct FK field names used instead of nested property traversal (e.g., `findByUserId(...)` instead of `findByUser_Id(...)`).
  - JPQL referenced `sv.status` instead of `sv.verificationStatus`.
- Representative failures:
  - NoSuchMethod/PropertyReference exceptions during context startup or query execution.

## 2) JWT HMAC key too short (HS256 WeakKey)
- Symptoms:
  - Runtime `WeakKeyException` or similar when building the signing key.
  - Security-related integration tests failed early in context initialization.
- Root cause:
  - `jwt.secret` string in test config was under 32 bytes for HS256.

## 3) users.student_id NOT NULL constraint
- Symptoms:
  - `DataIntegrityViolationException` on user creation paths that omit studentId.
  - Flyway migrations succeeded, but tests inserting users without studentId failed.
- Root cause:
  - Schema enforced NOT NULL, while tests (and some flows) allow null studentId.

## 4) user.updated event payload missing fields
- Symptoms:
  - `ProfileServiceUpdateIT` expected `bio` and `phoneNumber` in the Kafka payload.
  - Test consumed event but assertions failed due to missing keys.
- Root cause:
  - `ProfileServiceImpl` only included a subset of profile fields in the published map.

## 5) Benign warnings observed (for awareness)
- Spring Data Redis repository scan reported no Redis repos; these are informational and expected in this setup.
- Kafka broker logs showed `LEADER_NOT_AVAILABLE` during topic creation; this is normal during Embedded Kafka/Testcontainers bootstrap.

