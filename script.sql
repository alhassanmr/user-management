-- Insert sample data
INSERT INTO users (username, password, email, created_at, updated_at)
VALUES
    ('kofi_ghana', 'hashed_password_kofi', 'kofi.ghana@gmail.com', NOW(), NOW()),
    ('ama_ghana', 'hashed_password_ama', 'ama.ghana@gmail.com', NOW(), NOW());

-- Select all users
SELECT * FROM users;

-- Select a specific user by id
SELECT * FROM users WHERE id = 2;

-- Update user details
UPDATE users
SET email = 'kwesi.ghana@yahoo.com', updated_at = NOW()
WHERE id = 1;

-- Delete a user by id
DELETE FROM users WHERE id = 2;
