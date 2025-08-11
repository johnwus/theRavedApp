-- Add missing payment fields

ALTER TABLE payments
  ADD COLUMN IF NOT EXISTS buyer_id BIGINT NOT NULL DEFAULT 0;

ALTER TABLE payments
  ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE payments
  ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE payments
  ADD COLUMN IF NOT EXISTS processed_at TIMESTAMP;

ALTER TABLE payments
  ADD COLUMN IF NOT EXISTS gateway_response TEXT;

ALTER TABLE payments
  ADD COLUMN IF NOT EXISTS parent_payment_id BIGINT;

-- Create indexes for the new fields for better performance
CREATE INDEX IF NOT EXISTS idx_payments_buyer ON payments(buyer_id);
CREATE INDEX IF NOT EXISTS idx_payments_created_at ON payments(created_at);
CREATE INDEX IF NOT EXISTS idx_payments_parent ON payments(parent_payment_id);

-- Add foreign key constraint for parent_payment_id
ALTER TABLE payments
  ADD CONSTRAINT fk_payments_parent_payment
  FOREIGN KEY (parent_payment_id) REFERENCES payments(id);