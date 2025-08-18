CREATE TABLE IF NOT EXISTS student_verifications (
  id                      BIGSERIAL PRIMARY KEY,
  user_id                 BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  student_id_document_url TEXT NOT NULL,
  verification_status     VARCHAR(20) DEFAULT 'PENDING', -- PENDING, APPROVED, REJECTED
  verified_by             BIGINT REFERENCES users(id),
  verification_notes      TEXT,
  submitted_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  reviewed_at             TIMESTAMP,
  expires_at              TIMESTAMP,
  created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
