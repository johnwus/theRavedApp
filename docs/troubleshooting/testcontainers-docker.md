# Troubleshooting: Testcontainers and Docker

Symptoms:
- `IllegalStateException: Could not find a valid Docker environment`
- Tests fail at container startup for Postgres/Kafka

Checklist:
- Is Docker Desktop running? (Windows/macOS)
- On Windows + WSL2: ensure integration is enabled and Linux backend is on
- Confirm `docker ps` works in the same shell user Maven runs under
- Check VPN/Firewall that may block Docker

Commands:
- Run ITs: `mvn -DskipITs=false verify`
- Skip ITs: `mvn -DskipITs=true verify`

Notes:
- Module POM sets skipITs=true by default to keep CI green without Docker

