# RAvED Database Model & Entities

## Database Architecture Overview

Each microservice has its own database with clear boundaries. Cross-service data access happens through APIs, not direct database connections.

## 1. USER SERVICE DATABASE (user_service_db)

### Universities Table
```sql
CREATE TABLE universities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(10) NOT NULL UNIQUE,
    country VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    domain_suffix VARCHAR(50), -- e.g., "@student.ug.edu.gh"
    logo_url TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Faculties Table
```sql
CREATE TABLE faculties (
    id BIGSERIAL PRIMARY KEY,
    university_id BIGINT NOT NULL REFERENCES universities(id),
    name VARCHAR(255) NOT NULL,
    code VARCHAR(20) NOT NULL,
    description TEXT,
    color_code VARCHAR(7), -- hex color for UI theming
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(university_id, code)
);
```

### Departments Table
```sql
CREATE TABLE departments (
    id BIGSERIAL PRIMARY KEY,
    faculty_id BIGINT NOT NULL REFERENCES faculties(id),
    name VARCHAR(255) NOT NULL,
    code VARCHAR(20) NOT NULL,
    head_of_department VARCHAR(255),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(faculty_id, code)
);
```

### Users Table
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    
    -- Personal Information
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    display_name VARCHAR(100),
    bio TEXT,
    date_of_birth DATE,
    gender VARCHAR(20),
    phone_number VARCHAR(20),
    
    -- Academic Information
    university_id BIGINT NOT NULL REFERENCES universities(id),
    faculty_id BIGINT NOT NULL REFERENCES faculties(id),
    department_id BIGINT REFERENCES departments(id),
    academic_year INTEGER,
    graduation_year INTEGER,
    
    -- Profile Information
    profile_picture_url TEXT,
    cover_photo_url TEXT,
    location VARCHAR(255),
    website_url TEXT,
    
    -- Privacy Settings
    is_profile_public BOOLEAN DEFAULT true,
    allow_messages_from_strangers BOOLEAN DEFAULT true,
    show_email BOOLEAN DEFAULT false,
    show_phone BOOLEAN DEFAULT false,
    
    -- System Fields
    email_verified BOOLEAN DEFAULT false,
    student_id_verified BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    last_login_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Student Verifications Table
```sql
CREATE TABLE student_verifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    student_id_document_url TEXT NOT NULL,
    verification_status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED
    verified_by BIGINT REFERENCES users(id),
    verification_notes TEXT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### User Sessions Table
```sql
CREATE TABLE user_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    session_token VARCHAR(255) NOT NULL UNIQUE,
    device_info JSONB,
    ip_address INET,
    user_agent TEXT,
    is_active BOOLEAN DEFAULT true,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 2. CONTENT SERVICE DATABASE (content_service_db)

### Posts Table
```sql
CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Reference to user service
    content TEXT NOT NULL,
    
    -- Content Classification
    post_type VARCHAR(20) DEFAULT 'OUTFIT', -- OUTFIT, GENERAL, POLL, EVENT
    visibility VARCHAR(20) DEFAULT 'PUBLIC', -- PUBLIC, FACULTY_ONLY, FOLLOWERS_ONLY, PRIVATE
    faculty_id BIGINT, -- Reference to user service
    
    -- Engagement Metrics (denormalized for performance)
    likes_count INTEGER DEFAULT 0,
    comments_count INTEGER DEFAULT 0,
    shares_count INTEGER DEFAULT 0,
    views_count INTEGER DEFAULT 0,
    
    -- Content Moderation
    is_flagged BOOLEAN DEFAULT false,
    moderation_status VARCHAR(20) DEFAULT 'APPROVED', -- PENDING, APPROVED, REJECTED
    flagged_reason TEXT,
    
    -- System Fields
    is_deleted BOOLEAN DEFAULT false,
    is_featured BOOLEAN DEFAULT false,
    featured_until TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Indexes
    INDEX idx_posts_user_id (user_id),
    INDEX idx_posts_faculty_id (faculty_id),
    INDEX idx_posts_created_at (created_at),
    INDEX idx_posts_featured (is_featured, featured_until),
    INDEX idx_posts_visibility (visibility)
);
```

### Media Files Table
```sql
CREATE TABLE media_files (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    file_url TEXT NOT NULL,
    thumbnail_url TEXT,
    file_type VARCHAR(20) NOT NULL, -- IMAGE, VIDEO, AUDIO
    file_size BIGINT,
    width INTEGER,
    height INTEGER,
    duration INTEGER, -- for videos/audio in seconds
    file_format VARCHAR(20), -- jpg, png, mp4, etc.
    display_order INTEGER DEFAULT 0,
    alt_text TEXT,
    is_primary BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_media_files_post_id (post_id),
    INDEX idx_media_files_type (file_type)
);
```

### Post Tags Table
```sql
CREATE TABLE post_tags (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    tag_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE(post_id, tag_name),
    INDEX idx_post_tags_name (tag_name),
    INDEX idx_post_tags_post_id (post_id)
);
```

### Post Mentions Table
```sql
CREATE TABLE post_mentions (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    mentioned_user_id BIGINT NOT NULL, -- Reference to user service
    mention_type VARCHAR(20) DEFAULT 'USER', -- USER, FACULTY, DEPARTMENT
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE(post_id, mentioned_user_id),
    INDEX idx_post_mentions_user (mentioned_user_id)
);
```

## 3. SOCIAL SERVICE DATABASE (social_service_db)

### Likes Table
```sql
CREATE TABLE likes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Reference to user service
    target_id BIGINT NOT NULL, -- post_id or comment_id
    target_type VARCHAR(20) NOT NULL, -- POST, COMMENT
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE(user_id, target_id, target_type),
    INDEX idx_likes_target (target_id, target_type),
    INDEX idx_likes_user (user_id),
    INDEX idx_likes_created_at (created_at)
);
```

### Comments Table
```sql
CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL, -- Reference to content service
    user_id BIGINT NOT NULL, -- Reference to user service
    parent_comment_id BIGINT REFERENCES comments(id), -- For threaded comments
    content TEXT NOT NULL,
    
    -- Engagement Metrics
    likes_count INTEGER DEFAULT 0,
    replies_count INTEGER DEFAULT 0,
    
    -- Content Moderation
    is_flagged BOOLEAN DEFAULT false,
    moderation_status VARCHAR(20) DEFAULT 'APPROVED',
    
    -- System Fields
    is_deleted BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_comments_post_id (post_id),
    INDEX idx_comments_user_id (user_id),
    INDEX idx_comments_parent (parent_comment_id),
    INDEX idx_comments_created_at (created_at)
);
```

### Follows Table
```sql
CREATE TABLE follows (
    id BIGSERIAL PRIMARY KEY,
    follower_id BIGINT NOT NULL, -- Reference to user service
    following_id BIGINT NOT NULL, -- Reference to user service
    follow_type VARCHAR(20) DEFAULT 'USER', -- USER, FACULTY
    is_mutual BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE(follower_id, following_id, follow_type),
    INDEX idx_follows_follower (follower_id),
    INDEX idx_follows_following (following_id),
    INDEX idx_follows_mutual (is_mutual)
);
```

### User Connections Table
```sql
CREATE TABLE user_connections (
    id BIGSERIAL PRIMARY KEY,
    requester_id BIGINT NOT NULL, -- Reference to user service
    addressee_id BIGINT NOT NULL, -- Reference to user service
    connection_status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, ACCEPTED, BLOCKED
    connection_type VARCHAR(20) DEFAULT 'FRIEND', -- FRIEND, STUDY_BUDDY, ROOMMATE
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE(requester_id, addressee_id),
    INDEX idx_connections_requester (requester_id),
    INDEX idx_connections_addressee (addressee_id),
    INDEX idx_connections_status (connection_status)
);
```

### Activities Table
```sql
CREATE TABLE activities (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Reference to user service
    target_user_id BIGINT, -- Reference to user service (optional)
    activity_type VARCHAR(50) NOT NULL, -- LIKE_POST, COMMENT_POST, FOLLOW_USER, etc.
    target_id BIGINT, -- post_id, comment_id, etc.
    target_type VARCHAR(20), -- POST, COMMENT, USER
    metadata JSONB, -- Additional activity data
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_activities_user_id (user_id),
    INDEX idx_activities_target_user (target_user_id),
    INDEX idx_activities_type (activity_type),
    INDEX idx_activities_created_at (created_at)
);
```

## 4. REALTIME SERVICE DATABASE (realtime_service_db)

### Chat Rooms Table
```sql
CREATE TABLE chat_rooms (
    id BIGSERIAL PRIMARY KEY,
    room_type VARCHAR(20) NOT NULL, -- DIRECT, GROUP, FACULTY_GROUP
    name VARCHAR(255), -- null for direct messages
    description TEXT,
    avatar_url TEXT,
    created_by BIGINT NOT NULL, -- Reference to user service
    faculty_id BIGINT, -- Reference to user service (for faculty groups)
    is_active BOOLEAN DEFAULT true,
    last_message_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_chat_rooms_type (room_type),
    INDEX idx_chat_rooms_faculty (faculty_id),
    INDEX idx_chat_rooms_last_message (last_message_at)
);
```

### Chat Room Members Table
```sql
CREATE TABLE chat_room_members (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL REFERENCES chat_rooms(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL, -- Reference to user service
    role VARCHAR(20) DEFAULT 'MEMBER', -- ADMIN, MODERATOR, MEMBER
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_read_at TIMESTAMP,
    is_muted BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    
    UNIQUE(room_id, user_id),
    INDEX idx_chat_members_room (room_id),
    INDEX idx_chat_members_user (user_id)
);
```

### Messages Table
```sql
CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL REFERENCES chat_rooms(id),
    sender_id BIGINT NOT NULL, -- Reference to user service
    message_type VARCHAR(20) DEFAULT 'TEXT', -- TEXT, IMAGE, VIDEO, AUDIO, FILE, SYSTEM
    content TEXT,
    media_url TEXT,
    media_metadata JSONB, -- file size, dimensions, duration, etc.
    reply_to_message_id BIGINT REFERENCES messages(id),
    
    -- System fields
    is_edited BOOLEAN DEFAULT false,
    is_deleted BOOLEAN DEFAULT false,
    edited_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_messages_room_id (room_id),
    INDEX idx_messages_sender_id (sender_id),
    INDEX idx_messages_created_at (created_at),
    INDEX idx_messages_reply_to (reply_to_message_id)
);
```

### Message Reactions Table
```sql
CREATE TABLE message_reactions (
    id BIGSERIAL PRIMARY KEY,
    message_id BIGINT NOT NULL REFERENCES messages(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL, -- Reference to user service
    reaction_type VARCHAR(20) NOT NULL, -- LIKE, LOVE, LAUGH, ANGRY, etc.
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE(message_id, user_id, reaction_type),
    INDEX idx_message_reactions_message (message_id),
    INDEX idx_message_reactions_user (user_id)
);
```

## 5. ECOMMERCE SERVICE DATABASE (ecommerce_service_db)

### Product Categories Table
```sql
CREATE TABLE product_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    parent_category_id BIGINT REFERENCES product_categories(id),
    icon_url TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Products Table
```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    seller_id BIGINT NOT NULL, -- Reference to user service
    post_id BIGINT, -- Reference to content service (linked outfit post)
    
    -- Product Information
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    category_id BIGINT REFERENCES product_categories(id),
    brand VARCHAR(100),
    size VARCHAR(20),
    color VARCHAR(50),
    condition VARCHAR(20) DEFAULT 'NEW', -- NEW, LIKE_NEW, GOOD, FAIR
    
    -- Pricing
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    currency VARCHAR(3) DEFAULT 'GHS',
    is_negotiable BOOLEAN DEFAULT false,
    
    -- Inventory
    quantity INTEGER DEFAULT 1,
    is_available BOOLEAN DEFAULT true,
    
    -- Shipping
    shipping_cost DECIMAL(10,2) DEFAULT 0,
    ships_from VARCHAR(255),
    processing_days INTEGER DEFAULT 1,
    
    -- Analytics
    views_count INTEGER DEFAULT 0,
    likes_count INTEGER DEFAULT 0,
    
    -- System Fields
    is_featured BOOLEAN DEFAULT false,
    featured_until TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_products_seller (seller_id),
    INDEX idx_products_category (category_id),
    INDEX idx_products_price (price),
    INDEX idx_products_featured (is_featured, featured_until),
    INDEX idx_products_created_at (created_at)
);
```

### Product Images Table
```sql
CREATE TABLE product_images (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    thumbnail_url TEXT,
    alt_text TEXT,
    display_order INTEGER DEFAULT 0,
    is_primary BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_product_images_product (product_id)
);
```

### Orders Table
```sql
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    buyer_id BIGINT NOT NULL, -- Reference to user service
    seller_id BIGINT NOT NULL, -- Reference to user service
    
    -- Order Status
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED, REFUNDED
    payment_status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, PAID, FAILED, REFUNDED
    
    -- Pricing
    subtotal DECIMAL(10,2) NOT NULL,
    shipping_cost DECIMAL(10,2) DEFAULT 0,
    tax_amount DECIMAL(10,2) DEFAULT 0,
    total_amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'GHS',
    
    -- Shipping Information
    shipping_address JSONB NOT NULL,
    billing_address JSONB,
    tracking_number VARCHAR(100),
    estimated_delivery DATE,
    
    -- Timeline
    ordered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP,
    shipped_at TIMESTAMP,
    delivered_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    
    -- System Fields
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_orders_buyer (buyer_id),
    INDEX idx_orders_seller (seller_id),
    INDEX idx_orders_status (status),
    INDEX idx_orders_created_at (created_at)
);
```

### Order Items Table
```sql
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES products(id),
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    product_snapshot JSONB, -- Store product details at time of purchase
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_order_items_order (order_id),
    INDEX idx_order_items_product (product_id)
);
```

### Payments Table
```sql
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    payment_method VARCHAR(50) NOT NULL, -- MOBILE_MONEY, CARD, BANK_TRANSFER
    payment_provider VARCHAR(50), -- PAYSTACK, FLUTTERWAVE, etc.
    transaction_id VARCHAR(255) UNIQUE,
    
    -- Payment Details
    amount DECIMAL(10,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'GHS',
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, SUCCESS, FAILED, CANCELLED, REFUNDED
    
    -- Provider Response
    provider_response JSONB,
    failure_reason TEXT,
    
    -- Timeline
    initiated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP,
    failed_at TIMESTAMP,
    
    INDEX idx_payments_order (order_id),
    INDEX idx_payments_status (status),
    INDEX idx_payments_transaction (transaction_id)
);
```

## 6. NOTIFICATION SERVICE DATABASE (notification_service_db)

### Notification Templates Table
```sql
CREATE TABLE notification_templates (
    id BIGSERIAL PRIMARY KEY,
    template_name VARCHAR(100) NOT NULL UNIQUE,
    template_type VARCHAR(20) NOT NULL, -- PUSH, EMAIL, SMS
    subject_template TEXT,
    body_template TEXT NOT NULL,
    variables JSONB, -- Template variables definition
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Notifications Table
```sql
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Reference to user service
    notification_type VARCHAR(50) NOT NULL, -- LIKE, COMMENT, FOLLOW, ORDER_UPDATE, etc.
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    
    -- Notification Data
    data JSONB, -- Additional notification data
    action_url TEXT, -- Deep link URL
    image_url TEXT,
    
    -- Delivery Status
    is_read BOOLEAN DEFAULT false,
    is_sent BOOLEAN DEFAULT false,
    delivery_status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, SENT, FAILED
    
    -- Channels
    push_sent BOOLEAN DEFAULT false,
    email_sent BOOLEAN DEFAULT false,
    sms_sent BOOLEAN DEFAULT false,
    
    -- Timeline
    scheduled_at TIMESTAMP,
    sent_at TIMESTAMP,
    read_at TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_notifications_user (user_id),
    INDEX idx_notifications_type (notification_type),
    INDEX idx_notifications_read (is_read),
    INDEX idx_notifications_created_at (created_at)
);
```

### Device Tokens Table
```sql
CREATE TABLE device_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Reference to user service
    token VARCHAR(500) NOT NULL,
    platform VARCHAR(20) NOT NULL, -- IOS, ANDROID, WEB
    device_info JSONB,
    is_active BOOLEAN DEFAULT true,
    last_used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE(user_id, token),
    INDEX idx_device_tokens_user (user_id),
    INDEX idx_device_tokens_active (is_active)
);
```

## 7. ANALYTICS SERVICE DATABASE (analytics_service_db)

### Analytics Events Table
```sql
CREATE TABLE analytics_events (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT, -- Reference to user service (nullable for anonymous events)
    session_id VARCHAR(255),
    event_name VARCHAR(100) NOT NULL,
    event_category VARCHAR(50),
    
    -- Event Data
    properties JSONB,
    user_properties JSONB,
    
    -- Context
    platform VARCHAR(20), -- IOS, ANDROID, WEB
    app_version VARCHAR(20),
    device_info JSONB,
    location_info JSONB,
    
    -- Timing
    event_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    server_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_analytics_events_user (user_id),
    INDEX idx_analytics_events_name (event_name),
    INDEX idx_analytics_events_category (event_category),
    INDEX idx_analytics_events_time (event_time)
);
```

### User Metrics Table
```sql
CREATE TABLE user_metrics (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE, -- Reference to user service
    
    -- Engagement Metrics
    posts_count INTEGER DEFAULT 0,
    likes_given INTEGER DEFAULT 0,
    likes_received INTEGER DEFAULT 0,
    comments_given INTEGER DEFAULT 0,
    comments_received INTEGER DEFAULT 0,
    
    -- Social Metrics
    followers_count INTEGER DEFAULT 0,
    following_count INTEGER DEFAULT 0,
    connections_count INTEGER DEFAULT 0,
    
    -- E-commerce Metrics
    products_sold INTEGER DEFAULT 0,
    products_bought INTEGER DEFAULT 0,
    total_sales_amount DECIMAL(10,2) DEFAULT 0,
    total_purchases_amount DECIMAL(10,2) DEFAULT 0,
    
    -- Activity Metrics
    login_streak INTEGER DEFAULT 0,
    total_sessions INTEGER DEFAULT 0,
    total_time_spent BIGINT DEFAULT 0, -- in seconds
    
    -- Calculated Fields
    engagement_score DECIMAL(5,2) DEFAULT 0,
    influence_score DECIMAL(5,2) DEFAULT 0,
    
    -- Timeline
    last_calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_user_metrics_engagement (engagement_score),
    INDEX idx_user_metrics_influence (influence_score)
);
```

### Content Metrics Table
```sql
CREATE TABLE content_metrics (
    id BIGSERIAL PRIMARY KEY,
    content_id BIGINT NOT NULL, -- post_id, product_id, etc.
    content_type VARCHAR(20) NOT NULL, -- POST, PRODUCT, COMMENT
    
    -- Engagement Metrics
    views_count INTEGER DEFAULT 0,
    likes_count INTEGER DEFAULT 0,
    comments_count INTEGER DEFAULT 0,
    shares_count INTEGER DEFAULT 0,
    saves_count INTEGER DEFAULT 0,
    
    -- Performance Metrics
    engagement_rate DECIMAL(5,2) DEFAULT 0,
    viral_score DECIMAL(5,2) DEFAULT 0,
    reach INTEGER DEFAULT 0,
    impressions INTEGER DEFAULT 0,
    
    -- E-commerce Specific (for products)
    conversion_rate DECIMAL(5,2),
    revenue_generated DECIMAL(10,2),
    
    -- Time-based Metrics
    peak_engagement_time TIMESTAMP,
    metrics_date DATE DEFAULT CURRENT_DATE,
    
    -- Timeline
    last_calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE(content_id, content_type, metrics_date),
    INDEX idx_content_metrics_type (content_type),
    INDEX idx_content_metrics_engagement (engagement_rate),
    INDEX idx_content_metrics_date (metrics_date)
);
```

## Cross-Service Reference Management

### Service Reference IDs
Since we're using microservices, we store foreign key references as simple `BIGINT` fields that reference entities in other services. Communication happens through REST APIs:

```java
// Example: In Content Service, getting user info
@Service
public class UserServiceClient {
    public UserDTO getUserById(Long userId) {
        // HTTP call to user-service
        return restTemplate.getForObject("/users/" + userId, UserDTO.class);
    }
}
```

## Database Indexes Strategy

### Primary Indexes (already defined above)
- Primary keys with BIGSERIAL
- Unique constraints where needed
- Foreign key indexes for joins

### Additional Performance Indexes
```sql
-- Feed Algorithm Indexes
CREATE INDEX idx_posts_feed_algorithm ON posts(faculty_id, created_at, visibility) WHERE is_deleted = false;
CREATE INDEX idx_posts_trending ON posts(likes_count, comments_count, created_at) WHERE created_at > CURRENT_DATE - INTERVAL '7 days';

-- Search Indexes
CREATE INDEX idx_users_search ON users USING GIN(to_tsvector('english', first_name || ' ' || last_name || ' ' || username));
CREATE INDEX idx_posts_search ON posts USING GIN(to_tsvector('english', content));
CREATE INDEX idx_products_search ON products USING GIN(to_tsvector('english', title || ' ' || description));

-- Chat Performance
CREATE INDEX idx_messages_room_time ON messages(room_id, created_at DESC);
CREATE INDEX idx_chat_rooms_last_activity ON chat_rooms(last_message_at DESC) WHERE is_active = true;

-- Analytics Performance
CREATE INDEX idx_analytics_events_time_range ON analytics_events(event_time) WHERE event_time > CURRENT_DATE - INTERVAL '30 days';
```

## Data Relationships Summary

### Cross-Service Relationships
- **User Service** ←→ **Content Service**: user_id references
- **Content Service** ←→ **Social Service**: post_id references  
- **User Service** ←→ **Social Service**: user_id references
- **User Service** ←→ **Realtime Service**: user_id references
- **User Service** ←→ **Ecommerce Service**: user_id references
- **Content Service** ←→ **Ecommerce Service**: post_id references (outfit posts → products)

### Within-Service Relationships
- **Users** ←→ **Faculties** ←→ **Universities** (hierarchical academic structure)
- **Posts** ←→ **Media Files** ←→ **Tags** (content composition)
- **Chat Rooms** ←→ **Messages** ←→ **Members** (messaging structure)
- **Orders** ←→ **Order Items** ←→ **Products** (e-commerce transactions)

This database model supports:
✅ **Student verification & faculty organization**
✅ **Rich social interactions (likes, comments, follows)**  
✅ **Real-time chat with threading**
✅ **E-commerce integration with outfit posts**
✅ **Comprehensive analytics & metrics**
✅ **Scalable microservices architecture**
✅ **Performance optimization through proper indexing**