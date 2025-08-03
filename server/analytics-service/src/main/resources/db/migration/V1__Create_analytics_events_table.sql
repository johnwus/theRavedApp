-- Create analytics_events table
CREATE TABLE IF NOT EXISTS analytics_events (
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
    server_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_analytics_events_user ON analytics_events(user_id);
CREATE INDEX idx_analytics_events_name ON analytics_events(event_name);
CREATE INDEX idx_analytics_events_category ON analytics_events(event_category);
CREATE INDEX idx_analytics_events_time ON analytics_events(event_time);
CREATE INDEX idx_analytics_events_session ON analytics_events(session_id);
CREATE INDEX idx_analytics_events_platform ON analytics_events(platform);

-- Create time-based partitioned index for performance
CREATE INDEX idx_analytics_events_time_range ON analytics_events(event_time) WHERE event_time > CURRENT_DATE - INTERVAL '30 days';
