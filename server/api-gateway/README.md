# Raved API Gateway

Clean, minimal Spring Cloud Gateway aligned with project conventions.

## Structure

```
src/main/java/com/raved/gateway/
├── ApiGatewayApplication.java
├── config/
│   ├── SecurityConfig.java
│   ├── GatewayRoutesConfig.java
│   ├── CorsConfig.java
│   ├── ActuatorConfig.java
│   └── OpenApiConfig.java
├── filter/
│   └── RequestLoggingFilter.java
└── exception/
    └── GlobalErrorHandler.java

src/main/resources/
├── application.yml
└── bootstrap.yml
```

## Principles
- Modular: routing, security, cors, filters, and errors are separated
- Simple: YAML configuration + concise beans
- Shared: reuses `raved-common` and `raved-security`

## Adding Routes
- Add a new `.route("service-id", r -> r.path("/api/.../**").uri("lb://service-id"))` in `GatewayRoutesConfig`
- Or source from Config Server by externalizing route definitions

## Security
- Resource server (JWT) via `spring.security.oauth2.resourceserver.jwt.jwk-set-uri`
- Public paths: `/actuator/**`, `/auth/**`