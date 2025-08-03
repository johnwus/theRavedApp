#!/bin/bash

# Simple Database Setup for RAvED
# Creates tables directly using SQL without requiring Maven/Flyway

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

print_banner() {
    echo -e "${PURPLE}"
    echo "╔══════════════════════════════════════════════════════════════╗"
    echo "║                Simple Database Setup                         ║"
    echo "║              Direct SQL Table Creation                       ║"
    echo "╚══════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

# Function to create tables for user service
create_user_service_tables() {
    echo -e "${YELLOW}👥 Creating user service tables...${NC}"
    
    docker exec workspace_postgres_1 psql -U raved_user -d raved_user_db << 'EOF'
-- Create universities table
CREATE TABLE IF NOT EXISTS universities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(10) NOT NULL UNIQUE,
    country VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    domain_suffix VARCHAR(100),
    logo_url TEXT,
    website_url TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create faculties table
CREATE TABLE IF NOT EXISTS faculties (
    id BIGSERIAL PRIMARY KEY,
    university_id BIGINT NOT NULL REFERENCES universities(id),
    name VARCHAR(255) NOT NULL,
    code VARCHAR(10) NOT NULL,
    description TEXT,
    color_code VARCHAR(7),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(university_id, code)
);

-- Create departments table
CREATE TABLE IF NOT EXISTS departments (
    id BIGSERIAL PRIMARY KEY,
    faculty_id BIGINT NOT NULL REFERENCES faculties(id),
    name VARCHAR(255) NOT NULL,
    code VARCHAR(10) NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(faculty_id, code)
);

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    profile_picture_url TEXT,
    bio TEXT,
    university_id BIGINT REFERENCES universities(id),
    faculty_id BIGINT REFERENCES faculties(id),
    department_id BIGINT REFERENCES departments(id),
    student_id VARCHAR(50),
    graduation_year INTEGER,
    is_verified BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    last_login_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_university ON users(university_id);
CREATE INDEX IF NOT EXISTS idx_faculties_university ON faculties(university_id);
CREATE INDEX IF NOT EXISTS idx_departments_faculty ON departments(faculty_id);
EOF

    echo -e "${GREEN}✅ User service tables created${NC}"
}

# Function to create tables for content service
create_content_service_tables() {
    echo -e "${YELLOW}📝 Creating content service tables...${NC}"
    
    docker exec workspace_postgres_1 psql -U raved_user -d raved_content_db << 'EOF'
-- Create posts table
CREATE TABLE IF NOT EXISTS posts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    post_type VARCHAR(20) DEFAULT 'TEXT',
    visibility VARCHAR(20) DEFAULT 'PUBLIC',
    likes_count INTEGER DEFAULT 0,
    comments_count INTEGER DEFAULT 0,
    shares_count INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create media_files table
CREATE TABLE IF NOT EXISTS media_files (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT REFERENCES posts(id) ON DELETE CASCADE,
    file_url TEXT NOT NULL,
    file_type VARCHAR(20) NOT NULL,
    file_size BIGINT,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create post_tags table
CREATE TABLE IF NOT EXISTS post_tags (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    tag_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_posts_user ON posts(user_id);
CREATE INDEX IF NOT EXISTS idx_posts_created_at ON posts(created_at);
CREATE INDEX IF NOT EXISTS idx_media_files_post ON media_files(post_id);
CREATE INDEX IF NOT EXISTS idx_post_tags_post ON post_tags(post_id);
CREATE INDEX IF NOT EXISTS idx_post_tags_tag ON post_tags(tag_name);
EOF

    echo -e "${GREEN}✅ Content service tables created${NC}"
}

# Function to create tables for social service
create_social_service_tables() {
    echo -e "${YELLOW}👫 Creating social service tables...${NC}"
    
    docker exec workspace_postgres_1 psql -U raved_user -d raved_social_db << 'EOF'
-- Create likes table
CREATE TABLE IF NOT EXISTS likes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    target_id BIGINT NOT NULL,
    target_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, target_id, target_type)
);

-- Create comments table
CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    parent_comment_id BIGINT REFERENCES comments(id),
    content TEXT NOT NULL,
    likes_count INTEGER DEFAULT 0,
    replies_count INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create follows table
CREATE TABLE IF NOT EXISTS follows (
    id BIGSERIAL PRIMARY KEY,
    follower_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(follower_id, following_id)
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_likes_user ON likes(user_id);
CREATE INDEX IF NOT EXISTS idx_likes_target ON likes(target_id, target_type);
CREATE INDEX IF NOT EXISTS idx_comments_post ON comments(post_id);
CREATE INDEX IF NOT EXISTS idx_comments_user ON comments(user_id);
CREATE INDEX IF NOT EXISTS idx_follows_follower ON follows(follower_id);
CREATE INDEX IF NOT EXISTS idx_follows_following ON follows(following_id);
EOF

    echo -e "${GREEN}✅ Social service tables created${NC}"
}

# Function to create tables for realtime service
create_realtime_service_tables() {
    echo -e "${YELLOW}💬 Creating realtime service tables...${NC}"
    
    docker exec workspace_postgres_1 psql -U raved_user -d raved_realtime_db << 'EOF'
-- Create chat_rooms table
CREATE TABLE IF NOT EXISTS chat_rooms (
    id BIGSERIAL PRIMARY KEY,
    room_type VARCHAR(20) NOT NULL,
    name VARCHAR(255),
    description TEXT,
    avatar_url TEXT,
    created_by BIGINT NOT NULL,
    faculty_id BIGINT,
    is_active BOOLEAN DEFAULT true,
    last_message_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create chat_room_members table
CREATE TABLE IF NOT EXISTS chat_room_members (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL REFERENCES chat_rooms(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL,
    role VARCHAR(20) DEFAULT 'MEMBER',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_read_at TIMESTAMP,
    is_muted BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    UNIQUE(room_id, user_id)
);

-- Create messages table
CREATE TABLE IF NOT EXISTS messages (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL REFERENCES chat_rooms(id),
    sender_id BIGINT NOT NULL,
    message_type VARCHAR(20) DEFAULT 'TEXT',
    content TEXT,
    media_url TEXT,
    media_metadata JSONB,
    reply_to_message_id BIGINT REFERENCES messages(id),
    is_edited BOOLEAN DEFAULT false,
    is_deleted BOOLEAN DEFAULT false,
    edited_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_chat_rooms_type ON chat_rooms(room_type);
CREATE INDEX IF NOT EXISTS idx_chat_members_room ON chat_room_members(room_id);
CREATE INDEX IF NOT EXISTS idx_messages_room ON messages(room_id);
CREATE INDEX IF NOT EXISTS idx_messages_created_at ON messages(created_at);
EOF

    echo -e "${GREEN}✅ Realtime service tables created${NC}"
}

# Function to create tables for ecommerce service
create_ecommerce_service_tables() {
    echo -e "${YELLOW}🛒 Creating ecommerce service tables...${NC}"
    
    docker exec workspace_postgres_1 psql -U raved_user -d raved_ecommerce_db << 'EOF'
-- Create product_categories table
CREATE TABLE IF NOT EXISTS product_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    parent_category_id BIGINT REFERENCES product_categories(id),
    icon_url TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create products table
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    post_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    category_id BIGINT REFERENCES product_categories(id),
    brand VARCHAR(100),
    size VARCHAR(20),
    color VARCHAR(50),
    condition VARCHAR(20) DEFAULT 'NEW',
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    currency VARCHAR(3) DEFAULT 'GHS',
    is_negotiable BOOLEAN DEFAULT false,
    quantity INTEGER DEFAULT 1,
    is_available BOOLEAN DEFAULT true,
    shipping_cost DECIMAL(10,2) DEFAULT 0,
    ships_from VARCHAR(255),
    processing_days INTEGER DEFAULT 1,
    views_count INTEGER DEFAULT 0,
    likes_count INTEGER DEFAULT 0,
    is_featured BOOLEAN DEFAULT false,
    featured_until TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    buyer_id BIGINT NOT NULL,
    seller_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    subtotal DECIMAL(10,2) NOT NULL,
    shipping_cost DECIMAL(10,2) DEFAULT 0,
    tax_amount DECIMAL(10,2) DEFAULT 0,
    total_amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'GHS',
    shipping_address JSONB NOT NULL,
    billing_address JSONB,
    tracking_number VARCHAR(100),
    estimated_delivery DATE,
    ordered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP,
    shipped_at TIMESTAMP,
    delivered_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_products_seller ON products(seller_id);
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_orders_buyer ON orders(buyer_id);
CREATE INDEX IF NOT EXISTS idx_orders_seller ON orders(seller_id);
EOF

    echo -e "${GREEN}✅ Ecommerce service tables created${NC}"
}

# Function to create tables for notification service
create_notification_service_tables() {
    echo -e "${YELLOW}🔔 Creating notification service tables...${NC}"
    
    docker exec workspace_postgres_1 psql -U raved_user -d raved_notification_db << 'EOF'
-- Create notification_templates table
CREATE TABLE IF NOT EXISTS notification_templates (
    id BIGSERIAL PRIMARY KEY,
    template_name VARCHAR(100) NOT NULL UNIQUE,
    template_type VARCHAR(20) NOT NULL,
    subject_template TEXT,
    body_template TEXT NOT NULL,
    variables JSONB,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create notifications table
CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    data JSONB,
    action_url TEXT,
    image_url TEXT,
    is_read BOOLEAN DEFAULT false,
    is_sent BOOLEAN DEFAULT false,
    delivery_status VARCHAR(20) DEFAULT 'PENDING',
    push_sent BOOLEAN DEFAULT false,
    email_sent BOOLEAN DEFAULT false,
    sms_sent BOOLEAN DEFAULT false,
    scheduled_at TIMESTAMP,
    sent_at TIMESTAMP,
    read_at TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create device_tokens table
CREATE TABLE IF NOT EXISTS device_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(500) NOT NULL,
    platform VARCHAR(20) NOT NULL,
    device_info JSONB,
    is_active BOOLEAN DEFAULT true,
    last_used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, token)
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_notifications_user ON notifications(user_id);
CREATE INDEX IF NOT EXISTS idx_notifications_type ON notifications(notification_type);
CREATE INDEX IF NOT EXISTS idx_device_tokens_user ON device_tokens(user_id);
EOF

    echo -e "${GREEN}✅ Notification service tables created${NC}"
}

# Function to create tables for analytics service
create_analytics_service_tables() {
    echo -e "${YELLOW}📊 Creating analytics service tables...${NC}"
    
    docker exec workspace_postgres_1 psql -U raved_user -d raved_analytics_db << 'EOF'
-- Create analytics_events table
CREATE TABLE IF NOT EXISTS analytics_events (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    session_id VARCHAR(255),
    event_name VARCHAR(100) NOT NULL,
    event_category VARCHAR(50),
    properties JSONB,
    user_properties JSONB,
    platform VARCHAR(20),
    app_version VARCHAR(20),
    device_info JSONB,
    location_info JSONB,
    event_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    server_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create user_metrics table
CREATE TABLE IF NOT EXISTS user_metrics (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    posts_count INTEGER DEFAULT 0,
    likes_given INTEGER DEFAULT 0,
    likes_received INTEGER DEFAULT 0,
    comments_given INTEGER DEFAULT 0,
    comments_received INTEGER DEFAULT 0,
    followers_count INTEGER DEFAULT 0,
    following_count INTEGER DEFAULT 0,
    connections_count INTEGER DEFAULT 0,
    products_sold INTEGER DEFAULT 0,
    products_bought INTEGER DEFAULT 0,
    total_sales_amount DECIMAL(10,2) DEFAULT 0,
    total_purchases_amount DECIMAL(10,2) DEFAULT 0,
    login_streak INTEGER DEFAULT 0,
    total_sessions INTEGER DEFAULT 0,
    total_time_spent BIGINT DEFAULT 0,
    engagement_score DECIMAL(5,2) DEFAULT 0,
    influence_score DECIMAL(5,2) DEFAULT 0,
    last_calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create content_metrics table
CREATE TABLE IF NOT EXISTS content_metrics (
    id BIGSERIAL PRIMARY KEY,
    content_id BIGINT NOT NULL,
    content_type VARCHAR(20) NOT NULL,
    views_count INTEGER DEFAULT 0,
    likes_count INTEGER DEFAULT 0,
    comments_count INTEGER DEFAULT 0,
    shares_count INTEGER DEFAULT 0,
    saves_count INTEGER DEFAULT 0,
    engagement_rate DECIMAL(5,2) DEFAULT 0,
    viral_score DECIMAL(5,2) DEFAULT 0,
    reach INTEGER DEFAULT 0,
    impressions INTEGER DEFAULT 0,
    conversion_rate DECIMAL(5,2),
    revenue_generated DECIMAL(10,2),
    peak_engagement_time TIMESTAMP,
    metrics_date DATE DEFAULT CURRENT_DATE,
    last_calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(content_id, content_type, metrics_date)
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_analytics_events_user ON analytics_events(user_id);
CREATE INDEX IF NOT EXISTS idx_analytics_events_name ON analytics_events(event_name);
CREATE INDEX IF NOT EXISTS idx_analytics_events_time ON analytics_events(event_time);
CREATE INDEX IF NOT EXISTS idx_content_metrics_type ON content_metrics(content_type);
EOF

    echo -e "${GREEN}✅ Analytics service tables created${NC}"
}

# Function to seed initial data
seed_initial_data() {
    echo -e "${BLUE}🌱 Seeding initial data...${NC}"
    
    # Seed universities
    docker exec workspace_postgres_1 psql -U raved_user -d raved_user_db << 'EOF'
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

    # Seed product categories
    docker exec workspace_postgres_1 psql -U raved_user -d raved_ecommerce_db << 'EOF'
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

    # Seed notification templates
    docker exec workspace_postgres_1 psql -U raved_user -d raved_notification_db << 'EOF'
INSERT INTO notification_templates (template_name, template_type, subject_template, body_template, variables, is_active) VALUES
('like_post', 'PUSH', NULL, '{{user_name}} liked your post', '{"user_name": "string", "post_id": "number"}', true),
('comment_post', 'PUSH', NULL, '{{user_name}} commented on your post', '{"user_name": "string", "post_id": "number"}', true),
('follow_user', 'PUSH', NULL, '{{user_name}} started following you', '{"user_name": "string", "user_id": "number"}', true),
('order_confirmed', 'PUSH', NULL, 'Your order #{{order_number}} has been confirmed', '{"order_number": "string"}', true),
('order_shipped', 'PUSH', NULL, 'Your order #{{order_number}} has been shipped', '{"order_number": "string", "tracking_number": "string"}', true),
('welcome_email', 'EMAIL', 'Welcome to RAvED!', 'Welcome {{user_name}} to the RAvED community!', '{"user_name": "string"}', true)
ON CONFLICT (template_name) DO NOTHING;
EOF

    echo -e "${GREEN}✅ Initial data seeded${NC}"
}

# Function to show table counts
show_table_counts() {
    echo -e "${BLUE}📊 Database table counts:${NC}"
    
    declare -A DATABASES=(
        ["User Service"]="raved_user_db"
        ["Content Service"]="raved_content_db"
        ["Social Service"]="raved_social_db"
        ["Realtime Service"]="raved_realtime_db"
        ["Ecommerce Service"]="raved_ecommerce_db"
        ["Notification Service"]="raved_notification_db"
        ["Analytics Service"]="raved_analytics_db"
    )
    
    for service in "${!DATABASES[@]}"; do
        local database=${DATABASES[$service]}
        local count=$(docker exec workspace_postgres_1 psql -U raved_user -d $database -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public';" 2>/dev/null | xargs)
        echo -e "${GREEN}  $service: $count tables${NC}"
    done
}

# Main execution
main() {
    print_banner
    
    echo -e "${BLUE}🚀 Starting Simple Database Setup${NC}"
    echo -e "${BLUE}==================================${NC}"
    
    create_user_service_tables
    create_content_service_tables
    create_social_service_tables
    create_realtime_service_tables
    create_ecommerce_service_tables
    create_notification_service_tables
    create_analytics_service_tables
    
    echo ""
    seed_initial_data
    
    echo ""
    show_table_counts
    
    echo ""
    echo -e "${GREEN}🎉 Simple database setup completed!${NC}"
    echo -e "${GREEN}All tables created and initial data seeded.${NC}"
    echo -e "${GREEN}Your databases are ready for pgAdmin connection!${NC}"
    
    echo ""
    echo -e "${YELLOW}pgAdmin Connection Details:${NC}"
    echo -e "${YELLOW}Host: localhost${NC}"
    echo -e "${YELLOW}Port: 5432${NC}"
    echo -e "${YELLOW}Username: raved_user${NC}"
    echo -e "${YELLOW}Password: raved_password${NC}"
}

# Run main function
main "$@"
