@echo off
echo Starting TheRavedApp Microservices...
echo.

echo Starting services in order:
echo 1. Eureka Server (Service Discovery)
echo 2. Config Server
echo 3. API Gateway
echo 4. Business Services (User, Content, Social, etc.)
echo.

cd /d C:\theRavedApp\server

echo Starting Eureka Server...
start "Eureka Server" cmd /k "mvn spring-boot:run -pl eureka-server"

echo Waiting for Eureka Server to start...
timeout /t 15 /nobreak > nul

echo Starting Config Server...
start "Config Server" cmd /k "mvn spring-boot:run -pl config-server"

echo Waiting for Config Server to start...
timeout /t 15 /nobreak > nul

echo Starting API Gateway...
start "API Gateway" cmd /k "mvn spring-boot:run -pl api-gateway"

echo Waiting for API Gateway to start...
timeout /t 15 /nobreak > nul

echo Starting Business Services...
start "User Service" cmd /k "mvn spring-boot:run -pl user-service"
start "Content Service" cmd /k "mvn spring-boot:run -pl content-service"
start "Social Service" cmd /k "mvn spring-boot:run -pl social-service"
start "Realtime Service" cmd /k "mvn spring-boot:run -pl realtime-service"
start "Ecommerce Service" cmd /k "mvn spring-boot:run -pl ecommerce-service"
start "Notification Service" cmd /k "mvn spring-boot:run -pl notification-service"
start "Analytics Service" cmd /k "mvn spring-boot:run -pl analytics-service"
start "Events Service" cmd /k "mvn spring-boot:run -pl events-service"
start "Subscription Service" cmd /k "mvn spring-boot:run -pl subscription-service"

echo.
echo All microservices are starting...
echo Check the individual command windows for startup progress.
echo.
echo Service URLs:
echo Eureka Server: http://localhost:8761
echo Config Server: http://localhost:8888
echo API Gateway: http://localhost:8080
echo User Service: http://localhost:8081
echo Content Service: http://localhost:8082
echo Social Service: http://localhost:8083
echo Realtime Service: http://localhost:8084
echo Ecommerce Service: http://localhost:8085
echo Notification Service: http://localhost:8086
echo Analytics Service: http://localhost:8087
echo Events Service: http://localhost:8088
echo Subscription Service: http://localhost:8089

pause





