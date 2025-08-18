-- Add verified_at column if missing to match JPA entity StudentVerification
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_name = 'student_verifications' AND column_name = 'verified_at'
    ) THEN
        EXECUTE 'ALTER TABLE student_verifications ADD COLUMN verified_at TIMESTAMP';
    END IF;
END $$;

