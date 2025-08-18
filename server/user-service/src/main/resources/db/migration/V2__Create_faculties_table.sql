-- Universities catalog
CREATE TABLE IF NOT EXISTS universities (
  id              BIGSERIAL PRIMARY KEY,
  name            VARCHAR(255) NOT NULL UNIQUE,
  code            VARCHAR(10)  NOT NULL UNIQUE,
  country         VARCHAR(100) NOT NULL,
  city            VARCHAR(100) NOT NULL,
  domain_suffix   VARCHAR(50),
  logo_url        TEXT,
  is_active       BOOLEAN DEFAULT true,
  created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Faculties catalog
CREATE TABLE IF NOT EXISTS faculties (
  id              BIGSERIAL PRIMARY KEY,
  university_id   BIGINT NOT NULL REFERENCES universities(id),
  name            VARCHAR(255) NOT NULL,
  code            VARCHAR(20)  NOT NULL,
  description     TEXT,
  color_code      VARCHAR(7),
  is_active       BOOLEAN DEFAULT true,
  created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(university_id, code)
);
