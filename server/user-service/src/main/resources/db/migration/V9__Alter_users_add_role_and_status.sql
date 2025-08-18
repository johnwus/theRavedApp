-- Ensure users table has role and status columns to match JPA entity
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'users' AND column_name = 'role'
    ) THEN
        EXECUTE 'ALTER TABLE users ADD COLUMN role VARCHAR(50) NOT NULL DEFAULT ''STUDENT'' ';
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'users' AND column_name = 'status'
    ) THEN
        EXECUTE 'ALTER TABLE users ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT ''PENDING_VERIFICATION'' ';
    END IF;
END $$;

