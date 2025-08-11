-- User connections (friend-like relationships)

CREATE TABLE IF NOT EXISTS user_connections (
  id                 BIGSERIAL PRIMARY KEY,
  requester_id       BIGINT NOT NULL,
  addressee_id       BIGINT NOT NULL,
  connection_status  VARCHAR(20) DEFAULT 'PENDING', -- PENDING, ACCEPTED, BLOCKED
  connection_type    VARCHAR(20) DEFAULT 'FRIEND',  -- FRIEND, STUDY_BUDDY, ROOMMATE
  created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(requester_id, addressee_id)
);

CREATE INDEX IF NOT EXISTS idx_connections_requester ON user_connections(requester_id);
CREATE INDEX IF NOT EXISTS idx_connections_addressee ON user_connections(addressee_id);


