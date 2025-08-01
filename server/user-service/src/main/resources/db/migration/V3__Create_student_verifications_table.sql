-- Create student_verifications table
CREATE TABLE IF NOT EXISTS student_verifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    university_id BIGINT NOT NULL,
    student_id VARCHAR(50) NOT NULL,
    verification_document_url VARCHAR(500),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    verified_at TIMESTAMP,
    verified_by BIGINT,
    rejection_reason TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (university_id) REFERENCES universities(id) ON DELETE CASCADE,
    FOREIGN KEY (verified_by) REFERENCES users(id),
    UNIQUE(user_id, university_id),
    UNIQUE(university_id, student_id)
);

-- Create indexes
CREATE INDEX idx_student_verifications_user_id ON student_verifications(user_id);
CREATE INDEX idx_student_verifications_university_id ON student_verifications(university_id);
CREATE INDEX idx_student_verifications_status ON student_verifications(status);
CREATE INDEX idx_student_verifications_submitted_at ON student_verifications(submitted_at);
CREATE INDEX idx_student_verifications_verified_at ON student_verifications(verified_at);
