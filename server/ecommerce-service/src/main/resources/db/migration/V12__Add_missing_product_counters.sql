-- Add missing product counter fields

ALTER TABLE products
  ADD COLUMN IF NOT EXISTS saves_count INTEGER DEFAULT 0;

ALTER TABLE products
  ADD COLUMN IF NOT EXISTS orders_count INTEGER DEFAULT 0;

-- Create indexes for the new counter fields for better performance
CREATE INDEX IF NOT EXISTS idx_products_saves_count ON products(saves_count);
CREATE INDEX IF NOT EXISTS idx_products_orders_count ON products(orders_count);
