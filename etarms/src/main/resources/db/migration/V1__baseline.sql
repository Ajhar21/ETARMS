-- Enable pgcrypto for gen_random_uuid()
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- --------------------------
-- Roles Table
-- --------------------------
CREATE TABLE roles (
    role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------
-- Users Table
-- --------------------------
CREATE TABLE users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT true,
    role_id UUID REFERENCES roles(role_id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------
-- Seed Roles
-- --------------------------
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('MANAGER');
INSERT INTO roles (name) VALUES ('EMPLOYEE');

-- --------------------------
-- Seed Admin User
-- Password should be hashed in Java code
-- Replace with actual BCrypt hash
-- --------------------------
INSERT INTO users (username, password, enabled, role_id)
VALUES (
    'admin',
    '$2a$10$X9v7Hk1H.kf5JYxBZ2aLe.8KszxG7A5qfR9jTjvC9QjqYozx4h4kO', -- replace later with real BCrypt hash
    true,
    (SELECT role_id FROM roles WHERE name='ADMIN')
);
