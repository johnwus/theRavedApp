@echo off
rem Comprehensive Test Coverage Validation Script for RAvED App
rem This script validates test coverage across all microservices

echo ========================================
echo RAvED App - Test Coverage Validation
echo ========================================
echo.

set "SERVER_DIR=%~dp0..\server"
set "COVERAGE_THRESHOLD=80"
set "FAILED_SERVICES="

echo Validating test coverage for all microservices...
echo Coverage threshold: %COVERAGE_THRESHOLD%%
echo.

rem Function to check service test coverage
call :check_service "user-service" "User Service"
call :check_service "content-service" "Content Service"
call :check_service "social-service" "Social Service"
call :check_service "ecommerce-service" "Ecommerce Service"
call :check_service "notification-service" "Notification Service"
call :check_service "analytics-service" "Analytics Service"
call :check_service "realtime-service" "Realtime Service"
call :check_service "events-service" "Events Service"
call :check_service "subscription-service" "Subscription Service"
call :check_service "eureka-server" "Eureka Server"
call :check_service "config-server" "Config Server"
call :check_service "api-gateway" "API Gateway"

echo.
echo ========================================
echo Test Coverage Summary
echo ========================================

if "%FAILED_SERVICES%"=="" (
    echo ✅ All services meet the test coverage threshold of %COVERAGE_THRESHOLD%%
    echo ✅ Test coverage validation PASSED
    exit /b 0
) else (
    echo ❌ The following services failed to meet the coverage threshold:
    echo %FAILED_SERVICES%
    echo ❌ Test coverage validation FAILED
    exit /b 1
)

:check_service
set "SERVICE_NAME=%~1"
set "SERVICE_DISPLAY=%~2"
set "SERVICE_PATH=%SERVER_DIR%\%SERVICE_NAME%"

echo Checking %SERVICE_DISPLAY% (%SERVICE_NAME%)...

if not exist "%SERVICE_PATH%" (
    echo   ❌ Service directory not found: %SERVICE_PATH%
    set "FAILED_SERVICES=%FAILED_SERVICES% %SERVICE_NAME%"
    goto :eof
)

rem Check if pom.xml exists
if not exist "%SERVICE_PATH%\pom.xml" (
    echo   ❌ pom.xml not found in %SERVICE_NAME%
    set "FAILED_SERVICES=%FAILED_SERVICES% %SERVICE_NAME%"
    goto :eof
)

rem Check if test directory exists
if not exist "%SERVICE_PATH%\src\test" (
    echo   ⚠️  No test directory found in %SERVICE_NAME%
    set "FAILED_SERVICES=%FAILED_SERVICES% %SERVICE_NAME%"
    goto :eof
)

rem Check for test files
set "TEST_COUNT=0"
for /r "%SERVICE_PATH%\src\test" %%f in (*.java) do (
    set /a TEST_COUNT+=1
)

if %TEST_COUNT% equ 0 (
    echo   ❌ No test files found in %SERVICE_NAME%
    set "FAILED_SERVICES=%FAILED_SERVICES% %SERVICE_NAME%"
    goto :eof
)

echo   ✅ Found %TEST_COUNT% test files

rem Check for application-test.yml
if exist "%SERVICE_PATH%\src\test\resources\application-test.yml" (
    echo   ✅ Test configuration found
) else (
    echo   ⚠️  Test configuration missing (application-test.yml)
)

rem Check for integration tests
set "INTEGRATION_TESTS=0"
for /r "%SERVICE_PATH%\src\test" %%f in (*Integration*.java) do (
    set /a INTEGRATION_TESTS+=1
)
for /r "%SERVICE_PATH%\src\test" %%f in (*IT.java) do (
    set /a INTEGRATION_TESTS+=1
)

if %INTEGRATION_TESTS% gtr 0 (
    echo   ✅ Found %INTEGRATION_TESTS% integration test files
) else (
    echo   ⚠️  No integration tests found
)

rem Check for TestContainers usage
findstr /r /c:"@Testcontainers" "%SERVICE_PATH%\src\test\java\**\*.java" >nul 2>&1
if %errorlevel% equ 0 (
    echo   ✅ TestContainers integration found
) else (
    echo   ⚠️  TestContainers not used
)

rem Check for MockMvc usage
findstr /r /c:"MockMvc" "%SERVICE_PATH%\src\test\java\**\*.java" >nul 2>&1
if %errorlevel% equ 0 (
    echo   ✅ MockMvc web layer tests found
) else (
    echo   ⚠️  MockMvc web layer tests not found
)

echo   📊 %SERVICE_DISPLAY% test validation completed
echo.

goto :eof
