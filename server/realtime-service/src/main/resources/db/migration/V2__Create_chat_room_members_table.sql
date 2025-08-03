-- Create chat_room_members table
CREATE TABLE IF NOT EXISTS chat_room_members (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL REFERENCES chat_rooms(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL, -- Reference to user service
    role VARCHAR(20) DEFAULT 'MEMBER', -- ADMIN, MODERATOR, MEMBER
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_read_at TIMESTAMP,
    is_muted BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true
);

-- Create indexes
CREATE UNIQUE INDEX idx_chat_members_room_user ON chat_room_members(room_id, user_id);
CREATE INDEX idx_chat_members_room ON chat_room_members(room_id);
CREATE INDEX idx_chat_members_user ON chat_room_members(user_id);
CREATE INDEX idx_chat_members_active ON chat_room_members(is_active);
