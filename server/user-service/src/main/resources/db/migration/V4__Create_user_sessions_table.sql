CREATE TABLE IF NOT EXISTS user_sessions (
  id               BIGSERIAL PRIMARY KEY,
  user_id          BIGINT NOT NULL,
  session_token    VARCHAR(255) NOT NULL UNIQUE,
  device_info      JSONB,
  ip_address       INET,
  user_agent       TEXT,
  is_active        BOOLEAN DEFAULT true,
  expires_at       TIMESTAMP NOT NULL,
  created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


