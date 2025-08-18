#!/bin/bash

echo "🎯 THERAVEDAPP COMPLETE ENVIRONMENT SETUP"
echo "========================================="
echo

echo "This script will:"
echo "1. 🧹 Clean up any existing containers"
echo "2. 🏗️  Start infrastructure services"
echo "3. 🚀 Start microservices"
echo "4. 🔧 Setup database GUI tools"
echo

read -p "Continue with complete setup? (y/N): " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Setup cancelled."
    exit 1
fi

echo
echo "🧹 STEP 1: CLEANING UP EXISTING CONTAINERS"
echo "=========================================="

# Change to docker development directory
cd "$(dirname "$0")/../docker/development"

echo "Stopping existing containers..."
docker-compose down --remove-orphans

echo "Removing unused volumes (optional cleanup)..."
docker volume prune -f

echo "✅ Cleanup complete"
echo

echo "🏗️  STEP 2: STARTING INFRASTRUCTURE"
echo "=================================="

# Start infrastructure
../scripts/start-infrastructure.sh

echo
echo "⏳ Waiting for infrastructure to stabilize..."
sleep 60

echo
echo "🚀 STEP 3: STARTING MICROSERVICES"
echo "================================"

# Start microservices
../scripts/start-microservices.sh

echo
echo "🔧 STEP 4: DATABASE GUI SETUP INSTRUCTIONS"
echo "=========================================="
echo
echo "📊 pgAdmin (PostgreSQL GUI) - READY!"
echo "===================================="
echo "✅ Already running at: http://localhost:5050"
echo "✅ Login: admin@raved.com / admin123"
echo
echo "To add PostgreSQL server in pgAdmin:"
echo "1. Open http://localhost:5050"
echo "2. Login with admin@raved.com / admin123"
echo "3. Right-click 'Servers' → Create → Server"
echo "4. General tab: Name = 'RAvED PostgreSQL'"
echo "5. Connection tab:"
echo "   - Host: postgres (or use container IP)"
echo "   - Port: 5432"
echo "   - Database: raved_db"
echo "   - Username: raved_admin"
echo "   - Password: theRAVEDapp#123"
echo "6. Click Save"
echo
echo "🍃 MongoDB Compass Setup"
echo "======================="
echo "MongoDB Compass is not included in Docker setup."
echo "Please install MongoDB Compass separately:"
echo
echo "1. Download from: https://www.mongodb.com/products/compass"
echo "2. Install MongoDB Compass"
echo "3. Connect with URI: mongodb://raved_admin:theRAVEDapp#123@localhost:27017/?authSource=admin"
echo
echo "Alternative: Use MongoDB Express (web-based)"
echo "You can add mongo-express to docker-compose.yml if preferred."
echo

echo "📊 FINAL STATUS CHECK"
echo "===================="

echo
echo "Infrastructure Services:"
docker ps --format "table {{.Names}}\t{{.Status}}" | grep -E "(postgres|mongodb|redis|elasticsearch|kafka|rabbitmq|prometheus|grafana|jaeger|pgadmin)"

echo
echo "Microservices:"
docker ps --format "table {{.Names}}\t{{.Status}}" | grep -E "(eureka|config|gateway|user|ecommerce|realtime|events|subscription|content|social|notification|analytics)"

echo
echo "🎯 ACCESS SUMMARY"
echo "================"
echo
echo "🌐 Web Interfaces:"
echo "  pgAdmin: http://localhost:5050 (admin@raved.com / admin123)"
echo "  Eureka: http://localhost:8761"
echo "  Grafana: http://localhost:3000 (admin / admin)"
echo "  RabbitMQ: http://localhost:15672 (raved / raved123)"
echo "  Jaeger: http://localhost:16686"
echo
echo "🗄️  Database Connections:"
echo "  PostgreSQL: localhost:5432 (raved_admin / theRAVEDapp#123)"
echo "  MongoDB: localhost:27017 (raved_admin / theRAVEDapp#123)"
echo
echo "🚀 API Gateway: http://localhost:8080"
echo
echo "✅ COMPLETE SETUP FINISHED!"
echo "=========================="
echo "Your TheRavedApp development environment is ready!"
echo
echo "Next steps:"
echo "1. Open pgAdmin and connect to PostgreSQL"
echo "2. Install and connect MongoDB Compass"
echo "3. Check Eureka dashboard for service registration"
echo "4. Start developing your application!"
echo
echo "For troubleshooting, check the documentation in docs/ directory."
