# Probes and Healthchecks — TODO

Goal: Decide and standardize liveness/readiness endpoints for each service before enabling stricter probes in Kubernetes/Helm.

Current state
- K8s uses /actuator/health for both liveness and readiness as a safe default
- Spring Boot can expose specific readiness/liveness endpoints if enabled

Action items
- For each service, confirm if these endpoints are available:
  - /actuator/health/readiness
  - /actuator/health/liveness
- If not available, enable via Spring Actuator config, e.g.:
  - management.endpoint.health.probes.enabled=true
  - management.health.livenessstate.enabled=true
  - management.health.readinessstate.enabled=true
- Update service Helm values to use the dedicated endpoints

Services to verify
- api-gateway (8080)
- user-service (8081)
- content-service (8082)
- social-service (8083)
- realtime-service (8084)
- ecommerce-service (8085)
- notification-service (8086)
- analytics-service (8087)
- events-service (8088)
- subscription-service (8089)
- eureka-server (8761)
- config-server (8888)

Notes
- Keep /actuator/health until readiness/liveness are confirmed and enabled
- Ensure correct security settings if actuator endpoints require auth

