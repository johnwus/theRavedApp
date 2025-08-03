-- Create content_metrics table
CREATE TABLE IF NOT EXISTS content_metrics (
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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE UNIQUE INDEX idx_content_metrics_unique ON content_metrics(content_id, content_type, metrics_date);
CREATE INDEX idx_content_metrics_type ON content_metrics(content_type);
CREATE INDEX idx_content_metrics_engagement ON content_metrics(engagement_rate);
CREATE INDEX idx_content_metrics_date ON content_metrics(metrics_date);
CREATE INDEX idx_content_metrics_viral ON content_metrics(viral_score);
