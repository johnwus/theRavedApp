CREATE TABLE IF NOT EXISTS events (
  id             BIGSERIAL PRIMARY KEY,
  organizer_id   BIGINT NOT NULL,
  title          VARCHAR(255) NOT NULL,
  description    TEXT,
  image_url      TEXT,
  org_avatar_url TEXT,
  location_name  VARCHAR(255),
  location_geo   JSONB,
  starts_at      TIMESTAMP NOT NULL,
  ends_at        TIMESTAMP,
  category       VARCHAR(50),
  audience       VARCHAR(20) DEFAULT 'ALL', -- ALL, FACULTY_ONLY, INVITE, UNDERGRADUATE, GRADUATE, ALUMNI, PUBLIC
  price_amount   DECIMAL(10,2) DEFAULT 0,
  currency       VARCHAR(3) DEFAULT 'GHS',
  capacity       INTEGER,
  is_featured    BOOLEAN DEFAULT false,
  created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_events_time     ON events(starts_at);
CREATE INDEX IF NOT EXISTS idx_events_org      ON events(organizer_id);
CREATE INDEX IF NOT EXISTS idx_events_category ON events(category);


