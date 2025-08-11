-- Content reports (abuse/report flows)

CREATE TABLE IF NOT EXISTS content_reports (
  id            BIGSERIAL PRIMARY KEY,
  reporter_id   BIGINT NOT NULL,
  target_id     BIGINT NOT NULL,
  target_type   VARCHAR(20) NOT NULL, -- POST, COMMENT, PRODUCT
  reason        TEXT,
  metadata      JSONB,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_reports_target   ON content_reports(target_id, target_type);
CREATE INDEX IF NOT EXISTS idx_reports_reporter ON content_reports(reporter_id);


