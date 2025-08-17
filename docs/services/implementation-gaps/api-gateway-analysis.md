# api-gateway — Implementation Analysis

Current state
- Main app: com.raved.gateway.ApiGatewayApplication
- Config: GatewayConfig defines routes; CorsConfig; SecurityConfig (JWT resource server) present
- application.yml: discovery defaults now point to eureka-server; resource server jwk-set-uri env-driven; resilience/metrics configured

Gaps
- Rate limiting commented (Redis not configured); CORS origins need per-env values
- No contract tests on routing rules; no integration tests on auth filters
- No clear health/readiness split endpoints

Recommendations
- Add route tests using WebTestClient; add JWT auth integration tests
- Decide on RequestRateLimiter filter and configure Redis (optional)
- Confirm /actuator/health readiness/liveness once enabled (tracked in probes TODO)
- Document required envs for JWT (secret or jwk-set)

