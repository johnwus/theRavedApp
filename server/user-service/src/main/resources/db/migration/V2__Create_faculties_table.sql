-- Create universities table
CREATE TABLE IF NOT EXISTS universities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE,
    country VARCHAR(100),
    state VARCHAR(100),
    city VARCHAR(100),
    address TEXT,
    website VARCHAR(255),
    logo_url VARCHAR(500),
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create faculties table
CREATE TABLE IF NOT EXISTS faculties (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    university_id BIGINT NOT NULL,
    faculty_id VARCHAR(50) NOT NULL,
    department VARCHAR(100) NOT NULL,
    position VARCHAR(100),
    office_location VARCHAR(255),
    office_hours VARCHAR(255),
    research_interests TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (university_id) REFERENCES universities(id) ON DELETE CASCADE,
    UNIQUE(university_id, faculty_id)
);

-- Create indexes
CREATE INDEX idx_universities_code ON universities(code);
CREATE INDEX idx_universities_status ON universities(status);
CREATE INDEX idx_faculties_user_id ON faculties(user_id);
CREATE INDEX idx_faculties_university_id ON faculties(university_id);
CREATE INDEX idx_faculties_department ON faculties(department);
CREATE INDEX idx_faculties_status ON faculties(status);
