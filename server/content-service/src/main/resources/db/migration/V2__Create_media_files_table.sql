-- Create media_files table
CREATE TABLE IF NOT EXISTS media_files (
  id             BIGSERIAL PRIMARY KEY,
  post_id        BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
  file_url       TEXT NOT NULL,
  thumbnail_url  TEXT,
  file_type      VARCHAR(20) NOT NULL, -- IMAGE, VIDEO, AUDIO
  file_size      BIGINT,
  width          INTEGER,
  height         INTEGER,
  duration       INTEGER,
  file_format    VARCHAR(20),
  display_order  INTEGER DEFAULT 0,
  alt_text       TEXT,
  is_primary     BOOLEAN DEFAULT false,
  created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_media_files_post_id ON media_files(post_id);