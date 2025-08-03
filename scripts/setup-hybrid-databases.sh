#!/bin/bash

# RAvED Hybrid Database Setup Script
# Complete setup for local development and cloud deployment readiness

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Print banner
print_banner() {
    echo -e "${PURPLE}"
    echo "╔══════════════════════════════════════════════════════════════╗"
    echo "║                RAvED Hybrid Database Setup                   ║"
    echo "║          Local Development + Cloud Deployment Ready          ║"
    echo "╚══════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

# Function to check prerequisites
check_prerequisites() {
    echo -e "${BLUE}🔍 Checking prerequisites...${NC}"
    
    local missing_tools=()
    
    # Check Docker
    if ! command -v docker &> /dev/null; then
        missing_tools+=("Docker")
    fi
    
    # Check Docker Compose
    if ! command -v docker-compose &> /dev/null; then
        missing_tools+=("Docker Compose")
    fi
    
    # Check Maven
    if ! command -v mvn &> /dev/null; then
        missing_tools+=("Maven")
    fi
    
    # Check PostgreSQL client
    if ! command -v psql &> /dev/null; then
        missing_tools+=("PostgreSQL client (psql)")
    fi
    
    if [ ${#missing_tools[@]} -ne 0 ]; then
        echo -e "${RED}❌ Missing required tools:${NC}"
        for tool in "${missing_tools[@]}"; do
            echo -e "${RED}   - $tool${NC}"
        done
        echo -e "${YELLOW}Please install the missing tools and try again.${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}✅ All prerequisites met${NC}"
}

# Function to update service configurations
update_configurations() {
    echo -e "${BLUE}🔧 Updating service configurations for hybrid setup...${NC}"
    
    if [ -f "scripts/update-database-configs.sh" ]; then
        ./scripts/update-database-configs.sh
    else
        echo -e "${RED}❌ Configuration update script not found${NC}"
        exit 1
    fi
}

# Function to setup local environment
setup_local_environment() {
    echo -e "${BLUE}🏠 Setting up local development environment...${NC}"
    
    # Copy local environment file
    if [ -f ".env.local" ]; then
        cp .env.local .env
        echo -e "${GREEN}✅ Local environment file configured${NC}"
    else
        echo -e "${YELLOW}⚠️  .env.local not found, creating default...${NC}"
        create_default_env_local
    fi
    
    # Start local infrastructure
    echo -e "${YELLOW}🐳 Starting local infrastructure...${NC}"
    docker-compose up -d postgres redis
    
    # Wait for services to be ready
    echo -e "${YELLOW}⏳ Waiting for services to be ready...${NC}"
    sleep 15
    
    # Setup databases
    echo -e "${YELLOW}🗄️  Setting up local databases...${NC}"
    if [ -f "scripts/setup-databases.sh" ]; then
        ./scripts/setup-databases.sh
    else
        echo -e "${RED}❌ Database setup script not found${NC}"
        exit 1
    fi
}

# Function to create default local environment file
create_default_env_local() {
    cat > .env << 'EOF'
# RAvED Local Development Environment
SPRING_PROFILES_ACTIVE=local

# Database Configuration
DATABASE_USERNAME=raved_user
DATABASE_PASSWORD=raved_password

# Service Database URLs (Local)
USER_SERVICE_DB_URL=jdbc:postgresql://localhost:5432/raved_user_db
CONTENT_SERVICE_DB_URL=jdbc:postgresql://localhost:5432/raved_content_db
SOCIAL_SERVICE_DB_URL=jdbc:postgresql://localhost:5432/raved_social_db
REALTIME_SERVICE_DB_URL=jdbc:postgresql://localhost:5432/raved_realtime_db
ECOMMERCE_SERVICE_DB_URL=jdbc:postgresql://localhost:5432/raved_ecommerce_db
NOTIFICATION_SERVICE_DB_URL=jdbc:postgresql://localhost:5432/raved_notification_db
ANALYTICS_SERVICE_DB_URL=jdbc:postgresql://localhost:5432/raved_analytics_db

# Other Configuration
EUREKA_SERVER_URL=http://localhost:8761/eureka/
LOG_LEVEL=DEBUG
EOF
    echo -e "${GREEN}✅ Default .env file created${NC}"
}

# Function to show cloud setup instructions
show_cloud_setup_instructions() {
    echo -e "${CYAN}☁️  Cloud Setup Instructions${NC}"
    echo -e "${CYAN}=============================${NC}"
    echo ""
    echo -e "${YELLOW}For Staging/Production deployment with Neon:${NC}"
    echo ""
    echo -e "${BLUE}1. Create Neon Account:${NC}"
    echo -e "   Visit: https://console.neon.tech"
    echo -e "   Sign up and create a new project"
    echo ""
    echo -e "${BLUE}2. Create Databases:${NC}"
    echo -e "   Create 7 databases for each service:"
    echo -e "   - raved-user-[env]"
    echo -e "   - raved-content-[env]"
    echo -e "   - raved-social-[env]"
    echo -e "   - raved-realtime-[env]"
    echo -e "   - raved-ecommerce-[env]"
    echo -e "   - raved-notification-[env]"
    echo -e "   - raved-analytics-[env]"
    echo ""
    echo -e "${BLUE}3. Update Environment Variables:${NC}"
    echo -e "   Copy connection strings from Neon and update:"
    echo -e "   - .env.staging for staging environment"
    echo -e "   - .env.production for production environment"
    echo ""
    echo -e "${BLUE}4. Deploy to Cloud:${NC}"
    echo -e "   export SPRING_PROFILES_ACTIVE=staging"
    echo -e "   ./scripts/database/migrate.sh"
    echo -e "   ./scripts/database/seed.sh"
    echo ""
}

# Function to show next steps
show_next_steps() {
    echo -e "${GREEN}🎉 Hybrid Database Setup Complete!${NC}"
    echo -e "${GREEN}===================================${NC}"
    echo ""
    echo -e "${CYAN}✅ What's been set up:${NC}"
    echo -e "   • All service configurations updated for hybrid setup"
    echo -e "   • Local PostgreSQL databases created and migrated"
    echo -e "   • Initial data seeded"
    echo -e "   • Environment profiles configured (local/staging/production)"
    echo ""
    echo -e "${CYAN}🚀 Next Steps:${NC}"
    echo ""
    echo -e "${YELLOW}1. Start all microservices:${NC}"
    echo -e "   docker-compose up -d"
    echo ""
    echo -e "${YELLOW}2. Test the services:${NC}"
    echo -e "   curl http://localhost:8080/actuator/health"
    echo -e "   curl http://localhost:8081/actuator/health"
    echo ""
    echo -e "${YELLOW}3. Access the application:${NC}"
    echo -e "   • API Gateway: http://localhost:8080"
    echo -e "   • Eureka Dashboard: http://localhost:8761"
    echo -e "   • Service Health: http://localhost:808[1-7]/actuator/health"
    echo ""
    echo -e "${YELLOW}4. Switch environments:${NC}"
    echo -e "   • Local: export SPRING_PROFILES_ACTIVE=local"
    echo -e "   • Staging: export SPRING_PROFILES_ACTIVE=staging"
    echo -e "   • Production: export SPRING_PROFILES_ACTIVE=production"
    echo ""
    echo -e "${YELLOW}5. Database management:${NC}"
    echo -e "   • Check status: ./scripts/setup-databases.sh --status"
    echo -e "   • Re-run migrations: ./scripts/database/migrate.sh"
    echo -e "   • Re-seed data: ./scripts/database/seed.sh"
    echo ""
    echo -e "${CYAN}📚 Documentation:${NC}"
    echo -e "   • Database Setup: docs/DATABASE_SETUP.md"
    echo -e "   • Hybrid Configuration: docs/HYBRID_DATABASE_SETUP.md"
    echo ""
}

# Function to show environment status
show_environment_status() {
    echo -e "${CYAN}📊 Environment Status${NC}"
    echo -e "${CYAN}===================${NC}"
    echo -e "Current Profile: ${GREEN}${SPRING_PROFILES_ACTIVE:-local}${NC}"
    echo -e "Environment File: ${GREEN}.env${NC}"
    echo ""
    
    # Check database connectivity
    if command -v psql &> /dev/null; then
        echo -e "${BLUE}Database Connectivity:${NC}"
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
            if PGPASSWORD=raved_password psql -h localhost -p 5432 -U raved_user -d $db -c '\q' 2>/dev/null; then
                echo -e "   ${GREEN}✅ $db${NC}"
            else
                echo -e "   ${RED}❌ $db${NC}"
            fi
        done
    fi
    echo ""
}

# Main execution
main() {
    print_banner
    
    echo -e "${BLUE}🏁 Starting RAvED Hybrid Database Setup${NC}"
    echo -e "${BLUE}=======================================${NC}"
    echo ""
    
    check_prerequisites
    echo ""
    
    update_configurations
    echo ""
    
    setup_local_environment
    echo ""
    
    show_environment_status
    echo ""
    
    show_cloud_setup_instructions
    echo ""
    
    show_next_steps
}

# Handle script arguments
case "${1:-}" in
    --status)
        show_environment_status
        ;;
    --cloud-instructions)
        show_cloud_setup_instructions
        ;;
    --help)
        echo "RAvED Hybrid Database Setup Script"
        echo ""
        echo "Usage: $0 [OPTIONS]"
        echo ""
        echo "Options:"
        echo "  --status              Show current environment status"
        echo "  --cloud-instructions  Show cloud setup instructions"
        echo "  --help               Show this help message"
        echo ""
        echo "This script sets up a hybrid database configuration that supports"
        echo "both local development and cloud deployment."
        ;;
    *)
        main "$@"
        ;;
esac
