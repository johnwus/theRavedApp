#!/bin/bash

# Database Seeding Script for RAvED App
# This script seeds initial data into all microservice databases

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

echo -e "${BLUE}🌱 Seeding databases with initial data...${NC}"

# Function to execute SQL
execute_sql() {
    local database=$1
    local sql=$2
    
    PGPASSWORD=$POSTGRES_PASSWORD psql -h $POSTGRES_HOST -p $POSTGRES_PORT -U $POSTGRES_USER -d $database -c "$sql"
}

# Seed User Service Database
seed_user_service() {
    echo -e "${YELLOW}👥 Seeding user service data...${NC}"
    
    # Insert universities
    execute_sql "raved_user_db" "
    INSERT INTO universities (name, code, country, city, domain_suffix, is_active) VALUES
    ('University of Ghana', 'UG', 'Ghana', 'Accra', '@student.ug.edu.gh', true),
    ('Kwame Nkrumah University of Science and Technology', 'KNUST', 'Ghana', 'Kumasi', '@student.knust.edu.gh', true),
    ('University of Cape Coast', 'UCC', 'Ghana', 'Cape Coast', '@student.ucc.edu.gh', true),
    ('Ghana Institute of Management and Public Administration', 'GIMPA', 'Ghana', 'Accra', '@student.gimpa.edu.gh', true)
    ON CONFLICT (code) DO NOTHING;
    "
    
    # Insert faculties
    execute_sql "raved_user_db" "
    INSERT INTO faculties (university_id, name, code, description, color_code, is_active) VALUES
    (1, 'Faculty of Arts', 'ARTS', 'Faculty of Arts and Humanities', '#FF6B6B', true),
    (1, 'Faculty of Science', 'SCI', 'Faculty of Physical and Biological Sciences', '#4ECDC4', true),
    (1, 'Faculty of Engineering', 'ENG', 'Faculty of Engineering Sciences', '#45B7D1', true),
    (1, 'Faculty of Social Studies', 'SOC', 'Faculty of Social Studies', '#96CEB4', true),
    (2, 'College of Engineering', 'COE', 'College of Engineering', '#FFEAA7', true),
    (2, 'College of Science', 'COS', 'College of Science', '#DDA0DD', true),
    (2, 'College of Art and Built Environment', 'CABE', 'College of Art and Built Environment', '#98D8C8', true)
    ON CONFLICT (university_id, code) DO NOTHING;
    "
    
    # Insert departments
    execute_sql "raved_user_db" "
    INSERT INTO departments (faculty_id, name, code, is_active) VALUES
    (1, 'Department of English', 'ENG', true),
    (1, 'Department of History', 'HIST', true),
    (2, 'Department of Computer Science', 'CS', true),
    (2, 'Department of Mathematics', 'MATH', true),
    (3, 'Department of Electrical Engineering', 'EE', true),
    (3, 'Department of Civil Engineering', 'CE', true),
    (4, 'Department of Economics', 'ECON', true),
    (4, 'Department of Political Science', 'POLS', true)
    ON CONFLICT (faculty_id, code) DO NOTHING;
    "
    
    echo -e "${GREEN}✅ User service seeded${NC}"
}

# Seed Content Service Database
seed_content_service() {
    echo -e "${YELLOW}📝 Seeding content service data...${NC}"
    
    # Sample posts will be created by the application
    echo -e "${GREEN}✅ Content service ready (posts will be created by users)${NC}"
}

# Seed Social Service Database
seed_social_service() {
    echo -e "${YELLOW}👫 Seeding social service data...${NC}"
    
    # Social interactions will be created by users
    echo -e "${GREEN}✅ Social service ready (interactions will be created by users)${NC}"
}

# Seed Ecommerce Service Database
seed_ecommerce_service() {
    echo -e "${YELLOW}🛒 Seeding ecommerce service data...${NC}"
    
    # Insert product categories
    execute_sql "raved_ecommerce_db" "
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
    "
    
    # Insert subcategories
    execute_sql "raved_ecommerce_db" "
    INSERT INTO product_categories (name, description, parent_category_id, is_active) VALUES
    ('Dresses', 'Women dresses and gowns', 1, true),
    ('Shirts', 'Shirts and blouses', 1, true),
    ('Pants', 'Trousers and jeans', 1, true),
    ('Watches', 'Wristwatches and timepieces', 2, true),
    ('Jewelry', 'Necklaces, earrings, and rings', 2, true),
    ('Sneakers', 'Casual and sports sneakers', 3, true),
    ('Formal Shoes', 'Dress shoes and formal footwear', 3, true)
    ON CONFLICT (name) DO NOTHING;
    "
    
    echo -e "${GREEN}✅ Ecommerce service seeded${NC}"
}

# Seed Notification Service Database
seed_notification_service() {
    echo -e "${YELLOW}🔔 Seeding notification service data...${NC}"
    
    # Insert notification templates
    execute_sql "raved_notification_db" "
    INSERT INTO notification_templates (template_name, template_type, subject_template, body_template, variables, is_active) VALUES
    ('like_post', 'PUSH', NULL, '{{user_name}} liked your post', '{\"user_name\": \"string\", \"post_id\": \"number\"}', true),
    ('comment_post', 'PUSH', NULL, '{{user_name}} commented on your post', '{\"user_name\": \"string\", \"post_id\": \"number\"}', true),
    ('follow_user', 'PUSH', NULL, '{{user_name}} started following you', '{\"user_name\": \"string\", \"user_id\": \"number\"}', true),
    ('order_confirmed', 'PUSH', NULL, 'Your order #{{order_number}} has been confirmed', '{\"order_number\": \"string\"}', true),
    ('order_shipped', 'PUSH', NULL, 'Your order #{{order_number}} has been shipped', '{\"order_number\": \"string\", \"tracking_number\": \"string\"}', true),
    ('welcome_email', 'EMAIL', 'Welcome to RAvED!', 'Welcome {{user_name}} to the RAvED community!', '{\"user_name\": \"string\"}', true)
    ON CONFLICT (template_name) DO NOTHING;
    "
    
    echo -e "${GREEN}✅ Notification service seeded${NC}"
}

# Seed Analytics Service Database
seed_analytics_service() {
    echo -e "${YELLOW}📊 Seeding analytics service data...${NC}"
    
    # Analytics data will be generated by user interactions
    echo -e "${GREEN}✅ Analytics service ready (metrics will be generated by user activity)${NC}"
}

# Seed Realtime Service Database
seed_realtime_service() {
    echo -e "${YELLOW}💬 Seeding realtime service data...${NC}"
    
    # Chat rooms and messages will be created by users
    echo -e "${GREEN}✅ Realtime service ready (chats will be created by users)${NC}"
}

# Main execution
main() {
    echo -e "${BLUE}🚀 Starting Database Seeding${NC}"
    echo -e "${BLUE}=============================${NC}"
    
    seed_user_service
    seed_content_service
    seed_social_service
    seed_ecommerce_service
    seed_notification_service
    seed_analytics_service
    seed_realtime_service
    
    echo -e "${GREEN}🎉 All databases seeded successfully!${NC}"
    echo -e "${BLUE}=============================${NC}"
    echo -e "${GREEN}Your RAvED databases are ready with initial data${NC}"
}

# Run main function
main "$@"
