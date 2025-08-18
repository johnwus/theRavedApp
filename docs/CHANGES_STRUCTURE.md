# Repository Structure Refactor - Summary

Key changes:
- Consolidated monitoring under infrastructure/docker/monitoring with Prometheus, Grafana, Loki, Promtail, Tempo, OTel collector configs.
- Added reusable GitHub Actions workflow for Docker builds and centralized DOCKER_NAMESPACE.
- Included events-service and subscription-service across compose and CI; normalized their configs.
- Added app.config.js for Expo environment-based API selection; updated root scripts.
- Added Checkstyle/SpotBugs/Jacoco gates in server/pom.xml.
- Healthchecks and startup sequencing added to docker-compose.yml.

See docs/PROJECT_STRUCTURE.md for the intended structure and ensure future files follow it.

