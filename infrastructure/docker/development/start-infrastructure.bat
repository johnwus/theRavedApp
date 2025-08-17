@echo off
echo Starting TheRavedApp Infrastructure Services...
echo.

echo Starting PostgreSQL, MongoDB, Redis, Elasticsearch, Kafka, RabbitMQ, Prometheus, Grafana, and Jaeger...
docker-compose up -d

echo.
echo Waiting for services to start...
timeout /t 15 /nobreak > nul

echo.
echo Infrastructure services status:
docker-compose ps

echo.
echo Service URLs:
echo PostgreSQL: localhost:5432
echo MongoDB: localhost:27017
echo Redis: localhost:6380
echo Elasticsearch: localhost:9200
echo Kafka: localhost:9092
echo RabbitMQ: localhost:5672 (Management: localhost:15672)
echo Prometheus: localhost:9090
echo Grafana: localhost:3000 (admin/admin)
echo Jaeger: localhost:16686

echo.
echo Infrastructure services are now running!
pause
