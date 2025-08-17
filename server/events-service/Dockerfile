# Multi-stage build for events-service with monorepo parent
FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /build
COPY server server

RUN mvn -f server/pom.xml -pl events-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="TheRavedApp Team"
LABEL description="events-service for TheRavedApp - Event management and organization"

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app
COPY --from=build /build/server/events-service/target/*.jar app.jar

RUN mkdir -p logs && chown -R appuser:appgroup /app
USER appuser

EXPOSE 8088

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8088/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
