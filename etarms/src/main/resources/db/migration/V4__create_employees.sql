-- baseline V4__create_employees.sql

CREATE TABLE employees (
    employee_id VARCHAR(10) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    job_title VARCHAR(50) NOT NULL,
    department_id VARCHAR(10) NOT NULL,
    manager_id VARCHAR(10),
    created_by VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_by VARCHAR(50),
    updated_at TIMESTAMP,
    CONSTRAINT fk_department
        FOREIGN KEY (department_id)
        REFERENCES departments(department_id)
);

-- Sequence for employee IDs
CREATE SEQUENCE employee_seq START WITH 1 INCREMENT BY 1;
