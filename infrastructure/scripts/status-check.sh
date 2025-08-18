#!/bin/bash

echo "🎯 THERAVEDAPP INFRASTRUCTURE STATUS"
echo "===================================="
echo

echo "📊 CONTAINER STATUS"
echo "=================="
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | head -15

echo
echo "🔍 DATABASE CONTAINER DETAILS"
echo "============================="
echo "PostgreSQL Container:"
echo "  - IP: $(docker inspect raved-postgres-dev --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' 2>/dev/null || echo 'Not running')"
echo "  - Status: $(docker ps --filter 'name=raved-postgres-dev' --format '{{.Status}}' 2>/dev/null || echo 'Not running')"

echo
echo "MongoDB Container:"
echo "  - IP: $(docker inspect raved-mongodb-dev --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' 2>/dev/null || echo 'Not running')"
echo "  - Status: $(docker ps --filter 'name=raved-mongodb-dev' --format '{{.Status}}' 2>/dev/null || echo 'Not running')"

echo
echo "pgAdmin Container:"
echo "  - IP: $(docker inspect raved-pgadmin-dev --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' 2>/dev/null || echo 'Not running')"
echo "  - Status: $(docker ps --filter 'name=raved-pgadmin-dev' --format '{{.Status}}' 2>/dev/null || echo 'Not running')"

echo
echo "🔧 CONNECTION TESTS"
echo "=================="
echo "Testing database connections..."

echo -n "PostgreSQL: "
docker exec raved-postgres-dev psql -U raved_admin -d raved_db -c 'SELECT current_user;' >/dev/null 2>&1 && echo "✅ Connected" || echo "❌ Failed"

echo -n "MongoDB: "
docker exec raved-mongodb-dev mongosh --eval 'db.runCommand({ping: 1})' --quiet >/dev/null 2>&1 && echo "✅ Connected" || echo "❌ Failed"

echo -n "pgAdmin Web: "
curl -s http://localhost:5050 >/dev/null 2>&1 && echo "✅ Accessible" || echo "❌ Not accessible"

echo
echo "🌐 ACCESS INFORMATION"
echo "===================="
echo
echo "📊 Database Management:"
echo "  pgAdmin: http://localhost:5050"
echo "    - Email: admin@raved.com"
echo "    - Password: admin123"
echo
echo "🗄️  Direct Database Access:"
echo "  PostgreSQL: localhost:5432"
echo "    - Username: raved_admin"
echo "    - Password: theRAVEDapp#123"
echo "    - Container IP: $(docker inspect raved-postgres-dev --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' 2>/dev/null || echo 'N/A')"
echo
echo "  MongoDB: localhost:27017"
echo "    - Username: raved_admin"
echo "    - Password: theRAVEDapp#123"
echo "    - Container IP: $(docker inspect raved-mongodb-dev --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' 2>/dev/null || echo 'N/A')"
echo
echo "🔗 Other Services:"
echo "  Grafana: http://localhost:3000 (admin/admin)"
echo "  RabbitMQ: http://localhost:15672 (raved/raved123)"
echo "  Prometheus: http://localhost:9090"
echo "  Jaeger: http://localhost:16686"
echo "  Elasticsearch: http://localhost:9200"

echo
echo "📋 NEXT STEPS"
echo "============="
echo "1. Open pgAdmin: http://localhost:5050"
echo "2. Add PostgreSQL server with container IP: $(docker inspect raved-postgres-dev --format='{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' 2>/dev/null || echo 'Check container')"
echo "3. Install MongoDB Compass and connect to: mongodb://raved_admin:theRAVEDapp#123@localhost:27017/?authSource=admin"
echo "4. Start microservices: ./start-microservices.sh"
echo
echo "📚 Documentation:"
echo "  - Database GUI Setup: infrastructure/database-gui-setup.md"
echo "  - Troubleshooting: docs/troubleshooting/"
echo
echo "✅ Infrastructure is ready for development!"
