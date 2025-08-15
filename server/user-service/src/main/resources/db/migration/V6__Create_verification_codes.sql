-- Verification codes for email/phone flows
CREATE TABLE IF NOT EXISTS verification_codes (
  id            BIGSERIAL PRIMARY KEY,
  user_id       BIGINT, -- nullable until user exists
  channel       VARCHAR(20) NOT NULL, -- EMAIL, PHONE
  destination   VARCHAR(255) NOT NULL, -- email or phone
  purpose       VARCHAR(30) NOT NULL, -- REGISTER, VERIFY_EMAIL, VERIFY_PHONE, RESET_PASSWORD
  code_hash     VARCHAR(255) NOT NULL, -- store hashed code
  attempts      INTEGER DEFAULT 0,
  max_attempts  INTEGER DEFAULT 5,
  expires_at    TIMESTAMP WITH TIME ZONE NOT NULL,
  consumed_at   TIMESTAMP WITH TIME ZONE,
  created_at    TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_verification_dest    ON verification_codes(destination);
CREATE INDEX IF NOT EXISTS idx_verification_purpose ON verification_codes(purpose);

