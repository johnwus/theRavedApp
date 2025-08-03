-- Create chat_rooms table
CREATE TABLE IF NOT EXISTS chat_rooms (
    id BIGSERIAL PRIMARY KEY,
    room_type VARCHAR(20) NOT NULL, -- DIRECT, GROUP, FACULTY_GROUP
    name VARCHAR(255), -- null for direct messages
    description TEXT,
    avatar_url TEXT,
    created_by BIGINT NOT NULL, -- Reference to user service
    faculty_id BIGINT, -- Reference to user service (for faculty groups)
    is_active BOOLEAN DEFAULT true,
    last_message_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_chat_rooms_type ON chat_rooms(room_type);
CREATE INDEX idx_chat_rooms_faculty ON chat_rooms(faculty_id);
CREATE INDEX idx_chat_rooms_last_message ON chat_rooms(last_message_at);
CREATE INDEX idx_chat_rooms_created_by ON chat_rooms(created_by);
CREATE INDEX idx_chat_rooms_active ON chat_rooms(is_active);
