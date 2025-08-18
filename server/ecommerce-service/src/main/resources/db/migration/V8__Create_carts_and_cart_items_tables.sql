CREATE TABLE IF NOT EXISTS carts (
  id         BIGSERIAL PRIMARY KEY,
  user_id    BIGINT NOT NULL UNIQUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart_items (
  id          BIGSERIAL PRIMARY KEY,
  cart_id     BIGINT NOT NULL,
  product_id  BIGINT NOT NULL,
  size        VARCHAR(20) DEFAULT 'M',
  color       VARCHAR(50) DEFAULT 'Black',
  quantity    INTEGER NOT NULL DEFAULT 1,
  unit_price  DECIMAL(10,2) NOT NULL,
  seller_id   BIGINT NOT NULL,
  UNIQUE(cart_id, product_id, size, color)
);

CREATE INDEX IF NOT EXISTS idx_cart_items_cart ON cart_items(cart_id);


