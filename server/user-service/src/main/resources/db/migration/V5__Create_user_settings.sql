-- User Settings table per service spec
CREATE TABLE IF NOT EXISTS user_settings (
  user_id            BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
  show_activity      BOOLEAN DEFAULT TRUE,
  read_receipts      BOOLEAN DEFAULT TRUE,
  allow_downloads    BOOLEAN DEFAULT FALSE,
  allow_story_sharing BOOLEAN DEFAULT TRUE,
  analytics_enabled  BOOLEAN DEFAULT TRUE,
  personalized_ads   BOOLEAN DEFAULT FALSE,
  language           VARCHAR(10) DEFAULT 'en',
  date_format        VARCHAR(20) DEFAULT 'DD/MM/YYYY',
  currency           VARCHAR(10) DEFAULT 'GHS',
  theme              VARCHAR(50) DEFAULT 'default',
  notification_preferences JSONB DEFAULT '{}'::jsonb,
  created_at         TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  updated_at         TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

