-- Create products table
CREATE TABLE IF NOT EXISTS products (
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
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_products_seller ON products(seller_id);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_price ON products(price);
CREATE INDEX idx_products_featured ON products(is_featured, featured_until);
CREATE INDEX idx_products_created_at ON products(created_at);
CREATE INDEX idx_products_available ON products(is_available);
CREATE INDEX idx_products_condition ON products(condition);
