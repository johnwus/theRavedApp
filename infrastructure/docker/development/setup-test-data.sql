-- PostgreSQL Test Data Setup for TheRavedApp
-- This script creates test data for development and testing

-- Create test users
INSERT INTO users (id, username, email, password_hash, first_name, last_name, created_at, updated_at, status)
VALUES 
    (1, 'testuser1', 'user1@test.com', '$2a$10$test.hash.here', 'Test', 'User1', NOW(), NOW(), 'ACTIVE'),
    (2, 'testuser2', 'user2@test.com', '$2a$10$test.hash.here', 'Test', 'User2', NOW(), NOW(), 'ACTIVE'),
    (3, 'testuser3', 'user3@test.com', '$2a$10$test.hash.here', 'Test', 'User3', NOW(), NOW(), 'ACTIVE');

-- Create test products for ecommerce
INSERT INTO products (id, name, description, price, category_id, seller_id, created_at, updated_at, status)
VALUES 
    (1, 'Test Product 1', 'A test product for development', 29.99, 1, 1, NOW(), NOW(), 'ACTIVE'),
    (2, 'Test Product 2', 'Another test product for development', 49.99, 1, 1, NOW(), NOW(), 'ACTIVE'),
    (3, 'Test Product 3', 'Yet another test product', 19.99, 2, 2, NOW(), NOW(), 'ACTIVE');

-- Create test events
INSERT INTO events (id, name, description, start_date, end_date, location, organizer_id, created_at, updated_at, status)
VALUES 
    (1, 'Test Event 1', 'A test event for development', '2025-09-01 10:00:00', '2025-09-01 18:00:00', 'Test Location', 1, NOW(), NOW(), 'ACTIVE'),
    (2, 'Test Event 2', 'Another test event', '2025-09-15 14:00:00', '2025-09-15 22:00:00', 'Test Location 2', 2, NOW(), NOW(), 'ACTIVE');

-- Create test subscriptions
INSERT INTO subscriptions (id, user_id, plan_type, start_date, end_date, status, created_at, updated_at)
VALUES 
    (1, 1, 'PREMIUM', '2025-08-01', '2025-12-31', 'ACTIVE', NOW(), NOW()),
    (2, 2, 'BASIC', '2025-08-01', '2025-12-31', 'ACTIVE', NOW(), NOW());

-- Create test chat rooms for realtime service
INSERT INTO chat_rooms (id, name, description, created_by, created_at, updated_at, status)
VALUES 
    (1, 'Test Chat Room 1', 'A test chat room', 1, NOW(), NOW(), 'ACTIVE'),
    (2, 'Test Chat Room 2', 'Another test chat room', 2, NOW(), NOW(), 'ACTIVE');

-- Create test chat room members
INSERT INTO chat_room_members (id, chat_room_id, user_id, role, joined_at, status)
VALUES 
    (1, 1, 1, 'ADMIN', NOW(), 'ACTIVE'),
    (2, 1, 2, 'MEMBER', NOW(), 'ACTIVE'),
    (3, 2, 2, 'ADMIN', NOW(), 'ACTIVE'),
    (4, 2, 3, 'MEMBER', NOW(), 'ACTIVE');





