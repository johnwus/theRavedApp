#!/bin/bash

echo "🚀 STARTING THERAVEDAPP INFRASTRUCTURE"
echo "====================================="
echo

# Change to docker development directory
cd "$(dirname "$0")/../docker/development"

echo "📋 INFRASTRUCTURE COMPONENTS:"
echo "============================="
echo "✅ PostgreSQL Database (Port 5432)"
echo "✅ pgAdmin Web Interface (Port 5050)"
echo "✅ MongoDB Database (Port 27017)"
echo "✅ Redis Cache (Port 6380)"
echo "✅ Elasticsearch (Port 9200)"
echo "✅ Kafka + Zookeeper (Port 9092)"
echo "✅ RabbitMQ (Port 5672, Management: 15672)"
echo "✅ Prometheus (Port 9090)"
echo "✅ Grafana (Port 3000)"
echo "✅ Jaeger (Port 16686)"
echo

echo "🔧 STARTING INFRASTRUCTURE SERVICES..."
echo "======================================"

# Start only infrastructure services (not microservices)
docker-compose up -d \
  postgres \
  pgadmin \
  mongodb \
  redis \
  elasticsearch \
  zookeeper \
  kafka \
  rabbitmq \
  prometheus \
  grafana \
  jaeger

echo
echo "⏳ WAITING FOR SERVICES TO INITIALIZE..."
echo "======================================="
echo "This may take 2-3 minutes for all services to be ready..."

# Wait for critical services
echo "Waiting for PostgreSQL..."
timeout 120 bash -c 'until docker exec raved-postgres-dev pg_isready -U raved_admin -d raved_db; do sleep 2; done' || echo "PostgreSQL timeout (may still be starting)"

echo "Waiting for MongoDB..."
timeout 120 bash -c 'until docker exec raved-mongodb-dev mongosh --eval "db.adminCommand(\"ping\")" --quiet; do sleep 2; done' || echo "MongoDB timeout (may still be starting)"

echo "Waiting for pgAdmin..."
timeout 60 bash -c 'until curl -s http://localhost:5050 > /dev/null; do sleep 2; done' || echo "pgAdmin timeout (may still be starting)"

echo
echo "📊 INFRASTRUCTURE STATUS:"
echo "========================"
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep -E "(raved-|NAMES)"

echo
echo "🎯 ACCESS INFORMATION:"
echo "====================="
echo
echo "📊 DATABASES:"
echo "============"
echo "PostgreSQL:"
echo "  - Host: localhost:5432"
echo "  - Username: raved_admin"
echo "  - Password: theRAVEDapp#123"
echo "  - Databases: raved_user_db, raved_ecommerce_db, raved_realtime_db, raved_db"
echo
echo "MongoDB:"
echo "  - Host: localhost:27017"
echo "  - Username: raved_admin"
echo "  - Password: theRAVEDapp#123"
echo "  - Databases: raved_content, raved_social, raved_notifications, raved_analytics"
echo
echo "🌐 WEB INTERFACES:"
echo "=================="
echo "pgAdmin (PostgreSQL Management):"
echo "  - URL: http://localhost:5050"
echo "  - Email: admin@raved.com"
echo "  - Password: admin123"
echo
echo "RabbitMQ Management:"
echo "  - URL: http://localhost:15672"
echo "  - Username: raved"
echo "  - Password: raved123"
echo
echo "Grafana (Monitoring):"
echo "  - URL: http://localhost:3000"
echo "  - Username: admin"
echo "  - Password: admin"
echo
echo "Prometheus (Metrics):"
echo "  - URL: http://localhost:9090"
echo
echo "Jaeger (Tracing):"
echo "  - URL: http://localhost:16686"
echo
echo "Elasticsearch:"
echo "  - URL: http://localhost:9200"
echo
echo "🔧 NEXT STEPS:"
echo "============="
echo "1. Open pgAdmin at http://localhost:5050"
echo "2. Add PostgreSQL server with host: postgres (or container IP)"
echo "3. Install MongoDB Compass for MongoDB GUI"
echo "4. Start microservices with: ./start-microservices.sh"
echo
echo "✅ INFRASTRUCTURE READY!"
echo "======================="
echo "All infrastructure services are starting up."
echo "Please wait a few minutes for full initialization."
