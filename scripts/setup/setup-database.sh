#!/bin/bash

# RAvED Database Setup Script
# This script sets up all databases for the microservices architecture

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
POSTGRES_HOST=${POSTGRES_HOST:-localhost}
POSTGRES_PORT=${POSTGRES_PORT:-5432}
POSTGRES_USER=${POSTGRES_USER:-raved_user}
POSTGRES_PASSWORD=${POSTGRES_PASSWORD:-raved_password}
POSTGRES_DB=${POSTGRES_DB:-raved_db}

# Database names
DATABASES=(
    "raved_user_db"
    "raved_content_db"
    "raved_social_db"
    "raved_realtime_db"
    "raved_ecommerce_db"
    "raved_notification_db"
    "raved_analytics_db"
)

# Service directories
SERVICES=(
    "user-service"
    "content-service"
    "social-service"
    "realtime-service"
    "ecommerce-service"
    "notification-service"
    "analytics-service"
)

echo -e "${BLUE}🗄️  Setting up RAvED databases...${NC}"

# Function to check if PostgreSQL is running
check_postgres() {
    echo -e "${YELLOW}⏳ Checking PostgreSQL connection...${NC}"

    for i in {1..30}; do
        if PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d postgres -c '\q' 2>/dev/null; then
            echo -e "${GREEN}✅ PostgreSQL is running${NC}"
            return 0
        fi
        echo -e "${YELLOW}⏳ Waiting for PostgreSQL... (attempt $i/30)${NC}"
        sleep 2
    done

    echo -e "${RED}❌ Failed to connect to PostgreSQL${NC}"
    exit 1
}

# Function to create databases
create_databases() {
    echo -e "${YELLOW}🏗️  Creating databases...${NC}"

    for db in "${DATABASES[@]}"; do
        echo -e "${BLUE}Creating database: $db${NC}"

        # Check if database exists
        if PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d postgres -lqt | cut -d \| -f 1 | grep -qw $db; then
            echo -e "${YELLOW}⚠️  Database $db already exists${NC}"
        else
            # Create database
            PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d postgres -c "CREATE DATABASE $db;"
            echo -e "${GREEN}✅ Database $db created${NC}"
        fi

        # Create extensions
        PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $db -c "CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";"
        PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $db -c "CREATE EXTENSION IF NOT EXISTS \"pg_trgm\";"
    done
}

# Function to run migrations
run_migrations() {
    echo -e "${YELLOW}🔄 Running database migrations...${NC}"

    # Change to server directory
    cd server

    for service in "${SERVICES[@]}"; do
        if [ -d "$service" ]; then
            echo -e "${BLUE}Running migrations for $service${NC}"
            cd $service

            # Run Flyway migrations using Maven
            if [ -f "pom.xml" ]; then
                mvn flyway:migrate -Dflyway.url=jdbc:postgresql://$POSTGRES_HOST:$POSTGRES_PORT/raved_${service//-/_}_db -Dflyway.user=$POSTGRES_USER -Dflyway.password=$POSTGRES_PASSWORD
                echo -e "${GREEN}✅ Migrations completed for $service${NC}"
            else
                echo -e "${YELLOW}⚠️  No pom.xml found for $service${NC}"
            fi

            cd ..
        else
            echo -e "${RED}❌ Service directory $service not found${NC}"
        fi
    done

    cd ..
}

# Function to verify database setup
verify_setup() {
    echo -e "${YELLOW}🔍 Verifying database setup...${NC}"

    for db in "${DATABASES[@]}"; do
        # Check if database exists and has tables
        table_count=$(PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $db -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public';" 2>/dev/null | xargs)

        if [ "$table_count" -gt 0 ]; then
            echo -e "${GREEN}✅ Database $db: $table_count tables${NC}"
        else
            echo -e "${YELLOW}⚠️  Database $db: No tables found${NC}"
        fi
    done
}

# Main execution
main() {
    echo -e "${BLUE}🚀 Starting RAvED Database Setup${NC}"
    echo -e "${BLUE}================================${NC}"

    check_postgres
    create_databases
    run_migrations
    verify_setup

    echo -e "${GREEN}🎉 Database setup completed successfully!${NC}"
    echo -e "${BLUE}================================${NC}"
    echo -e "${GREEN}All databases are ready for the RAvED microservices${NC}"
}

# Run main function
main "$@"