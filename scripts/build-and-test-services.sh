#!/bin/bash

# Build and Test All Microservices for Docker Deployment
# This script builds all services and prepares them for Docker testing

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

echo -e "${BLUE}🚀 Building and Testing All Microservices${NC}"
echo -e "${BLUE}==========================================${NC}"

# Function to build a service
build_service() {
    local service=$1
    local service_dir="$SERVER_DIR/$service"
    
    echo -e "${YELLOW}📦 Building $service...${NC}"
    
    if [ ! -d "$service_dir" ]; then
        echo -e "${RED}❌ Service directory not found: $service_dir${NC}"
        return 1
    fi
    
    cd "$service_dir"
    
    # Clean and compile
    if mvn clean compile -q; then
        echo -e "${GREEN}✅ $service compiled successfully${NC}"
    else
        echo -e "${RED}❌ $service compilation failed${NC}"
        return 1
    fi
    
    # Run tests
    if mvn test -q; then
        echo -e "${GREEN}✅ $service tests passed${NC}"
    else
        echo -e "${YELLOW}⚠️  $service tests failed (continuing...)${NC}"
    fi
    
    # Package
    if mvn package -DskipTests -q; then
        echo -e "${GREEN}✅ $service packaged successfully${NC}"
    else
        echo -e "${RED}❌ $service packaging failed${NC}"
        return 1
    fi
    
    cd "$PROJECT_ROOT"
    echo ""
}

# Function to create missing application.yml files
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
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true

eureka:
  client:
    service-url:
      defaultZone: http://eureka-server:8761/eureka/
  instance:
    prefer-ip-address: true

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always

logging:
  level:
    com.raved: INFO
    org.springframework: WARN
EOF
    fi
}

# Function to create Dockerfile for services
create_dockerfile() {
    local service=$1
    local service_dir="$SERVER_DIR/$service"
    
    if [ ! -f "$service_dir/Dockerfile" ]; then
        echo -e "${YELLOW}🐳 Creating Dockerfile for $service${NC}"
        
        cat > "$service_dir/Dockerfile" << EOF
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 808*

ENTRYPOINT ["java", "-jar", "app.jar"]
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

echo -e "${BLUE}📋 Preparing services for build...${NC}"

# Create application.yml and Dockerfile for each service
for service in "${!SERVICES[@]}"; do
    IFS=':' read -r port db_name <<< "${SERVICES[$service]}"
    create_application_yml "$service" "$port" "$db_name"
    create_dockerfile "$service"
done

echo -e "${BLUE}🔨 Building all services...${NC}"

# Build parent POM first
echo -e "${YELLOW}📦 Building parent POM...${NC}"
cd "$SERVER_DIR"
if mvn clean install -DskipTests -q; then
    echo -e "${GREEN}✅ Parent POM built successfully${NC}"
else
    echo -e "${RED}❌ Parent POM build failed${NC}"
    exit 1
fi

cd "$PROJECT_ROOT"

# Build each service
for service in "${!SERVICES[@]}"; do
    build_service "$service"
done

echo -e "${BLUE}🐳 Building Docker images...${NC}"

# Build Docker images
for service in "${!SERVICES[@]}"; do
    echo -e "${YELLOW}🐳 Building Docker image for $service...${NC}"
    
    cd "$SERVER_DIR/$service"
    
    if docker build -t "raved/$service:latest" . > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Docker image built for $service${NC}"
    else
        echo -e "${RED}❌ Docker image build failed for $service${NC}"
    fi
    
    cd "$PROJECT_ROOT"
done

echo -e "${BLUE}📊 Build Summary${NC}"
echo -e "${BLUE}===============${NC}"

# Check which services are ready
for service in "${!SERVICES[@]}"; do
    if [ -f "$SERVER_DIR/$service/target/*.jar" ] || ls "$SERVER_DIR/$service/target/"*.jar 1> /dev/null 2>&1; then
        echo -e "${GREEN}✅ $service - Ready for deployment${NC}"
    else
        echo -e "${RED}❌ $service - Build failed${NC}"
    fi
done

echo ""
echo -e "${GREEN}🎉 Build process completed!${NC}"
echo -e "${BLUE}Next steps:${NC}"
echo -e "  1. Run: ${YELLOW}docker-compose up -d${NC} to start infrastructure"
echo -e "  2. Run: ${YELLOW}docker-compose up${NC} to start all services"
echo -e "  3. Check health: ${YELLOW}curl http://localhost:8080/actuator/health${NC}"
