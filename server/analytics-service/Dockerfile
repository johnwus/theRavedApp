# Multi-stage build for analytics-service with monorepo parent
FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /build
# Copy entire server monorepo to satisfy aggregator modules
COPY server server

# Build only this module and its deps
RUN mvn -f server/pom.xml -pl analytics-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="TheRavedApp Team"
LABEL description="analytics-service for TheRavedApp"

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app
COPY --from=build /build/server/analytics-service/target/*.jar app.jar

RUN mkdir -p logs && chown -R appuser:appgroup /app
USER appuser

EXPOSE 8087

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
