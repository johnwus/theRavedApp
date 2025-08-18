-- Add listing-level payment/contact fields

ALTER TABLE products
  ADD COLUMN IF NOT EXISTS accepted_payment_methods JSONB;

ALTER TABLE products
  ADD COLUMN IF NOT EXISTS meetup_location JSONB;

ALTER TABLE products
  ADD COLUMN IF NOT EXISTS contact_phone VARCHAR(20);


