CREATE TABLE IF NOT EXISTS saved_products (
  user_id    BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  saved_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, product_id)
);


