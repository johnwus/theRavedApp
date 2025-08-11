-- Create likes table
CREATE TABLE IF NOT EXISTS likes (
  id           BIGSERIAL PRIMARY KEY,
  user_id      BIGINT NOT NULL,
  target_id    BIGINT NOT NULL,
  target_type  VARCHAR(20) NOT NULL, -- POST, COMMENT, PRODUCT
  created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(user_id, target_id, target_type)
);

CREATE INDEX idx_likes_user   ON likes(user_id);
CREATE INDEX idx_likes_target ON likes(target_id, target_type);
