# Multi-stage build for Eureka Server with monorepo parent
FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /build
COPY server server

RUN mvn -f server/pom.xml -pl eureka-server -am clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="TheRavedApp Team"
LABEL description="Eureka Discovery Server for TheRavedApp"
LABEL version="1.0"

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app
COPY --from=build /build/server/eureka-server/target/*.jar app.jar

RUN mkdir -p logs && chown -R appuser:appgroup /app

USER appuser

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8761/actuator/health || exit 1

EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
