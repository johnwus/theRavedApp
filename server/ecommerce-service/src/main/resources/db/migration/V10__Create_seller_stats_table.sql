CREATE TABLE IF NOT EXISTS seller_stats (
  seller_id        BIGINT PRIMARY KEY,
  total_items      INTEGER DEFAULT 0,
  total_sales      DECIMAL(10,2) DEFAULT 0,
  items_sold       INTEGER DEFAULT 0,
  rating           DECIMAL(3,2) DEFAULT 0,
  updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


