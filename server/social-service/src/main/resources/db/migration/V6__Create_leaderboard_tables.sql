-- Leaderboard seasons and scores

CREATE TABLE IF NOT EXISTS leaderboard_seasons (
  id            BIGSERIAL PRIMARY KEY,
  name          VARCHAR(100) NOT NULL,
  season_type   VARCHAR(20) DEFAULT 'WEEK', -- WEEK, MONTH, ALL
  starts_at     TIMESTAMP NOT NULL,
  ends_at       TIMESTAMP NOT NULL,
  is_active     BOOLEAN DEFAULT true,
  created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS leaderboard_scores (
  id        BIGSERIAL PRIMARY KEY,
  season_id BIGINT NOT NULL,
  user_id   BIGINT NOT NULL,
  score     INTEGER DEFAULT 0,
  rank      INTEGER,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(season_id, user_id)
);

CREATE INDEX IF NOT EXISTS idx_leaderboard_season ON leaderboard_scores(season_id, score DESC);


