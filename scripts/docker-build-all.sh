#!/bin/bash

# Docker-based Build Script for All Microservices
# This script uses Docker to build all services without requiring local Java/Maven installation

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Configuration
PROJECT_ROOT=$(pwd)
SERVER_DIR="$PROJECT_ROOT/server"

echo -e "${BLUE}🐳 Docker-based Build for All Microservices${NC}"
echo -e "${BLUE}===========================================${NC}"

# Function to create multi-stage Dockerfile for services
create_service_dockerfile() {
    local service=$1
    local service_dir="$SERVER_DIR/$service"
    
    echo -e "${YELLOW}🐳 Creating Dockerfile for $service${NC}"
    
    cat > "$service_dir/Dockerfile" << 'EOF'
# Multi-stage build
FROM maven:3.9-openjdk-17-slim AS builder

WORKDIR /app

# Copy parent POM first
COPY ../pom.xml ./pom.xml

# Copy shared modules
COPY ../shared ./shared

# Copy service-specific files
COPY pom.xml ./service/pom.xml
COPY src ./service/src

# Build the service
WORKDIR /app/service
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR
COPY --from=builder /app/service/target/*.jar app.jar

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
EOF
}

# Function to create application.yml files
create_application_yml() {
    local service=$1
    local port=$2
    local db_name=$3
    local service_dir="$SERVER_DIR/$service/src/main/resources"
    
    if [ ! -f "$service_dir/application.yml" ]; then
        echo -e "${YELLOW}📝 Creating application.yml for $service${NC}"
        
        mkdir -p "$service_dir"
        
        cat > "$service_dir/application.yml" << EOF
server:
  port: $port

spring:
  application:
    name: $service
  datasource:
    url: jdbc:postgresql://postgres:5432/$db_name
    username: raved_user
    password: raved_password
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 20000
      idle-timeout: 300000
      max-lifetime: 1200000
  jpa:
    hibernate:
      ddl-auto: validate
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: false
        jdbc:
          batch_size: 30
        order_inserts: true
        order_updates: true
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true
  redis:
    host: redis
    port: 6379
    timeout: 2000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0

eureka:
  client:
    service-url:
      defaultZone: http://eureka-server:8761/eureka/
    register-with-eureka: true
    fetch-registry: true
  instance:
    prefer-ip-address: true
    lease-renewal-interval-in-seconds: 30
    lease-expiration-duration-in-seconds: 90

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
  metrics:
    export:
      prometheus:
        enabled: true

logging:
  level:
    com.raved: INFO
    org.springframework: WARN
    org.hibernate: WARN
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
EOF
    fi
}

# Services configuration
declare -A SERVICES=(
    ["eureka-server"]="8761:raved_eureka_db"
    ["config-server"]="8888:raved_config_db"
    ["api-gateway"]="8080:raved_gateway_db"
    ["user-service"]="8081:raved_user_db"
    ["content-service"]="8082:raved_content_db"
    ["social-service"]="8083:raved_social_db"
    ["realtime-service"]="8084:raved_realtime_db"
    ["ecommerce-service"]="8085:raved_ecommerce_db"
    ["notification-service"]="8086:raved_notification_db"
    ["analytics-service"]="8087:raved_analytics_db"
)

echo -e "${BLUE}📋 Preparing services for Docker build...${NC}"

# Create application.yml and Dockerfile for each service
for service in "${!SERVICES[@]}"; do
    IFS=':' read -r port db_name <<< "${SERVICES[$service]}"
    create_application_yml "$service" "$port" "$db_name"
    create_service_dockerfile "$service"
done

echo -e "${BLUE}🐳 Building Docker images...${NC}"

# Build Docker images for each service
for service in "${!SERVICES[@]}"; do
    echo -e "${YELLOW}🐳 Building Docker image for $service...${NC}"
    
    cd "$SERVER_DIR"
    
    if docker build -t "raved/$service:latest" -f "$service/Dockerfile" . > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Docker image built for $service${NC}"
    else
        echo -e "${RED}❌ Docker image build failed for $service${NC}"
        echo -e "${YELLOW}⚠️  Continuing with other services...${NC}"
    fi
    
    cd "$PROJECT_ROOT"
done

echo -e "${BLUE}📊 Build Summary${NC}"
echo -e "${BLUE}===============${NC}"

# Check which Docker images were created
for service in "${!SERVICES[@]}"; do
    if docker images | grep -q "raved/$service"; then
        echo -e "${GREEN}✅ $service - Docker image ready${NC}"
    else
        echo -e "${RED}❌ $service - Docker image not found${NC}"
    fi
done

echo ""
echo -e "${GREEN}🎉 Docker build process completed!${NC}"
echo -e "${BLUE}Next steps:${NC}"
echo -e "  1. Run: ${YELLOW}docker-compose up -d postgres redis${NC} to start infrastructure"
echo -e "  2. Run: ${YELLOW}./scripts/setup-hybrid-databases.sh${NC} to setup databases"
echo -e "  3. Run: ${YELLOW}docker-compose up${NC} to start all services"
echo -e "  4. Check health: ${YELLOW}curl http://localhost:8080/actuator/health${NC}"
