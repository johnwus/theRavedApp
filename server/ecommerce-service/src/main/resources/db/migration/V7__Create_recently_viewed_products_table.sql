CREATE TABLE IF NOT EXISTS recently_viewed_products (
  user_id    BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  viewed_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, product_id)
);


