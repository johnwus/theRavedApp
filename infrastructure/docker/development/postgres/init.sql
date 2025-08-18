-- PostgreSQL Database Initialization Script for RAvED App
-- This script creates all the databases needed for the microservices

-- The main user is already created by Docker environment variables
-- Just ensure the password is set correctly
ALTER USER raved_admin WITH PASSWORD 'theRAVEDapp#123';
ALTER USER raved_admin CREATEDB CREATEROLE;

-- Create databases ONLY for PostgreSQL services (polyglot architecture)
-- MongoDB services (notification, social, content, analytics) do NOT need PostgreSQL databases

-- PostgreSQL Services (errors for existing databases will be ignored):
CREATE DATABASE raved_user_db;        -- User service (authentication, profiles)
CREATE DATABASE raved_ecommerce_db;   -- Ecommerce service (products, orders, payments)
CREATE DATABASE raved_realtime_db;    -- Realtime service (chat, websockets)

-- Note: raved_db may already exist from Docker environment variables

-- Grant privileges to the admin user for PostgreSQL databases
GRANT ALL PRIVILEGES ON DATABASE raved_user_db TO raved_admin;
GRANT ALL PRIVILEGES ON DATABASE raved_ecommerce_db TO raved_admin;
GRANT ALL PRIVILEGES ON DATABASE raved_realtime_db TO raved_admin;
GRANT ALL PRIVILEGES ON DATABASE raved_db TO raved_admin;

-- Ensure the admin user can connect from external hosts
GRANT CONNECT ON DATABASE postgres TO raved_admin;
GRANT CONNECT ON DATABASE raved_db TO raved_admin;

-- Connect to each PostgreSQL database and create extensions
\c raved_user_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

\c raved_ecommerce_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

\c raved_realtime_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

\c raved_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- Log completion
\echo 'PostgreSQL databases created successfully for polyglot architecture!'
\echo 'PostgreSQL services: user, ecommerce, realtime'
\echo 'MongoDB services: notification, social, content, analytics'
 