-- PostgreSQL Database Initialization Script for RAvED App
-- This script creates all the databases needed for the microservices

-- Create databases for each microservice
CREATE DATABASE raved_user_db;
CREATE DATABASE raved_content_db;
CREATE DATABASE raved_social_db;
CREATE DATABASE raved_realtime_db;
CREATE DATABASE raved_ecommerce_db;
CREATE DATABASE raved_notification_db;
CREATE DATABASE raved_analytics_db;

-- Grant privileges to the raved_user for all databases
GRANT ALL PRIVILEGES ON DATABASE raved_user_db TO raved_user;
GRANT ALL PRIVILEGES ON DATABASE raved_content_db TO raved_user;
GRANT ALL PRIVILEGES ON DATABASE raved_social_db TO raved_user;
GRANT ALL PRIVILEGES ON DATABASE raved_realtime_db TO raved_user;
GRANT ALL PRIVILEGES ON DATABASE raved_ecommerce_db TO raved_user;
GRANT ALL PRIVILEGES ON DATABASE raved_notification_db TO raved_user;
GRANT ALL PRIVILEGES ON DATABASE raved_analytics_db TO raved_user;

-- Connect to each database and create extensions
\c raved_user_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

\c raved_content_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

\c raved_social_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

\c raved_realtime_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

\c raved_ecommerce_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

\c raved_notification_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

\c raved_analytics_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- Log completion
\echo 'All RAvED databases created successfully!'
