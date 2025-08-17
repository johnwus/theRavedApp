@echo off
echo Setting up test data for TheRavedApp...
echo.

echo 1. Setting up PostgreSQL test data...
echo Please run the following command manually:
echo psql -h localhost -U raved_user -d raved_db -f setup-test-data.sql
echo.

echo 2. Setting up MongoDB test data...
echo Please run the following command manually:
echo mongo localhost:27017/raved_content setup-mongodb-test-data.js
echo.

echo 3. Setting up Elasticsearch test data...
echo Elasticsearch will be populated automatically when services start
echo.

echo 4. Setting up Redis test data...
echo Redis will be populated automatically when services start
echo.

echo Test data setup instructions completed!
echo.
echo Next steps:
echo 1. Start Docker Desktop
echo 2. Run start-infrastructure.bat
echo 3. Run the database setup commands above
echo 4. Run start-microservices.bat
echo.
pause

