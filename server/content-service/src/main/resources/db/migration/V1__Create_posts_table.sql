CREATE TABLE IF NOT EXISTS posts (
  id              BIGSERIAL PRIMARY KEY,
  user_id         BIGINT NOT NULL,
  content         TEXT NOT NULL,

  -- Types and visibility
  post_type       VARCHAR(20) DEFAULT 'OUTFIT', -- OUTFIT, GENERAL, POLL, EVENT
  visibility      VARCHAR(20) DEFAULT 'PUBLIC', -- PUBLIC, FACULTY_ONLY, FOLLOWERS_ONLY, CONNECTIONS_ONLY, PRIVATE
  faculty_id      BIGINT,

  -- Drafts & scheduling
  publish_status  VARCHAR(12) DEFAULT 'PUBLISHED', -- DRAFT, SCHEDULED, PUBLISHED, ARCHIVED
  scheduled_at    TIMESTAMP,

  -- Denormalized metrics
  likes_count     INTEGER DEFAULT 0,
  comments_count  INTEGER DEFAULT 0,
  shares_count    INTEGER DEFAULT 0,
  views_count     INTEGER DEFAULT 0,
  saves_count     INTEGER DEFAULT 0,

  -- Moderation
  is_flagged       BOOLEAN DEFAULT false,
  moderation_status VARCHAR(20) DEFAULT 'APPROVED',
  flagged_reason    TEXT,

  -- System flags
  is_deleted      BOOLEAN DEFAULT false,
  is_featured     BOOLEAN DEFAULT false,
  featured_until  TIMESTAMP,
  created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_posts_user_id     ON posts(user_id);
CREATE INDEX idx_posts_faculty_id  ON posts(faculty_id);
CREATE INDEX idx_posts_created_at  ON posts(created_at);
CREATE INDEX idx_posts_featured    ON posts(is_featured, featured_until);
CREATE INDEX idx_posts_visibility  ON posts(visibility);
