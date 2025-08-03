#!/bin/bash

# Docker-based Database Setup for RAvED
# This script uses Docker containers to run migrations without installing tools locally

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Configuration
POSTGRES_HOST=postgres
POSTGRES_PORT=5432
POSTGRES_USER=raved_user
POSTGRES_PASSWORD=raved_password

# Service configurations
declare -A SERVICES=(
    ["user-service"]="raved_user_db"
    ["content-service"]="raved_content_db"
    ["social-service"]="raved_social_db"
    ["realtime-service"]="raved_realtime_db"
    ["ecommerce-service"]="raved_ecommerce_db"
    ["notification-service"]="raved_notification_db"
    ["analytics-service"]="raved_analytics_db"
)

print_banner() {
    echo -e "${PURPLE}"
    echo "╔══════════════════════════════════════════════════════════════╗"
    echo "║              Docker-based Database Setup                     ║"
    echo "║                  No Local Tools Required                     ║"
    echo "╚══════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

# Function to test database connections
test_connections() {
    echo -e "${BLUE}🔍 Testing database connections...${NC}"
    
    for service in "${!SERVICES[@]}"; do
        local database=${SERVICES[$service]}
        
        if docker exec workspace_postgres_1 psql -U $POSTGRES_USER -d $database -c '\q' 2>/dev/null; then
            echo -e "${GREEN}✅ $database - Connected${NC}"
        else
            echo -e "${RED}❌ $database - Connection failed${NC}"
            return 1
        fi
    done
    
    echo -e "${GREEN}✅ All databases are accessible${NC}"
    return 0
}

# Function to run migrations using Docker
run_migrations_docker() {
    echo -e "${BLUE}🔄 Running migrations using Docker...${NC}"
    
    # Create a temporary Maven container to run migrations
    for service in "${!SERVICES[@]}"; do
        local database=${SERVICES[$service]}
        echo -e "${YELLOW}📦 Running migrations for $service -> $database${NC}"
        
        if [ -d "server/$service" ]; then
            # Use Maven Docker image to run Flyway migrations
            docker run --rm \
                --network workspace_raved-network \
                -v "$(pwd)/server/$service:/app" \
                -w /app \
                -e FLYWAY_URL="jdbc:postgresql://$POSTGRES_HOST:$POSTGRES_PORT/$database" \
                -e FLYWAY_USER="$POSTGRES_USER" \
                -e FLYWAY_PASSWORD="$POSTGRES_PASSWORD" \
                maven:3.9-openjdk-17-slim \
                mvn flyway:migrate \
                    -Dflyway.url="jdbc:postgresql://$POSTGRES_HOST:$POSTGRES_PORT/$database" \
                    -Dflyway.user="$POSTGRES_USER" \
                    -Dflyway.password="$POSTGRES_PASSWORD" \
                    -Dflyway.locations="classpath:db/migration" \
                    -Dflyway.baselineOnMigrate=true \
                    -q
            
            if [ $? -eq 0 ]; then
                echo -e "${GREEN}✅ Migrations completed for $service${NC}"
            else
                echo -e "${RED}❌ Migrations failed for $service${NC}"
            fi
        else
            echo -e "${RED}❌ Service directory server/$service not found${NC}"
        fi
        echo ""
    done
}

# Function to seed databases using Docker
seed_databases_docker() {
    echo -e "${BLUE}🌱 Seeding databases using Docker...${NC}"
    
    # Seed User Service Database
    echo -e "${YELLOW}👥 Seeding user service data...${NC}"
    docker exec workspace_postgres_1 psql -U $POSTGRES_USER -d raved_user_db << 'EOF'
INSERT INTO universities (name, code, country, city, domain_suffix, is_active) VALUES
('University of Ghana', 'UG', 'Ghana', 'Accra', '@student.ug.edu.gh', true),
('Kwame Nkrumah University of Science and Technology', 'KNUST', 'Ghana', 'Kumasi', '@student.knust.edu.gh', true),
('University of Cape Coast', 'UCC', 'Ghana', 'Cape Coast', '@student.ucc.edu.gh', true),
('Ghana Institute of Management and Public Administration', 'GIMPA', 'Ghana', 'Accra', '@student.gimpa.edu.gh', true)
ON CONFLICT (code) DO NOTHING;

INSERT INTO faculties (university_id, name, code, description, color_code, is_active) VALUES
(1, 'Faculty of Arts', 'ARTS', 'Faculty of Arts and Humanities', '#FF6B6B', true),
(1, 'Faculty of Science', 'SCI', 'Faculty of Physical and Biological Sciences', '#4ECDC4', true),
(1, 'Faculty of Engineering', 'ENG', 'Faculty of Engineering Sciences', '#45B7D1', true),
(1, 'Faculty of Social Studies', 'SOC', 'Faculty of Social Studies', '#96CEB4', true),
(2, 'College of Engineering', 'COE', 'College of Engineering', '#FFEAA7', true),
(2, 'College of Science', 'COS', 'College of Science', '#DDA0DD', true),
(2, 'College of Art and Built Environment', 'CABE', 'College of Art and Built Environment', '#98D8C8', true)
ON CONFLICT (university_id, code) DO NOTHING;
EOF

    # Seed Ecommerce Service Database
    echo -e "${YELLOW}🛒 Seeding ecommerce service data...${NC}"
    docker exec workspace_postgres_1 psql -U $POSTGRES_USER -d raved_ecommerce_db << 'EOF'
INSERT INTO product_categories (name, description, is_active) VALUES
('Clothing', 'Fashion and clothing items', true),
('Accessories', 'Fashion accessories and jewelry', true),
('Shoes', 'Footwear and shoes', true),
('Bags', 'Handbags, backpacks, and luggage', true),
('Electronics', 'Electronic devices and gadgets', true),
('Books', 'Textbooks and educational materials', true),
('Beauty', 'Beauty and personal care products', true),
('Sports', 'Sports and fitness equipment', true)
ON CONFLICT (name) DO NOTHING;
EOF

    # Seed Notification Service Database
    echo -e "${YELLOW}🔔 Seeding notification service data...${NC}"
    docker exec workspace_postgres_1 psql -U $POSTGRES_USER -d raved_notification_db << 'EOF'
INSERT INTO notification_templates (template_name, template_type, subject_template, body_template, variables, is_active) VALUES
('like_post', 'PUSH', NULL, '{{user_name}} liked your post', '{"user_name": "string", "post_id": "number"}', true),
('comment_post', 'PUSH', NULL, '{{user_name}} commented on your post', '{"user_name": "string", "post_id": "number"}', true),
('follow_user', 'PUSH', NULL, '{{user_name}} started following you', '{"user_name": "string", "user_id": "number"}', true),
('order_confirmed', 'PUSH', NULL, 'Your order #{{order_number}} has been confirmed', '{"order_number": "string"}', true),
('order_shipped', 'PUSH', NULL, 'Your order #{{order_number}} has been shipped', '{"order_number": "string", "tracking_number": "string"}', true),
('welcome_email', 'EMAIL', 'Welcome to RAvED!', 'Welcome {{user_name}} to the RAvED community!', '{"user_name": "string"}', true)
ON CONFLICT (template_name) DO NOTHING;
EOF

    echo -e "${GREEN}✅ Database seeding completed${NC}"
}

# Function to show table counts
show_table_counts() {
    echo -e "${BLUE}📊 Database table counts:${NC}"
    
    for service in "${!SERVICES[@]}"; do
        local database=${SERVICES[$service]}
        local count=$(docker exec workspace_postgres_1 psql -U $POSTGRES_USER -d $database -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public';" 2>/dev/null | xargs)
        echo -e "${GREEN}  $database: $count tables${NC}"
    done
}

# Main execution
main() {
    print_banner
    
    echo -e "${BLUE}🚀 Starting Docker-based Database Setup${NC}"
    echo -e "${BLUE}======================================${NC}"
    
    # Test connections
    if ! test_connections; then
        echo -e "${RED}❌ Database connections failed. Please ensure PostgreSQL is running.${NC}"
        exit 1
    fi
    
    echo ""
    
    # Run migrations
    run_migrations_docker
    
    echo ""
    
    # Seed databases
    seed_databases_docker
    
    echo ""
    
    # Show results
    show_table_counts
    
    echo ""
    echo -e "${GREEN}🎉 Docker-based database setup completed!${NC}"
    echo -e "${GREEN}Your databases are ready for pgAdmin connection.${NC}"
}

# Run main function
main "$@"
