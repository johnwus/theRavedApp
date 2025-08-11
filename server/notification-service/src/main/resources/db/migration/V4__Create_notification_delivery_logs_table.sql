-- Create notification_delivery_logs table
CREATE TABLE IF NOT EXISTS notification_delivery_logs (
    id BIGSERIAL PRIMARY KEY,
    notification_id BIGINT NOT NULL REFERENCES notifications(id) ON DELETE CASCADE,
    delivery_channel VARCHAR(20) NOT NULL, -- EMAIL, SMS, PUSH
    delivery_status VARCHAR(20) NOT NULL, -- PENDING, SENT, DELIVERED, FAILED
    recipient VARCHAR(255) NOT NULL, -- Email address, phone number, or device token
    attempt_count INTEGER DEFAULT 1,
    error_message TEXT,
    sent_at TIMESTAMP,
    delivered_at TIMESTAMP,
    failed_at TIMESTAMP,
    retry_after TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_notification_delivery_logs_notification ON notification_delivery_logs(notification_id);
CREATE INDEX idx_notification_delivery_logs_channel ON notification_delivery_logs(delivery_channel);
CREATE INDEX idx_notification_delivery_logs_status ON notification_delivery_logs(delivery_status);
CREATE INDEX idx_notification_delivery_logs_recipient ON notification_delivery_logs(recipient);
CREATE INDEX idx_notification_delivery_logs_created_at ON notification_delivery_logs(created_at);


