# Multi-stage build for social-service with monorepo parent
FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /build
COPY server server

RUN mvn -f server/pom.xml -pl social-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="TheRavedApp Team"
LABEL description="social-service for TheRavedApp"

RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app
COPY --from=build /build/server/social-service/target/*.jar app.jar

RUN mkdir -p logs && chown -R appuser:appgroup /app
USER appuser

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
