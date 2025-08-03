#!/bin/bash

# Database Migration Script for RAvED App
# This script runs Flyway migrations for all microservices

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

# Service configurations
declare -A SERVICE_DBS=(
    ["user-service"]="raved_user_db"
    ["content-service"]="raved_content_db"
    ["social-service"]="raved_social_db"
    ["realtime-service"]="raved_realtime_db"
    ["ecommerce-service"]="raved_ecommerce_db"
    ["notification-service"]="raved_notification_db"
    ["analytics-service"]="raved_analytics_db"
)

echo -e "${BLUE}🔄 Running database migrations for all services...${NC}"

# Function to run migrations for a service
run_service_migration() {
    local service=$1
    local database=${SERVICE_DBS[$service]}
    
    echo -e "${YELLOW}📦 Migrating $service -> $database${NC}"
    
    if [ ! -d "server/$service" ]; then
        echo -e "${RED}❌ Service directory server/$service not found${NC}"
        return 1
    fi
    
    cd "server/$service"
    
    # Check if migration directory exists
    if [ ! -d "src/main/resources/db/migration" ]; then
        echo -e "${YELLOW}⚠️  No migrations found for $service${NC}"
        cd ../..
        return 0
    fi
    
    # Run Flyway migration using Maven
    if mvn flyway:migrate \
        -Dflyway.url="jdbc:postgresql://$POSTGRES_HOST:$POSTGRES_PORT/$database" \
        -Dflyway.user="$POSTGRES_USER" \
        -Dflyway.password="$POSTGRES_PASSWORD" \
        -Dflyway.locations="classpath:db/migration" \
        -Dflyway.baselineOnMigrate=true \
        -q; then
        echo -e "${GREEN}✅ Migration completed for $service${NC}"
    else
        echo -e "${RED}❌ Migration failed for $service${NC}"
        cd ../..
        return 1
    fi
    
    cd ../..
}

# Function to check migration status
check_migration_status() {
    local service=$1
    local database=${SERVICE_DBS[$service]}
    
    cd "server/$service"
    
    if [ -d "src/main/resources/db/migration" ]; then
        echo -e "${BLUE}📊 Migration status for $service:${NC}"
        mvn flyway:info \
            -Dflyway.url="jdbc:postgresql://$POSTGRES_HOST:$POSTGRES_PORT/$database" \
            -Dflyway.user="$POSTGRES_USER" \
            -Dflyway.password="$POSTGRES_PASSWORD" \
            -Dflyway.locations="classpath:db/migration" \
            -q || true
    fi
    
    cd ../..
}

# Main execution
main() {
    echo -e "${BLUE}🚀 Starting Database Migrations${NC}"
    echo -e "${BLUE}================================${NC}"
    
    # Check if we're in the right directory
    if [ ! -d "server" ]; then
        echo -e "${RED}❌ Please run this script from the project root directory${NC}"
        exit 1
    fi
    
    # Run migrations for each service
    for service in "${!SERVICE_DBS[@]}"; do
        run_service_migration "$service"
        echo ""
    done
    
    echo -e "${BLUE}📊 Migration Status Summary${NC}"
    echo -e "${BLUE}===========================${NC}"
    
    # Show migration status for each service
    for service in "${!SERVICE_DBS[@]}"; do
        check_migration_status "$service"
        echo ""
    done
    
    echo -e "${GREEN}🎉 All migrations completed!${NC}"
}

# Run main function
main "$@"
