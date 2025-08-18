#!/bin/bash

echo "🚀 STARTING THERAVEDAPP MICROSERVICES"
echo "===================================="
echo

# Change to docker development directory
cd "$(dirname "$0")/../docker/development"

echo "📋 MICROSERVICES TO START:"
echo "=========================="
echo "🏗️  Infrastructure Services:"
echo "   ✅ Eureka Server (Service Discovery) - Port 8761"
echo "   ✅ Config Server (Configuration) - Port 8888"
echo "   ✅ API Gateway (Routing) - Port 8080"
echo
echo "🗄️  PostgreSQL Services:"
echo "   ✅ User Service - Port 8081"
echo "   ✅ Ecommerce Service - Port 8085"
echo "   ✅ Realtime Service - Port 8084"
echo "   ✅ Events Service - Port 8088"
echo "   ✅ Subscription Service - Port 8089"
echo
echo "🍃 MongoDB Services:"
echo "   ✅ Content Service - Port 8082"
echo "   ✅ Social Service - Port 8083"
echo "   ✅ Notification Service - Port 8086"
echo "   ✅ Analytics Service - Port 8087"
echo

echo "🔍 CHECKING INFRASTRUCTURE DEPENDENCIES..."
echo "=========================================="

# Check if infrastructure is running
if ! docker ps --format "{{.Names}}" | grep -q "raved-postgres-dev"; then
    echo "❌ PostgreSQL not running. Please start infrastructure first:"
    echo "   ./start-infrastructure.sh"
    exit 1
fi

if ! docker ps --format "{{.Names}}" | grep -q "raved-mongodb-dev"; then
    echo "❌ MongoDB not running. Please start infrastructure first:"
    echo "   ./start-infrastructure.sh"
    exit 1
fi

echo "✅ Infrastructure dependencies are running"
echo

echo "🔧 STARTING MICROSERVICES..."
echo "============================"

# Start infrastructure services first
echo "Starting infrastructure services..."
docker-compose up -d eureka-server

echo "Waiting for Eureka Server to be ready..."
timeout 120 bash -c 'until curl -s http://localhost:8761/actuator/health | grep -q "UP"; do sleep 5; done' || echo "Eureka timeout (may still be starting)"

echo "Starting Config Server..."
docker-compose up -d config-server

echo "Waiting for Config Server to be ready..."
timeout 120 bash -c 'until curl -s http://localhost:8888/actuator/health | grep -q "UP"; do sleep 5; done' || echo "Config Server timeout (may still be starting)"

echo "Starting API Gateway..."
docker-compose up -d api-gateway

echo
echo "Starting PostgreSQL-based services..."
docker-compose up -d \
  user-service \
  ecommerce-service \
  realtime-service \
  events-service \
  subscription-service

echo
echo "Starting MongoDB-based services..."
docker-compose up -d \
  content-service \
  social-service \
  notification-service \
  analytics-service

echo
echo "⏳ WAITING FOR SERVICES TO INITIALIZE..."
echo "======================================="
echo "This may take 3-5 minutes for all microservices to be ready..."

# Wait a bit for services to start
sleep 30

echo
echo "📊 MICROSERVICES STATUS:"
echo "======================="
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep -E "(raved-|NAMES)"

echo
echo "🎯 SERVICE ENDPOINTS:"
echo "===================="
echo
echo "🏗️  Infrastructure:"
echo "   Eureka Server: http://localhost:8761"
echo "   Config Server: http://localhost:8888"
echo "   API Gateway: http://localhost:8080"
echo
echo "🗄️  PostgreSQL Services:"
echo "   User Service: http://localhost:8081"
echo "   Ecommerce Service: http://localhost:8085"
echo "   Realtime Service: http://localhost:8084"
echo "   Events Service: http://localhost:8088"
echo "   Subscription Service: http://localhost:8089"
echo
echo "🍃 MongoDB Services:"
echo "   Content Service: http://localhost:8082"
echo "   Social Service: http://localhost:8083"
echo "   Notification Service: http://localhost:8086"
echo "   Analytics Service: http://localhost:8087"
echo
echo "🔍 HEALTH CHECKS:"
echo "================"
echo "Check service health at: http://localhost:8761 (Eureka Dashboard)"
echo "API Gateway routes: http://localhost:8080/actuator/gateway/routes"
echo
echo "✅ MICROSERVICES STARTED!"
echo "========================"
echo "All microservices are starting up."
echo "Check Eureka Dashboard to see when all services are registered."
echo "Full startup may take 5-10 minutes depending on your system."
