-- Align student_verifications schema with JPA entity StudentVerification

-- 1) Add additional_document_url if missing
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'student_verifications' AND column_name = 'additional_document_url'
    ) THEN
        EXECUTE 'ALTER TABLE student_verifications ADD COLUMN additional_document_url TEXT';
    END IF;
END $$;

-- 2) Rename verified_by -> verified_by_user_id if needed
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'student_verifications' AND column_name = 'verified_by'
    ) AND NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'student_verifications' AND column_name = 'verified_by_user_id'
    ) THEN
        EXECUTE 'ALTER TABLE student_verifications RENAME COLUMN verified_by TO verified_by_user_id';
    END IF;
END $$;

-- 3) Add rejection_reason if missing
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'student_verifications' AND column_name = 'rejection_reason'
    ) THEN
        EXECUTE 'ALTER TABLE student_verifications ADD COLUMN rejection_reason TEXT';
    END IF;
END $$;

-- 4) Add university_id if missing and reference universities(id)
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'student_verifications' AND column_name = 'university_id'
    ) THEN
        EXECUTE 'ALTER TABLE student_verifications ADD COLUMN university_id BIGINT';
        -- Add FK constraint
        IF NOT EXISTS (
            SELECT 1 FROM information_schema.table_constraints tc
            JOIN information_schema.key_column_usage kcu ON tc.constraint_name = kcu.constraint_name
            WHERE tc.table_name = 'student_verifications' AND tc.constraint_type = 'FOREIGN KEY' AND kcu.column_name = 'university_id'
        ) THEN
            EXECUTE 'ALTER TABLE student_verifications ADD CONSTRAINT fk_student_verifications_university FOREIGN KEY (university_id) REFERENCES universities(id)';
        END IF;
    END IF;
END $$;

