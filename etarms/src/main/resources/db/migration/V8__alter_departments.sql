--INSERT INTO departments (name, description, created_by, created_at, updated_by, updated_at)
--VALUES
--('Human Resources', 'Handles recruitment, payroll, and employee relations', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP),
--('Finance', 'Manages budgeting, accounting, and financial reporting', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP),
--('Marketing', 'Responsible for marketing campaigns and brand strategy', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP),
--('Operations', 'Oversees daily business operations and logistics', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP),
--('Customer Support', 'Provides support and assistance to customers', 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP);
ALTER TABLE departments ALTER COLUMN department_id
    SET DEFAULT ('emp' || nextval('department_seq'));