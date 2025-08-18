# Error: Testcontainers could not find Docker environment

- Module: server/user-service
- Command: `mvn -DskipITs=false verify`
- Symptoms:
  - `IllegalStateException: Could not find a valid Docker environment`
  - Testcontainers logs show no valid Docker configuration

## Cause
- Docker Desktop/Engine not running or not available to the build user

## Impact
- Fails ITs that depend on Testcontainers (Postgres, Kafka)

## Temporary Workaround
- Skip ITs: `mvn -DskipITs=true verify` (default in module POM)

## Proper Fix
- Start Docker Desktop (Windows/macOS) or ensure Docker Engine is running (Linux)
- Re-run with `mvn -DskipITs=false verify`

