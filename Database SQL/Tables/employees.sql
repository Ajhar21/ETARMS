CREATE TABLE employees (
    id              VARCHAR(20) PRIMARY KEY
                    DEFAULT ('EMP' || nextval('emp_seq')),

    name            VARCHAR(100) NOT NULL,
    email           VARCHAR(150) UNIQUE NOT NULL,
    role            employee_role NOT NULL,
    department      VARCHAR(100),

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(20) DEFAULT current_user,
	updated_by VARCHAR(20) DEFAULT current_user
);
--drop table employees;