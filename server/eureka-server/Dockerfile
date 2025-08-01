# Multi-stage build for Eureka Server
FROM maven:3.9.11-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy parent pom and shared modules first for better caching
COPY pom.xml .
COPY shared/ shared/

# Copy eureka-server specific files
COPY eureka-server/pom.xml eureka-server/
COPY eureka-server/src eureka-server/src

# Build the application
RUN mvn clean package -pl eureka-server -am -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Add metadata
LABEL maintainer="TheRavedApp Team"
LABEL description="Eureka Discovery Server for TheRavedApp"
LABEL version="1.0"

# Create app user
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy the built jar
COPY --from=build /app/eureka-server/target/*.jar app.jar

# Create logs directory
RUN mkdir -p logs && chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8761/actuator/health || exit 1

# Expose port
EXPOSE 8761

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
