# User Service — Fixes Implemented (2025-08-15)

This document records the exact solutions applied to stabilize user-service.

## Repository and Query Alignment
- Renamed repository methods to match entity fields and relationships:
  - `findByUserId` → `findByUser_Id`
  - `findByUserIdAndStatus` → `findByUser_IdAndVerificationStatus`
  - `findByUserIdAndUniversityId` → `findByUser_IdAndUniversity_Id`
  - `findByStatus` (+ pageable variants) → `findByVerificationStatus` (and sorted variants)
  - `findByUniversityId` → `findByUniversity_Id`
  - `findByUniversityIdAndStatus` → `findByUniversity_IdAndVerificationStatus`
  - `countByStatus` → `countByVerificationStatus`
  - `countByUniversityIdAndStatus` → `countByUniversity_IdAndVerificationStatus`
  - `existsByUniversityIdAndStudentId` → `existsByUniversity_IdAndUser_StudentId`
- Updated JPQL to use `sv.verificationStatus` instead of `sv.status`.
- Updated service layer usages accordingly (`StudentVerificationServiceImpl`).

## Database Constraint Adjustment
- Added Flyway migration `V10__Alter_users_student_id_nullable.sql`:
  - `ALTER TABLE users ALTER COLUMN student_id DROP NOT NULL;`
- Updated `User` entity field:
  - `@Column(name = "student_id", nullable = true, unique = true, length = 50)`.

## Security/Test Configuration
- Strengthened test JWT key:
  - `src/test/resources/application.yml` sets a 256-bit Base64 secret for HS256.

## Event Payload Enhancement
- `ProfileServiceImpl` now publishes `bio` and `phoneNumber` alongside existing fields in `user.updated`.

## Verification
- Command: `mvn -Pwith-it -DskipITs=false verify` in `server/user-service`
- Result: All tests passed. Benign Surefire shutdown message observed; build successful.

