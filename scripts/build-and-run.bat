@echo off
echo ========================================
echo RAvED Platform - Build and Run Script
echo ========================================

echo.
echo [1/5] Checking Java...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java not found. Please install Java 17+
    pause
    exit /b 1
)

echo.
echo [2/5] Checking Maven...
mvn --version
if %errorlevel% neq 0 (
    echo ERROR: Maven not found. Please install Maven
    pause
    exit /b 1
)

echo.
echo [3/5] Building parent project...
cd server
mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo ERROR: Build failed
    pause
    exit /b 1
)
cd ..

echo.
echo [4/5] Starting Eureka Server...
cd server\eureka-server
start "Eureka Server" mvn spring-boot:run
timeout /t 10
cd ..\..

echo.
echo [5/5] Starting other services...

echo Starting Config Server...
cd server\config-server
start "Config Server" mvn spring-boot:run
timeout /t 5
cd ..\..

echo Starting API Gateway...
cd server\api-gateway
start "API Gateway" mvn spring-boot:run
timeout /t 5
cd ..\..

echo Starting User Service...
cd server\user-service
start "User Service" mvn spring-boot:run
timeout /t 5
cd ..\..

echo Starting Content Service...
cd server\content-service
start "Content Service" mvn spring-boot:run
timeout /t 5
cd ..\..

echo Starting Social Service...
cd server\social-service
start "Social Service" mvn spring-boot:run
timeout /t 5
cd ..\..

echo Starting Realtime Service...
cd server\realtime-service
start "Realtime Service" mvn spring-boot:run
timeout /t 5
cd ..\..

echo Starting Ecommerce Service...
cd server\ecommerce-service
start "Ecommerce Service" mvn spring-boot:run
timeout /t 5
cd ..\..

echo Starting Notification Service...
cd server\notification-service
start "Notification Service" mvn spring-boot:run
timeout /t 5
cd ..\..

echo Starting Analytics Service...
cd server\analytics-service
start "Analytics Service" mvn spring-boot:run
timeout /t 5
cd ..\..

echo.
echo ========================================
echo All services are starting up...
echo Please wait 60 seconds for full startup
echo ========================================
echo.
echo Service URLs:
echo - Eureka Dashboard: http://localhost:8761
echo - API Gateway: http://localhost:8080
echo - User Service: http://localhost:8081
echo - Content Service: http://localhost:8082
echo - Social Service: http://localhost:8083
echo - Realtime Service: http://localhost:8084
echo - Ecommerce Service: http://localhost:8085
echo - Notification Service: http://localhost:8086
echo - Analytics Service: http://localhost:8087
echo.
echo Test commands:
echo curl http://localhost:8080/actuator/health
echo curl http://localhost:8761
echo.
echo To stop all services: taskkill /f /im java.exe
echo.
pause
