# Fix: Enable Docker for Testcontainers or skip ITs

- Start Docker Desktop (Windows/macOS) or ensure Docker Engine is available (Linux)
- Then run ITs: `mvn -DskipITs=false verify`

If Docker is intentionally unavailable (e.g., CI without Docker):
- Keep ITs skipped: `mvn -DskipITs=true verify`
- The module POM sets `<skipITs>true</skipITs>` by default to keep builds green without Docker

