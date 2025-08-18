CREATE TABLE IF NOT EXISTS user_subscriptions (
  id                      BIGSERIAL PRIMARY KEY,
  user_id                 BIGINT NOT NULL,
  plan_id                 BIGINT NOT NULL,
  status                  VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, TRIALING, CANCELED, EXPIRED
  trial_end_at            TIMESTAMP,
  current_period_start    TIMESTAMP,
  current_period_end      TIMESTAMP,
  cancel_at_period_end    BOOLEAN DEFAULT false,
  created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(user_id)
);

CREATE INDEX IF NOT EXISTS idx_user_subscriptions_user ON user_subscriptions(user_id);


