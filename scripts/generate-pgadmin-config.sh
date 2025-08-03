#!/bin/bash

# Generate pgAdmin Configuration for RAvED Databases
# This script helps you set up pgAdmin server groups and connections

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
declare -A LOCAL_DATABASES=(
    ["User Service DB"]="raved_user_db|Users, Universities, Faculties, Authentication"
    ["Content Service DB"]="raved_content_db|Posts, Media Files, Tags, Mentions"
    ["Social Service DB"]="raved_social_db|Likes, Comments, Follows, Activities"
    ["Realtime Service DB"]="raved_realtime_db|Chat Rooms, Messages, Reactions"
    ["Ecommerce Service DB"]="raved_ecommerce_db|Products, Orders, Payments, Categories"
    ["Notification Service DB"]="raved_notification_db|Notifications, Templates, Device Tokens"
    ["Analytics Service DB"]="raved_analytics_db|Events, User Metrics, Content Metrics"
)

declare -A PRODUCTION_DATABASES=(
    ["User Service Production"]="raved_user_production|Production - Users, Universities, Faculties"
    ["Content Service Production"]="raved_content_production|Production - Posts, Media Files, Tags"
    ["Social Service Production"]="raved_social_production|Production - Likes, Comments, Follows"
    ["Realtime Service Production"]="raved_realtime_production|Production - Chat Rooms, Messages"
    ["Ecommerce Service Production"]="raved_ecommerce_production|Production - Products, Orders, Payments"
    ["Notification Service Production"]="raved_notification_production|Production - Notifications, Templates"
    ["Analytics Service Production"]="raved_analytics_production|Production - Events, User Metrics"
)

# Print banner
print_banner() {
    echo -e "${PURPLE}"
    echo "╔══════════════════════════════════════════════════════════════╗"
    echo "║              pgAdmin Configuration Generator                  ║"
    echo "║                  RAvED Database Setup                        ║"
    echo "╚══════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

# Function to show local development configuration
show_local_config() {
    echo -e "${CYAN}🏠 Local Development Server Group Configuration${NC}"
    echo -e "${CYAN}===============================================${NC}"
    echo ""
    echo -e "${YELLOW}1. Create Server Group:${NC}"
    echo -e "   • Right-click 'Servers' → Create → Server Group"
    echo -e "   • Name: ${GREEN}RAvED Local Development${NC}"
    echo ""
    echo -e "${YELLOW}2. Add these servers to the group:${NC}"
    echo ""
    
    local counter=1
    for server_name in "${!LOCAL_DATABASES[@]}"; do
        IFS='|' read -r db_name description <<< "${LOCAL_DATABASES[$server_name]}"
        
        echo -e "${BLUE}Server $counter: $server_name${NC}"
        echo -e "   General Tab:"
        echo -e "     • Name: ${GREEN}$server_name${NC}"
        echo -e "     • Server Group: ${GREEN}RAvED Local Development${NC}"
        echo -e "     • Comments: ${GREEN}$description${NC}"
        echo ""
        echo -e "   Connection Tab:"
        echo -e "     • Host: ${GREEN}localhost${NC}"
        echo -e "     • Port: ${GREEN}5432${NC}"
        echo -e "     • Database: ${GREEN}$db_name${NC}"
        echo -e "     • Username: ${GREEN}raved_user${NC}"
        echo -e "     • Password: ${GREEN}raved_password${NC}"
        echo -e "     • Save password: ${GREEN}✓${NC}"
        echo ""
        echo -e "   ${YELLOW}---${NC}"
        echo ""
        
        ((counter++))
    done
}

# Function to show Neon setup instructions
show_neon_setup() {
    echo -e "${CYAN}☁️  Neon Production Setup Instructions${NC}"
    echo -e "${CYAN}======================================${NC}"
    echo ""
    echo -e "${YELLOW}Step 1: Create Neon Account${NC}"
    echo -e "   • Go to: ${GREEN}https://console.neon.tech${NC}"
    echo -e "   • Sign up with GitHub or email"
    echo -e "   • Verify your email"
    echo ""
    echo -e "${YELLOW}Step 2: Create Production Project${NC}"
    echo -e "   • Click 'Create Project'"
    echo -e "   • Project Name: ${GREEN}RAvED Production${NC}"
    echo -e "   • Database Name: ${GREEN}raved_production${NC}"
    echo -e "   • Region: Choose closest to your users"
    echo -e "   • PostgreSQL Version: ${GREEN}15${NC}"
    echo ""
    echo -e "${YELLOW}Step 3: Create Individual Databases${NC}"
    echo -e "   In Neon dashboard → Databases tab → Create Database:"
    echo ""
    
    for server_name in "${!PRODUCTION_DATABASES[@]}"; do
        IFS='|' read -r db_name description <<< "${PRODUCTION_DATABASES[$server_name]}"
        echo -e "   • ${GREEN}$db_name${NC}"
    done
    echo ""
    echo -e "${YELLOW}Step 4: Get Connection Strings${NC}"
    echo -e "   • For each database, copy the connection string"
    echo -e "   • Format: ${GREEN}postgresql://user:pass@host/database?sslmode=require${NC}"
    echo ""
}

# Function to show production pgAdmin configuration
show_production_config() {
    echo -e "${CYAN}🏭 Production Server Group Configuration${NC}"
    echo -e "${CYAN}========================================${NC}"
    echo ""
    echo -e "${YELLOW}1. Create Server Group:${NC}"
    echo -e "   • Right-click 'Servers' → Create → Server Group"
    echo -e "   • Name: ${GREEN}RAvED Neon Production${NC}"
    echo ""
    echo -e "${YELLOW}2. Add these servers (using your Neon connection strings):${NC}"
    echo ""
    
    local counter=1
    for server_name in "${!PRODUCTION_DATABASES[@]}"; do
        IFS='|' read -r db_name description <<< "${PRODUCTION_DATABASES[$server_name]}"
        
        echo -e "${BLUE}Server $counter: $server_name${NC}"
        echo -e "   General Tab:"
        echo -e "     • Name: ${GREEN}$server_name${NC}"
        echo -e "     • Server Group: ${GREEN}RAvED Neon Production${NC}"
        echo -e "     • Comments: ${GREEN}$description${NC}"
        echo ""
        echo -e "   Connection Tab:"
        echo -e "     • Host: ${GREEN}[from your Neon connection string]${NC}"
        echo -e "     • Port: ${GREEN}5432${NC}"
        echo -e "     • Database: ${GREEN}$db_name${NC}"
        echo -e "     • Username: ${GREEN}[from your Neon connection string]${NC}"
        echo -e "     • Password: ${GREEN}[from your Neon connection string]${NC}"
        echo -e "     • Save password: ${GREEN}✓${NC}"
        echo ""
        echo -e "   SSL Tab:"
        echo -e "     • SSL mode: ${GREEN}Require${NC}"
        echo ""
        echo -e "   ${YELLOW}---${NC}"
        echo ""
        
        ((counter++))
    done
}

# Function to generate environment variables template
generate_env_template() {
    echo -e "${CYAN}📝 Environment Variables Template${NC}"
    echo -e "${CYAN}==================================${NC}"
    echo ""
    echo -e "${YELLOW}Add these to your .env.production file:${NC}"
    echo ""
    
    echo "# Neon Production Database URLs"
    for server_name in "${!PRODUCTION_DATABASES[@]}"; do
        IFS='|' read -r db_name description <<< "${PRODUCTION_DATABASES[$server_name]}"
        local env_var=$(echo "$db_name" | tr '[:lower:]' '[:upper:]' | sed 's/RAVED_//' | sed 's/_PRODUCTION//' | sed 's/_/_SERVICE_/')
        echo "${env_var}_DB_URL=postgresql://username:password@your-neon-host/$db_name?sslmode=require"
    done
    
    echo ""
    echo "# Database Credentials"
    echo "DATABASE_USERNAME=your_neon_username"
    echo "DATABASE_PASSWORD=your_neon_password"
    echo ""
}

# Function to test local connections
test_local_connections() {
    echo -e "${CYAN}🔍 Testing Local Database Connections${NC}"
    echo -e "${CYAN}=====================================${NC}"
    echo ""
    
    local all_connected=true
    
    for server_name in "${!LOCAL_DATABASES[@]}"; do
        IFS='|' read -r db_name description <<< "${LOCAL_DATABASES[$server_name]}"
        
        if PGPASSWORD=raved_password psql -h localhost -p 5432 -U raved_user -d $db_name -c '\q' 2>/dev/null; then
            echo -e "${GREEN}✅ $db_name - Connected${NC}"
        else
            echo -e "${RED}❌ $db_name - Connection failed${NC}"
            all_connected=false
        fi
    done
    
    echo ""
    if [ "$all_connected" = true ]; then
        echo -e "${GREEN}🎉 All local databases are accessible!${NC}"
        echo -e "${GREEN}You can now set up pgAdmin with the configuration above.${NC}"
    else
        echo -e "${RED}⚠️  Some databases are not accessible.${NC}"
        echo -e "${YELLOW}Run: ./scripts/setup-hybrid-databases.sh${NC}"
    fi
    echo ""
}

# Main menu
show_menu() {
    echo -e "${BLUE}📋 pgAdmin Configuration Options${NC}"
    echo -e "${BLUE}=================================${NC}"
    echo ""
    echo -e "${YELLOW}1.${NC} Show Local Development Configuration"
    echo -e "${YELLOW}2.${NC} Show Neon Production Setup Instructions"
    echo -e "${YELLOW}3.${NC} Show Production pgAdmin Configuration"
    echo -e "${YELLOW}4.${NC} Generate Environment Variables Template"
    echo -e "${YELLOW}5.${NC} Test Local Database Connections"
    echo -e "${YELLOW}6.${NC} Show All (Complete Guide)"
    echo -e "${YELLOW}7.${NC} Exit"
    echo ""
    echo -n -e "${CYAN}Choose an option (1-7): ${NC}"
}

# Main execution
main() {
    print_banner
    
    while true; do
        show_menu
        read -r choice
        echo ""
        
        case $choice in
            1)
                show_local_config
                ;;
            2)
                show_neon_setup
                ;;
            3)
                show_production_config
                ;;
            4)
                generate_env_template
                ;;
            5)
                test_local_connections
                ;;
            6)
                show_local_config
                echo ""
                show_neon_setup
                echo ""
                show_production_config
                echo ""
                generate_env_template
                ;;
            7)
                echo -e "${GREEN}👋 Happy database managing!${NC}"
                exit 0
                ;;
            *)
                echo -e "${RED}❌ Invalid choice. Please select 1-7.${NC}"
                ;;
        esac
        
        echo ""
        echo -e "${YELLOW}Press Enter to continue...${NC}"
        read -r
        clear
    done
}

# Run main function
main "$@"
