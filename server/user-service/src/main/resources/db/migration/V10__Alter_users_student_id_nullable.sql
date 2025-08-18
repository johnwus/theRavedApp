-- Make student_id nullable to allow creating users without a student ID in some flows/tests
ALTER TABLE users ALTER COLUMN student_id DROP NOT NULL;

