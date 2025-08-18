# Multi-stage build for realtime-service with monorepo parent
FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /build
COPY server server

RUN mvn -f server/pom.xml -pl realtime-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="TheRavedApp Team"
LABEL description="realtime-service for TheRavedApp"

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app
COPY --from=build /build/server/realtime-service/target/*.jar app.jar

RUN mkdir -p logs && chown -R appuser:appgroup /app
USER appuser

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
