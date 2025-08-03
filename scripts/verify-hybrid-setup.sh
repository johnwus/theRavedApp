#!/bin/bash

# RAvED Hybrid Database Setup Verification Script
# Verifies that the hybrid database configuration is working correctly

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

# Service configurations
declare -A SERVICES=(
    ["user-service"]="8081"
    ["content-service"]="8082"
    ["social-service"]="8083"
    ["realtime-service"]="8084"
    ["ecommerce-service"]="8085"
    ["notification-service"]="8086"
    ["analytics-service"]="8087"
)

declare -A DATABASES=(
    ["user-service"]="raved_user_db"
    ["content-service"]="raved_content_db"
    ["social-service"]="raved_social_db"
    ["realtime-service"]="raved_realtime_db"
    ["ecommerce-service"]="raved_ecommerce_db"
    ["notification-service"]="raved_notification_db"
    ["analytics-service"]="raved_analytics_db"
)

# Print banner
print_banner() {
    echo -e "${PURPLE}"
    echo "╔══════════════════════════════════════════════════════════════╗"
    echo "║              RAvED Hybrid Setup Verification                 ║"
    echo "║                  Testing All Components                      ║"
    echo "╚══════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

# Function to check database connectivity
check_database_connectivity() {
    echo -e "${BLUE}🗄️  Checking database connectivity...${NC}"
    
    local all_connected=true
    
    for service in "${!DATABASES[@]}"; do
        local database=${DATABASES[$service]}
        
        if PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $database -c '\q' 2>/dev/null; then
            echo -e "${GREEN}✅ $database - Connected${NC}"
        else
            echo -e "${RED}❌ $database - Connection failed${NC}"
            all_connected=false
        fi
    done
    
    if [ "$all_connected" = true ]; then
        echo -e "${GREEN}✅ All databases are accessible${NC}"
        return 0
    else
        echo -e "${RED}❌ Some databases are not accessible${NC}"
        return 1
    fi
}

# Function to check database tables
check_database_tables() {
    echo -e "${BLUE}📊 Checking database tables...${NC}"
    
    local all_tables_exist=true
    
    for service in "${!DATABASES[@]}"; do
        local database=${DATABASES[$service]}
        local table_count=$(PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $database -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public';" 2>/dev/null | xargs)
        
        if [ "$table_count" -gt 0 ]; then
            echo -e "${GREEN}✅ $database - $table_count tables${NC}"
        else
            echo -e "${RED}❌ $database - No tables found${NC}"
            all_tables_exist=false
        fi
    done
    
    if [ "$all_tables_exist" = true ]; then
        echo -e "${GREEN}✅ All databases have tables${NC}"
        return 0
    else
        echo -e "${RED}❌ Some databases are missing tables${NC}"
        return 1
    fi
}

# Function to check service configurations
check_service_configurations() {
    echo -e "${BLUE}⚙️  Checking service configurations...${NC}"
    
    local all_configs_exist=true
    
    for service in "${!SERVICES[@]}"; do
        local config_file="server/$service/src/main/resources/application.yml"
        
        if [ -f "$config_file" ]; then
            # Check if hybrid configuration exists
            if grep -q "profiles: local" "$config_file" && grep -q "profiles: staging" "$config_file" && grep -q "profiles: production" "$config_file"; then
                echo -e "${GREEN}✅ $service - Hybrid configuration present${NC}"
            else
                echo -e "${YELLOW}⚠️  $service - Missing hybrid configuration${NC}"
                all_configs_exist=false
            fi
        else
            echo -e "${RED}❌ $service - Configuration file not found${NC}"
            all_configs_exist=false
        fi
    done
    
    if [ "$all_configs_exist" = true ]; then
        echo -e "${GREEN}✅ All service configurations are hybrid-ready${NC}"
        return 0
    else
        echo -e "${RED}❌ Some service configurations need updating${NC}"
        return 1
    fi
}

# Function to check migration files
check_migration_files() {
    echo -e "${BLUE}🔄 Checking migration files...${NC}"
    
    local all_migrations_exist=true
    
    for service in "${!SERVICES[@]}"; do
        local migration_dir="server/$service/src/main/resources/db/migration"
        
        if [ -d "$migration_dir" ]; then
            local migration_count=$(find "$migration_dir" -name "*.sql" | wc -l)
            if [ "$migration_count" -gt 0 ]; then
                echo -e "${GREEN}✅ $service - $migration_count migration files${NC}"
            else
                echo -e "${YELLOW}⚠️  $service - No migration files found${NC}"
            fi
        else
            echo -e "${RED}❌ $service - Migration directory not found${NC}"
            all_migrations_exist=false
        fi
    done
    
    if [ "$all_migrations_exist" = true ]; then
        echo -e "${GREEN}✅ All services have migration directories${NC}"
        return 0
    else
        echo -e "${RED}❌ Some services are missing migration directories${NC}"
        return 1
    fi
}

# Function to check environment files
check_environment_files() {
    echo -e "${BLUE}🌍 Checking environment files...${NC}"
    
    local env_files=(".env.local" ".env.staging" ".env.production")
    local all_env_files_exist=true
    
    for env_file in "${env_files[@]}"; do
        if [ -f "$env_file" ]; then
            echo -e "${GREEN}✅ $env_file - Present${NC}"
        else
            echo -e "${YELLOW}⚠️  $env_file - Not found${NC}"
            all_env_files_exist=false
        fi
    done
    
    # Check current .env file
    if [ -f ".env" ]; then
        echo -e "${GREEN}✅ .env - Present${NC}"
    else
        echo -e "${RED}❌ .env - Not found${NC}"
        all_env_files_exist=false
    fi
    
    if [ "$all_env_files_exist" = true ]; then
        echo -e "${GREEN}✅ All environment files are present${NC}"
        return 0
    else
        echo -e "${YELLOW}⚠️  Some environment files are missing${NC}"
        return 1
    fi
}

# Function to check Docker services
check_docker_services() {
    echo -e "${BLUE}🐳 Checking Docker services...${NC}"
    
    local required_services=("postgres" "redis")
    local all_services_running=true
    
    for service in "${required_services[@]}"; do
        if docker ps | grep -q "$service"; then
            echo -e "${GREEN}✅ $service - Running${NC}"
        else
            echo -e "${RED}❌ $service - Not running${NC}"
            all_services_running=false
        fi
    done
    
    if [ "$all_services_running" = true ]; then
        echo -e "${GREEN}✅ All required Docker services are running${NC}"
        return 0
    else
        echo -e "${RED}❌ Some Docker services are not running${NC}"
        return 1
    fi
}

# Function to check scripts
check_scripts() {
    echo -e "${BLUE}📜 Checking setup scripts...${NC}"
    
    local scripts=(
        "scripts/setup-hybrid-databases.sh"
        "scripts/setup-databases.sh"
        "scripts/update-database-configs.sh"
        "scripts/database/migrate.sh"
        "scripts/database/seed.sh"
    )
    
    local all_scripts_exist=true
    
    for script in "${scripts[@]}"; do
        if [ -f "$script" ] && [ -x "$script" ]; then
            echo -e "${GREEN}✅ $script - Present and executable${NC}"
        elif [ -f "$script" ]; then
            echo -e "${YELLOW}⚠️  $script - Present but not executable${NC}"
        else
            echo -e "${RED}❌ $script - Not found${NC}"
            all_scripts_exist=false
        fi
    done
    
    if [ "$all_scripts_exist" = true ]; then
        echo -e "${GREEN}✅ All setup scripts are present${NC}"
        return 0
    else
        echo -e "${RED}❌ Some setup scripts are missing${NC}"
        return 1
    fi
}

# Function to show verification summary
show_verification_summary() {
    echo -e "${CYAN}📋 Verification Summary${NC}"
    echo -e "${CYAN}======================${NC}"
    
    local total_checks=6
    local passed_checks=0
    
    # Run all checks and count passes
    if check_docker_services; then ((passed_checks++)); fi
    echo ""
    if check_database_connectivity; then ((passed_checks++)); fi
    echo ""
    if check_database_tables; then ((passed_checks++)); fi
    echo ""
    if check_service_configurations; then ((passed_checks++)); fi
    echo ""
    if check_migration_files; then ((passed_checks++)); fi
    echo ""
    if check_environment_files; then ((passed_checks++)); fi
    echo ""
    if check_scripts; then ((passed_checks++)); fi
    echo ""
    
    echo -e "${CYAN}Results: $passed_checks/$total_checks checks passed${NC}"
    
    if [ $passed_checks -eq $total_checks ]; then
        echo -e "${GREEN}🎉 All checks passed! Your hybrid database setup is working perfectly!${NC}"
        return 0
    else
        echo -e "${YELLOW}⚠️  Some checks failed. Please review the issues above.${NC}"
        return 1
    fi
}

# Main execution
main() {
    print_banner
    
    echo -e "${BLUE}🔍 Starting hybrid database setup verification...${NC}"
    echo -e "${BLUE}=================================================${NC}"
    echo ""
    
    show_verification_summary
}

# Handle script arguments
case "${1:-}" in
    --help)
        echo "RAvED Hybrid Database Setup Verification Script"
        echo ""
        echo "Usage: $0"
        echo ""
        echo "This script verifies that the hybrid database configuration"
        echo "is working correctly by checking:"
        echo "  • Docker services"
        echo "  • Database connectivity"
        echo "  • Database tables"
        echo "  • Service configurations"
        echo "  • Migration files"
        echo "  • Environment files"
        echo "  • Setup scripts"
        ;;
    *)
        main "$@"
        ;;
esac
