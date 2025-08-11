-- Add missing cart item fields

ALTER TABLE cart_items
  ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE cart_items
  ADD COLUMN IF NOT EXISTS price DECIMAL(10,2);

-- Create index for the new price field for better performance
CREATE INDEX IF NOT EXISTS idx_cart_items_price ON cart_items(price);
