-- Create user_metrics table
CREATE TABLE IF NOT EXISTS user_metrics (
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
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_user_metrics_engagement ON user_metrics(engagement_score);
CREATE INDEX idx_user_metrics_influence ON user_metrics(influence_score);
CREATE INDEX idx_user_metrics_last_calculated ON user_metrics(last_calculated_at);
