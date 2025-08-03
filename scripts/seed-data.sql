-- Seed initial data for RAvED databases

-- User Service Data
\c raved_user_db;

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

-- Ecommerce Service Data
\c raved_ecommerce_db;

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

INSERT INTO product_categories (name, description, parent_category_id, is_active) VALUES
('Dresses', 'Women dresses and gowns', 1, true),
('Shirts', 'Shirts and blouses', 1, true),
('Pants', 'Trousers and jeans', 1, true),
('Watches', 'Wristwatches and timepieces', 2, true),
('Jewelry', 'Necklaces, earrings, and rings', 2, true),
('Sneakers', 'Casual and sports sneakers', 3, true),
('Formal Shoes', 'Dress shoes and formal footwear', 3, true)
ON CONFLICT (name) DO NOTHING;

-- Notification Service Data
\c raved_notification_db;

INSERT INTO notification_templates (template_name, template_type, subject_template, body_template, variables, is_active) VALUES
('like_post', 'PUSH', NULL, '{{user_name}} liked your post', '{"user_name": "string", "post_id": "number"}', true),
('comment_post', 'PUSH', NULL, '{{user_name}} commented on your post', '{"user_name": "string", "post_id": "number"}', true),
('follow_user', 'PUSH', NULL, '{{user_name}} started following you', '{"user_name": "string", "user_id": "number"}', true),
('order_confirmed', 'PUSH', NULL, 'Your order #{{order_number}} has been confirmed', '{"order_number": "string"}', true),
('order_shipped', 'PUSH', NULL, 'Your order #{{order_number}} has been shipped', '{"order_number": "string", "tracking_number": "string"}', true),
('welcome_email', 'EMAIL', 'Welcome to RAvED!', 'Welcome {{user_name}} to the RAvED community!', '{"user_name": "string"}', true),
('new_message', 'PUSH', NULL, '{{user_name}} sent you a message', '{"user_name": "string", "room_id": "number"}', true),
('product_sold', 'PUSH', NULL, 'Your product "{{product_name}}" has been sold!', '{"product_name": "string", "order_id": "number"}', true),
('payment_received', 'PUSH', NULL, 'Payment of {{amount}} {{currency}} received', '{"amount": "number", "currency": "string"}', true),
('verification_approved', 'EMAIL', 'Account Verified!', 'Congratulations {{user_name}}! Your student account has been verified.', '{"user_name": "string"}', true)
ON CONFLICT (template_name) DO NOTHING;
