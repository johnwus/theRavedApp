#!/bin/bash

# Complete Database Setup Script for RAvED App
# This script orchestrates the complete database setup process

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Configuration
POSTGRES_HOST=${POSTGRES_HOST:-localhost}
POSTGRES_PORT=${POSTGRES_PORT:-5432}
POSTGRES_USER=${POSTGRES_USER:-raved_user}
POSTGRES_PASSWORD=${POSTGRES_PASSWORD:-raved_password}

# Print banner
print_banner() {
    echo -e "${PURPLE}"
    echo "╔══════════════════════════════════════════════════════════════╗"
    echo "║                    RAvED Database Setup                      ║"
    echo "║              Complete Microservices Database                 ║"
    echo "║                     Setup & Migration                        ║"
    echo "╚══════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

# Function to check prerequisites
check_prerequisites() {
    echo -e "${BLUE}🔍 Checking prerequisites...${NC}"
    
    # Check if Docker is running
    if ! docker ps >/dev/null 2>&1; then
        echo -e "${RED}❌ Docker is not running. Please start Docker first.${NC}"
        exit 1
    fi
    
    # Check if PostgreSQL container is running
    if ! docker ps | grep -q postgres; then
        echo -e "${YELLOW}⚠️  PostgreSQL container not found. Starting infrastructure...${NC}"
        docker-compose up -d postgres redis
        echo -e "${YELLOW}⏳ Waiting for PostgreSQL to be ready...${NC}"
        sleep 10
    fi
    
    # Check if Maven is available
    if ! command -v mvn &> /dev/null; then
        echo -e "${RED}❌ Maven is not installed. Please install Maven first.${NC}"
        exit 1
    fi
    
    # Check if psql is available
    if ! command -v psql &> /dev/null; then
        echo -e "${RED}❌ PostgreSQL client (psql) is not installed. Please install it first.${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}✅ All prerequisites met${NC}"
}

# Function to show database status
show_database_status() {
    echo -e "${CYAN}📊 Database Status Summary${NC}"
    echo -e "${CYAN}==========================${NC}"
    
    local databases=(
        "raved_user_db"
        "raved_content_db"
        "raved_social_db"
        "raved_realtime_db"
        "raved_ecommerce_db"
        "raved_notification_db"
        "raved_analytics_db"
    )
    
    for db in "${databases[@]}"; do
        if PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $db -c '\q' 2>/dev/null; then
            local table_count=$(PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $db -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public';" 2>/dev/null | xargs)
            echo -e "${GREEN}✅ $db: $table_count tables${NC}"
        else
            echo -e "${RED}❌ $db: Not accessible${NC}"
        fi
    done
}

# Function to run complete setup
run_complete_setup() {
    echo -e "${BLUE}🚀 Starting complete database setup...${NC}"
    
    # Step 1: Run database setup
    echo -e "${YELLOW}📋 Step 1: Setting up databases and running migrations...${NC}"
    if ./scripts/setup/setup-database.sh; then
        echo -e "${GREEN}✅ Database setup completed${NC}"
    else
        echo -e "${RED}❌ Database setup failed${NC}"
        exit 1
    fi
    
    echo ""
    
    # Step 2: Seed databases
    echo -e "${YELLOW}📋 Step 2: Seeding databases with initial data...${NC}"
    if ./scripts/database/seed.sh; then
        echo -e "${GREEN}✅ Database seeding completed${NC}"
    else
        echo -e "${RED}❌ Database seeding failed${NC}"
        exit 1
    fi
    
    echo ""
}

# Function to show next steps
show_next_steps() {
    echo -e "${PURPLE}🎯 Next Steps${NC}"
    echo -e "${PURPLE}=============${NC}"
    echo -e "${CYAN}1. Start all microservices:${NC}"
    echo -e "   ${YELLOW}docker-compose up -d${NC}"
    echo ""
    echo -e "${CYAN}2. Check service health:${NC}"
    echo -e "   ${YELLOW}make health${NC}"
    echo ""
    echo -e "${CYAN}3. View service logs:${NC}"
    echo -e "   ${YELLOW}make logs${NC}"
    echo ""
    echo -e "${CYAN}4. Access services:${NC}"
    echo -e "   ${YELLOW}• API Gateway: http://localhost:8080${NC}"
    echo -e "   ${YELLOW}• Eureka Server: http://localhost:8761${NC}"
    echo -e "   ${YELLOW}• PostgreSQL: localhost:5432${NC}"
    echo -e "   ${YELLOW}• Redis: localhost:6379${NC}"
    echo ""
    echo -e "${CYAN}5. Database Management:${NC}"
    echo -e "   ${YELLOW}• Re-run migrations: ./scripts/database/migrate.sh${NC}"
    echo -e "   ${YELLOW}• Re-seed data: ./scripts/database/seed.sh${NC}"
    echo -e "   ${YELLOW}• Backup databases: make db-backup${NC}"
}

# Main execution
main() {
    print_banner
    
    echo -e "${BLUE}🏁 Starting RAvED Database Setup Process${NC}"
    echo -e "${BLUE}=========================================${NC}"
    
    check_prerequisites
    echo ""
    
    run_complete_setup
    echo ""
    
    show_database_status
    echo ""
    
    show_next_steps
    
    echo -e "${GREEN}🎉 Database setup completed successfully!${NC}"
    echo -e "${GREEN}Your RAvED microservices databases are ready to use.${NC}"
}

# Handle script arguments
case "${1:-}" in
    --status)
        show_database_status
        ;;
    --help)
        echo "RAvED Database Setup Script"
        echo ""
        echo "Usage: $0 [OPTIONS]"
        echo ""
        echo "Options:"
        echo "  --status    Show current database status"
        echo "  --help      Show this help message"
        echo ""
        echo "Environment Variables:"
        echo "  POSTGRES_HOST      PostgreSQL host (default: localhost)"
        echo "  POSTGRES_PORT      PostgreSQL port (default: 5432)"
        echo "  POSTGRES_USER      PostgreSQL user (default: raved_user)"
        echo "  POSTGRES_PASSWORD  PostgreSQL password (default: raved_password)"
        ;;
    *)
        main "$@"
        ;;
esac
