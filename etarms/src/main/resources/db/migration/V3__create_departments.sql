-- baseline V1__create_departments.sql

CREATE TABLE departments (
    department_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_by VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_by VARCHAR(50),
    updated_at TIMESTAMP
);

-- Sequence for department IDs
CREATE SEQUENCE department_seq START WITH 1 INCREMENT BY 1;
