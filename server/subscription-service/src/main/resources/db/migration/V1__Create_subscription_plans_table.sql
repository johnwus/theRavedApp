CREATE TABLE IF NOT EXISTS subscription_plans (
  id            BIGSERIAL PRIMARY KEY,
  plan_code     VARCHAR(50) NOT NULL UNIQUE,
  name          VARCHAR(100) NOT NULL,
  description   TEXT,
  price_amount  DECIMAL(10,2) NOT NULL,
  currency      VARCHAR(3) DEFAULT 'GHS',
  billing_cycle VARCHAR(20) DEFAULT 'MONTHLY', -- MONTHLY, YEARLY
  features      JSONB,
  is_active     BOOLEAN DEFAULT true,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


