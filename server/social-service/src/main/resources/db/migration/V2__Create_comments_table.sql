-- Create comments table
CREATE TABLE IF NOT EXISTS comments (
  id                 BIGSERIAL PRIMARY KEY,
  post_id            BIGINT NOT NULL,
  user_id            BIGINT NOT NULL,
  parent_comment_id  BIGINT REFERENCES comments(id),
  content            TEXT NOT NULL,
  likes_count        INTEGER DEFAULT 0,
  replies_count      INTEGER DEFAULT 0,
  is_flagged         BOOLEAN DEFAULT false,
  moderation_status  VARCHAR(20) DEFAULT 'APPROVED',
  is_deleted         BOOLEAN DEFAULT false,
  created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_comments_post_id  ON comments(post_id);
CREATE INDEX idx_comments_user_id  ON comments(user_id);
CREATE INDEX idx_comments_parent   ON comments(parent_comment_id);
CREATE INDEX idx_comments_created  ON comments(created_at);