-- Create notifications table
CREATE TABLE IF NOT EXISTS notifications (
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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_notifications_type ON notifications(notification_type);
CREATE INDEX idx_notifications_read ON notifications(is_read);
CREATE INDEX idx_notifications_created_at ON notifications(created_at);
CREATE INDEX idx_notifications_delivery_status ON notifications(delivery_status);
CREATE INDEX idx_notifications_scheduled ON notifications(scheduled_at);
