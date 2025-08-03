-- Create messages table
CREATE TABLE IF NOT EXISTS messages (
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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_messages_room_id ON messages(room_id);
CREATE INDEX idx_messages_sender_id ON messages(sender_id);
CREATE INDEX idx_messages_created_at ON messages(created_at);
CREATE INDEX idx_messages_reply_to ON messages(reply_to_message_id);
CREATE INDEX idx_messages_room_time ON messages(room_id, created_at DESC);
