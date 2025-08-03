#!/bin/bash

# RAvED Database Manager
# Interactive database management tool for all microservices

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

# Database configurations
declare -A DATABASES=(
    ["1"]="raved_user_db"
    ["2"]="raved_content_db"
    ["3"]="raved_social_db"
    ["4"]="raved_realtime_db"
    ["5"]="raved_ecommerce_db"
    ["6"]="raved_notification_db"
    ["7"]="raved_analytics_db"
)

declare -A DB_DESCRIPTIONS=(
    ["raved_user_db"]="Users, Universities, Faculties"
    ["raved_content_db"]="Posts, Media, Tags"
    ["raved_social_db"]="Likes, Comments, Follows"
    ["raved_realtime_db"]="Chat, Messages, Reactions"
    ["raved_ecommerce_db"]="Products, Orders, Payments"
    ["raved_notification_db"]="Notifications, Templates"
    ["raved_analytics_db"]="Events, Metrics, Analytics"
)

# Print banner
print_banner() {
    echo -e "${PURPLE}"
    echo "╔══════════════════════════════════════════════════════════════╗"
    echo "║                  RAvED Database Manager                      ║"
    echo "║              Interactive Database Management                 ║"
    echo "╚══════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

# Function to show main menu
show_main_menu() {
    echo -e "${CYAN}📋 Database Management Options${NC}"
    echo -e "${CYAN}===============================${NC}"
    echo ""
    echo -e "${BLUE}1.${NC} List all databases and their status"
    echo -e "${BLUE}2.${NC} Connect to a specific database"
    echo -e "${BLUE}3.${NC} Show database table information"
    echo -e "${BLUE}4.${NC} Run custom SQL query"
    echo -e "${BLUE}5.${NC} Export database data"
    echo -e "${BLUE}6.${NC} Show database sizes"
    echo -e "${BLUE}7.${NC} Check database connections"
    echo -e "${BLUE}8.${NC} Generate pgAdmin connection info"
    echo -e "${BLUE}9.${NC} Exit"
    echo ""
    echo -n -e "${YELLOW}Choose an option (1-9): ${NC}"
}

# Function to list databases
list_databases() {
    echo -e "${BLUE}🗄️  Database Status Overview${NC}"
    echo -e "${BLUE}============================${NC}"
    echo ""
    
    for i in {1..7}; do
        local db=${DATABASES[$i]}
        local desc=${DB_DESCRIPTIONS[$db]}
        
        if PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $db -c '\q' 2>/dev/null; then
            local table_count=$(PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $db -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public';" 2>/dev/null | xargs)
            echo -e "${GREEN}✅ $i. $db${NC} - $desc ($table_count tables)"
        else
            echo -e "${RED}❌ $i. $db${NC} - $desc (Not accessible)"
        fi
    done
    echo ""
}

# Function to show database selection menu
show_database_menu() {
    echo -e "${CYAN}📊 Select Database${NC}"
    echo -e "${CYAN}==================${NC}"
    echo ""
    
    for i in {1..7}; do
        local db=${DATABASES[$i]}
        local desc=${DB_DESCRIPTIONS[$db]}
        echo -e "${BLUE}$i.${NC} $db - $desc"
    done
    echo ""
    echo -n -e "${YELLOW}Choose database (1-7): ${NC}"
}

# Function to connect to database
connect_to_database() {
    show_database_menu
    read -r choice
    
    if [[ "$choice" =~ ^[1-7]$ ]]; then
        local db=${DATABASES[$choice]}
        echo -e "${GREEN}🔗 Connecting to $db...${NC}"
        echo -e "${YELLOW}Type \\q to exit the database connection${NC}"
        echo ""
        PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $db
    else
        echo -e "${RED}❌ Invalid choice. Please select 1-7.${NC}"
    fi
}

# Function to show table information
show_table_info() {
    show_database_menu
    read -r choice
    
    if [[ "$choice" =~ ^[1-7]$ ]]; then
        local db=${DATABASES[$choice]}
        echo -e "${GREEN}📊 Table information for $db:${NC}"
        echo ""
        
        PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $db -c "
        SELECT 
            schemaname,
            tablename,
            attname as column_name,
            typname as data_type,
            attnotnull as not_null
        FROM pg_attribute 
        JOIN pg_class ON pg_attribute.attrelid = pg_class.oid 
        JOIN pg_type ON pg_attribute.atttypid = pg_type.oid 
        JOIN pg_namespace ON pg_class.relnamespace = pg_namespace.oid 
        WHERE pg_namespace.nspname = 'public' 
        AND pg_attribute.attnum > 0 
        AND NOT pg_attribute.attisdropped
        ORDER BY tablename, attnum;
        "
    else
        echo -e "${RED}❌ Invalid choice. Please select 1-7.${NC}"
    fi
}

# Function to run custom SQL
run_custom_sql() {
    show_database_menu
    read -r choice
    
    if [[ "$choice" =~ ^[1-7]$ ]]; then
        local db=${DATABASES[$choice]}
        echo -e "${GREEN}💻 Running custom SQL on $db${NC}"
        echo -e "${YELLOW}Enter your SQL query (press Enter twice to execute):${NC}"
        echo ""
        
        local sql=""
        while IFS= read -r line; do
            if [[ -z "$line" ]]; then
                break
            fi
            sql+="$line "
        done
        
        if [[ -n "$sql" ]]; then
            echo -e "${BLUE}Executing: $sql${NC}"
            echo ""
            PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $db -c "$sql"
        else
            echo -e "${YELLOW}No SQL provided.${NC}"
        fi
    else
        echo -e "${RED}❌ Invalid choice. Please select 1-7.${NC}"
    fi
}

# Function to show database sizes
show_database_sizes() {
    echo -e "${BLUE}💾 Database Sizes${NC}"
    echo -e "${BLUE}=================${NC}"
    echo ""
    
    for i in {1..7}; do
        local db=${DATABASES[$i]}
        
        if PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $db -c '\q' 2>/dev/null; then
            local size=$(PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $db -t -c "SELECT pg_size_pretty(pg_database_size('$db'));" 2>/dev/null | xargs)
            echo -e "${GREEN}📊 $db: $size${NC}"
        else
            echo -e "${RED}❌ $db: Not accessible${NC}"
        fi
    done
    echo ""
}

# Function to generate pgAdmin connection info
generate_pgadmin_info() {
    echo -e "${CYAN}🔧 pgAdmin 4 Connection Information${NC}"
    echo -e "${CYAN}===================================${NC}"
    echo ""
    echo -e "${YELLOW}Create a Server Group called 'RAvED Local' and add these servers:${NC}"
    echo ""
    
    for i in {1..7}; do
        local db=${DATABASES[$i]}
        local desc=${DB_DESCRIPTIONS[$db]}
        
        echo -e "${BLUE}Server $i: $db${NC}"
        echo -e "  Name: $db"
        echo -e "  Host: $POSTGRES_HOST"
        echo -e "  Port: $POSTGRES_PORT"
        echo -e "  Database: $db"
        echo -e "  Username: $POSTGRES_USER"
        echo -e "  Password: $POSTGRES_PASSWORD"
        echo -e "  Description: $desc"
        echo ""
    done
    
    echo -e "${YELLOW}For cloud databases (Neon), create separate server groups:${NC}"
    echo -e "${YELLOW}• 'RAvED Staging' - for staging environment${NC}"
    echo -e "${YELLOW}• 'RAvED Production' - for production environment${NC}"
    echo ""
}

# Main execution loop
main() {
    print_banner
    
    while true; do
        show_main_menu
        read -r choice
        echo ""
        
        case $choice in
            1)
                list_databases
                ;;
            2)
                connect_to_database
                ;;
            3)
                show_table_info
                ;;
            4)
                run_custom_sql
                ;;
            5)
                echo -e "${YELLOW}Database export functionality - Coming soon!${NC}"
                ;;
            6)
                show_database_sizes
                ;;
            7)
                list_databases
                ;;
            8)
                generate_pgadmin_info
                ;;
            9)
                echo -e "${GREEN}👋 Goodbye!${NC}"
                exit 0
                ;;
            *)
                echo -e "${RED}❌ Invalid choice. Please select 1-9.${NC}"
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
