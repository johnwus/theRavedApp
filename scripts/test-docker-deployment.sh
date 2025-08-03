#!/bin/bash

# Test Docker Deployment Script
# This script starts the infrastructure and tests the microservices

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

echo -e "${BLUE}🐳 Testing Docker Deployment${NC}"
echo -e "${BLUE}============================${NC}"

# Function to wait for service to be ready
wait_for_service() {
    local service_name=$1
    local port=$2
    local max_attempts=30
    local attempt=1
    
    echo -e "${YELLOW}⏳ Waiting for $service_name to be ready on port $port...${NC}"
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s -f "http://localhost:$port/actuator/health" > /dev/null 2>&1; then
            echo -e "${GREEN}✅ $service_name is ready!${NC}"
            return 0
        fi
        
        echo -e "${YELLOW}   Attempt $attempt/$max_attempts - $service_name not ready yet...${NC}"
        sleep 10
        ((attempt++))
    done
    
    echo -e "${RED}❌ $service_name failed to start within timeout${NC}"
    return 1
}

# Function to check service health
check_service_health() {
    local service_name=$1
    local port=$2
    
    echo -e "${BLUE}🏥 Checking $service_name health...${NC}"
    
    if curl -s "http://localhost:$port/actuator/health" | grep -q "UP"; then
        echo -e "${GREEN}✅ $service_name is healthy${NC}"
        return 0
    else
        echo -e "${RED}❌ $service_name is not healthy${NC}"
        return 1
    fi
}

# Step 1: Start infrastructure services
echo -e "${BLUE}🏗️ Starting infrastructure services...${NC}"
docker-compose up -d postgres redis rabbitmq

# Wait for infrastructure
echo -e "${YELLOW}⏳ Waiting for infrastructure to be ready...${NC}"
sleep 30

# Step 2: Setup databases
echo -e "${BLUE}🗄️ Setting up databases...${NC}"
if [ -f "./scripts/setup-hybrid-databases.sh" ]; then
    ./scripts/setup-hybrid-databases.sh
else
    echo -e "${YELLOW}⚠️  Database setup script not found, continuing...${NC}"
fi

# Step 3: Start discovery service first
echo -e "${BLUE}🔍 Starting Eureka Server...${NC}"
docker-compose up -d eureka-server

# Wait for Eureka
wait_for_service "Eureka Server" "8761"

# Step 4: Start config server
echo -e "${BLUE}⚙️ Starting Config Server...${NC}"
docker-compose up -d config-server

# Wait for Config Server
wait_for_service "Config Server" "8888"

# Step 5: Start API Gateway
echo -e "${BLUE}🚪 Starting API Gateway...${NC}"
docker-compose up -d api-gateway

# Wait for API Gateway
wait_for_service "API Gateway" "8080"

# Step 6: Start business services
echo -e "${BLUE}🚀 Starting business services...${NC}"
docker-compose up -d user-service content-service social-service

# Wait for business services
wait_for_service "User Service" "8081"
wait_for_service "Content Service" "8082"
wait_for_service "Social Service" "8083"

# Step 7: Start remaining services
echo -e "${BLUE}🔄 Starting remaining services...${NC}"
docker-compose up -d realtime-service ecommerce-service notification-service analytics-service

# Wait for remaining services
wait_for_service "Realtime Service" "8084"
wait_for_service "Ecommerce Service" "8085"
wait_for_service "Notification Service" "8086"
wait_for_service "Analytics Service" "8087"

# Step 8: Health checks
echo -e "${BLUE}🏥 Performing health checks...${NC}"

declare -A SERVICES=(
    ["Eureka Server"]="8761"
    ["Config Server"]="8888"
    ["API Gateway"]="8080"
    ["User Service"]="8081"
    ["Content Service"]="8082"
    ["Social Service"]="8083"
    ["Realtime Service"]="8084"
    ["Ecommerce Service"]="8085"
    ["Notification Service"]="8086"
    ["Analytics Service"]="8087"
)

healthy_services=0
total_services=${#SERVICES[@]}

for service in "${!SERVICES[@]}"; do
    if check_service_health "$service" "${SERVICES[$service]}"; then
        ((healthy_services++))
    fi
done

# Step 9: Test basic API endpoints
echo -e "${BLUE}🧪 Testing API endpoints...${NC}"

# Test API Gateway
echo -e "${YELLOW}🧪 Testing API Gateway...${NC}"
if curl -s -f "http://localhost:8080/actuator/health" > /dev/null; then
    echo -e "${GREEN}✅ API Gateway is accessible${NC}"
else
    echo -e "${RED}❌ API Gateway is not accessible${NC}"
fi

# Test User Service through Gateway
echo -e "${YELLOW}🧪 Testing User Service through Gateway...${NC}"
if curl -s -f "http://localhost:8080/api/users/health" > /dev/null 2>&1; then
    echo -e "${GREEN}✅ User Service is accessible through Gateway${NC}"
else
    echo -e "${YELLOW}⚠️  User Service endpoint may not be configured${NC}"
fi

# Step 10: Show service status
echo -e "${BLUE}📊 Deployment Summary${NC}"
echo -e "${BLUE}===================${NC}"

echo -e "Healthy Services: ${GREEN}$healthy_services${NC}/$total_services"

if [ $healthy_services -eq $total_services ]; then
    echo -e "${GREEN}🎉 All services are running successfully!${NC}"
else
    echo -e "${YELLOW}⚠️  Some services may need attention${NC}"
fi

# Show running containers
echo -e "${BLUE}🐳 Running Containers:${NC}"
docker-compose ps

echo -e "${BLUE}📋 Useful Commands:${NC}"
echo -e "  View logs: ${YELLOW}docker-compose logs -f [service-name]${NC}"
echo -e "  Stop all: ${YELLOW}docker-compose down${NC}"
echo -e "  Restart service: ${YELLOW}docker-compose restart [service-name]${NC}"
echo -e "  Check health: ${YELLOW}curl http://localhost:8080/actuator/health${NC}"

echo -e "${GREEN}✅ Docker deployment test completed!${NC}"
