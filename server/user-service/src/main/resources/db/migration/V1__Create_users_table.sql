CREATE TABLE IF NOT EXISTS users (
  id                   BIGSERIAL PRIMARY KEY,
  student_id           VARCHAR(50)  NOT NULL UNIQUE,
  email                VARCHAR(255) NOT NULL UNIQUE,
  username             VARCHAR(50)  NOT NULL UNIQUE,
  password_hash        VARCHAR(255) NOT NULL,

  -- Personal
  first_name           VARCHAR(100) NOT NULL,
  last_name            VARCHAR(100) NOT NULL,
  display_name         VARCHAR(100),
  bio                  TEXT,
  date_of_birth        DATE,
  gender               VARCHAR(20),
  phone_number         VARCHAR(20),

  -- Academic
  university_id        BIGINT,
  faculty_id           BIGINT,
  academic_year        INTEGER,
  graduation_year      INTEGER,

  -- Profile
  profile_picture_url  TEXT,
  cover_photo_url      TEXT,
  location             VARCHAR(255),
  website_url          TEXT,

  -- Privacy flags
  is_profile_public                  BOOLEAN DEFAULT true,
  allow_messages_from_strangers      BOOLEAN DEFAULT true,
  show_email                         BOOLEAN DEFAULT false,
  show_phone                         BOOLEAN DEFAULT false,

  -- System
  email_verified       BOOLEAN DEFAULT false,
  student_id_verified  BOOLEAN DEFAULT false,
  seller_verified      BOOLEAN DEFAULT false,
  is_active            BOOLEAN DEFAULT true,
  last_login_at        TIMESTAMP,
  created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_university ON users(university_id);
CREATE INDEX idx_users_faculty    ON users(faculty_id);
