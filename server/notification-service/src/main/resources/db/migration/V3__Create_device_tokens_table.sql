-- Create device_tokens table
CREATE TABLE IF NOT EXISTS device_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL, -- Reference to user service
    token VARCHAR(500) NOT NULL,
    platform VARCHAR(20) NOT NULL, -- IOS, ANDROID, WEB
    device_info JSONB,
    is_active BOOLEAN DEFAULT true,
    last_used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE UNIQUE INDEX idx_device_tokens_user_token ON device_tokens(user_id, token);
CREATE INDEX idx_device_tokens_user ON device_tokens(user_id);
CREATE INDEX idx_device_tokens_active ON device_tokens(is_active);
CREATE INDEX idx_device_tokens_platform ON device_tokens(platform);
