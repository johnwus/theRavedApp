-- Saved posts (bookmarks)

CREATE TABLE IF NOT EXISTS saved_posts (
  user_id   BIGINT NOT NULL,
  post_id   BIGINT NOT NULL,
  saved_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, post_id)
);


