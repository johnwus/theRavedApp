#!/bin/bash

# Verify Database Setup for RAvED
# Shows complete status of all databases and tables

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Database configurations
declare -A DATABASES=(
    ["User Service"]="raved_user_db"
    ["Content Service"]="raved_content_db"
    ["Social Service"]="raved_social_db"
    ["Realtime Service"]="raved_realtime_db"
    ["Ecommerce Service"]="raved_ecommerce_db"
    ["Notification Service"]="raved_notification_db"
    ["Analytics Service"]="raved_analytics_db"
)

print_banner() {
    echo -e "${PURPLE}"
    echo "╔══════════════════════════════════════════════════════════════╗"
    echo "║                Database Setup Verification                   ║"
    echo "║                    Complete Status                           ║"
    echo "╚══════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

# Function to show database status
show_database_status() {
    echo -e "${CYAN}📊 Database Status Overview${NC}"
    echo -e "${CYAN}===========================${NC}"
    echo ""
    
    for service in "${!DATABASES[@]}"; do
        local database=${DATABASES[$service]}
        
        if docker exec workspace_postgres_1 psql -U raved_user -d $database -c '\q' 2>/dev/null; then
            local table_count=$(docker exec workspace_postgres_1 psql -U raved_user -d $database -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public';" 2>/dev/null | xargs)
            echo -e "${GREEN}✅ $service${NC}"
            echo -e "   Database: $database"
            echo -e "   Tables: $table_count"
            echo ""
        else
            echo -e "${RED}❌ $service - Database not accessible${NC}"
            echo ""
        fi
    done
}

# Function to show table details for each database
show_table_details() {
    echo -e "${CYAN}📋 Table Details${NC}"
    echo -e "${CYAN}================${NC}"
    echo ""
    
    for service in "${!DATABASES[@]}"; do
        local database=${DATABASES[$service]}
        echo -e "${BLUE}$service ($database):${NC}"
        
        if docker exec workspace_postgres_1 psql -U raved_user -d $database -c '\q' 2>/dev/null; then
            docker exec workspace_postgres_1 psql -U raved_user -d $database -c "\dt" | grep -E "public|table" | while read line; do
                if [[ $line == *"table"* ]]; then
                    table_name=$(echo $line | awk '{print $2}')
                    echo -e "   • $table_name"
                fi
            done
        else
            echo -e "   ${RED}Database not accessible${NC}"
        fi
        echo ""
    done
}

# Function to show sample data
show_sample_data() {
    echo -e "${CYAN}📝 Sample Data${NC}"
    echo -e "${CYAN}=============${NC}"
    echo ""
    
    echo -e "${BLUE}Universities:${NC}"
    docker exec workspace_postgres_1 psql -U raved_user -d raved_user_db -c "SELECT name, code, city FROM universities;" 2>/dev/null || echo "No data"
    echo ""
    
    echo -e "${BLUE}Product Categories:${NC}"
    docker exec workspace_postgres_1 psql -U raved_user -d raved_ecommerce_db -c "SELECT name, description FROM product_categories WHERE parent_category_id IS NULL LIMIT 5;" 2>/dev/null || echo "No data"
    echo ""
    
    echo -e "${BLUE}Notification Templates:${NC}"
    docker exec workspace_postgres_1 psql -U raved_user -d raved_notification_db -c "SELECT template_name, template_type FROM notification_templates LIMIT 5;" 2>/dev/null || echo "No data"
    echo ""
}

# Function to show pgAdmin connection info
show_pgadmin_info() {
    echo -e "${CYAN}🔧 pgAdmin Connection Information${NC}"
    echo -e "${CYAN}==================================${NC}"
    echo ""
    echo -e "${YELLOW}Create a Server Group called 'RAvED Local Development' and add these servers:${NC}"
    echo ""
    
    for service in "${!DATABASES[@]}"; do
        local database=${DATABASES[$service]}
        
        echo -e "${BLUE}$service:${NC}"
        echo -e "   Name: $service DB"
        echo -e "   Host: localhost"
        echo -e "   Port: 5432"
        echo -e "   Database: $database"
        echo -e "   Username: raved_user"
        echo -e "   Password: raved_password"
        echo -e "   Save password: ✓"
        echo ""
    done
}

# Function to test connections
test_connections() {
    echo -e "${CYAN}🔍 Testing Database Connections${NC}"
    echo -e "${CYAN}===============================${NC}"
    echo ""
    
    local all_connected=true
    
    for service in "${!DATABASES[@]}"; do
        local database=${DATABASES[$service]}
        
        if docker exec workspace_postgres_1 psql -U raved_user -d $database -c "SELECT 'Connected successfully' as status;" 2>/dev/null | grep -q "Connected successfully"; then
            echo -e "${GREEN}✅ $database - Connection successful${NC}"
        else
            echo -e "${RED}❌ $database - Connection failed${NC}"
            all_connected=false
        fi
    done
    
    echo ""
    if [ "$all_connected" = true ]; then
        echo -e "${GREEN}🎉 All databases are accessible and ready for pgAdmin!${NC}"
    else
        echo -e "${RED}⚠️  Some databases are not accessible${NC}"
    fi
    echo ""
}

# Function to show next steps
show_next_steps() {
    echo -e "${CYAN}🚀 Next Steps${NC}"
    echo -e "${CYAN}=============${NC}"
    echo ""
    echo -e "${YELLOW}1. Connect pgAdmin:${NC}"
    echo -e "   • Use the connection details above"
    echo -e "   • Create server group 'RAvED Local Development'"
    echo -e "   • Add all 7 database servers"
    echo ""
    echo -e "${YELLOW}2. Start microservices:${NC}"
    echo -e "   • Run: docker-compose up -d"
    echo -e "   • Services will connect to their respective databases"
    echo ""
    echo -e "${YELLOW}3. Test the application:${NC}"
    echo -e "   • API Gateway: http://localhost:8080"
    echo -e "   • Eureka Dashboard: http://localhost:8761"
    echo ""
    echo -e "${YELLOW}4. For production deployment:${NC}"
    echo -e "   • Set up Neon databases"
    echo -e "   • Update environment variables"
    echo -e "   • Deploy with SPRING_PROFILES_ACTIVE=production"
    echo ""
}

# Main execution
main() {
    print_banner
    
    echo -e "${BLUE}🔍 Verifying RAvED Database Setup${NC}"
    echo -e "${BLUE}==================================${NC}"
    echo ""
    
    test_connections
    show_database_status
    show_table_details
    show_sample_data
    show_pgadmin_info
    show_next_steps
    
    echo -e "${GREEN}✨ Database verification completed!${NC}"
}

# Run main function
main "$@"
