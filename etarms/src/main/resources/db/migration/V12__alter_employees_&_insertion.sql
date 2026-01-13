ALTER TABLE employees
ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE employees ALTER COLUMN employee_id
    SET DEFAULT ('dep' || nextval('employee_seq'));




INSERT INTO employees (
    id, employee_id, first_name, last_name, email, phone_number,
    job_title, employment_status, department_ref_id, manager_id,
    created_by, created_at, updated_by, updated_at
) VALUES
(gen_random_uuid(), 'emp001', 'Alice', 'Johnson', 'alice.johnson@example.com', '01710000001', 'Software Engineer', 'Active', '79030f00-5224-4cf8-a334-e686a56ed27b', NULL, 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp002', 'Bob', 'Smith', 'bob.smith@example.com', '01710000002', 'DevOps Engineer', 'Active', '79030f00-5224-4cf8-a334-e686a56ed27b', 'EMP001', 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp003', 'Charlie', 'Brown', 'charlie.brown@example.com', '01710000003', 'IT Manager', 'Active', '79030f00-5224-4cf8-a334-e686a56ed27b', NULL, 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp005', 'Ethan', 'Hunt', 'ethan.hunt@example.com', '01710000005', 'Recruiter', 'Active', '69cc87ce-6b0c-49f8-80a6-05fe91902deb', 'EMP004', 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp006', 'Fiona', 'Gallagher', 'fiona.gallagher@example.com', '01710000006', 'HR Manager', 'Active', '69cc87ce-6b0c-49f8-80a6-05fe91902deb', NULL, 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp007', 'George', 'Miller', 'george.miller@example.com', '01710000007', 'Accountant', 'Active', '459ec892-b98e-4f06-9e2d-11d6f71b8f02', NULL, 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp008', 'Hannah', 'Williams', 'hannah.williams@example.com', '01710000008', 'Financial Analyst', 'Active', '459ec892-b98e-4f06-9e2d-11d6f71b8f02', 'EMP007', 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp009', 'Ian', 'Taylor', 'ian.taylor@example.com', '01710000009', 'Finance Manager', 'Active', '459ec892-b98e-4f06-9e2d-11d6f71b8f02', NULL, 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp010', 'Jane', 'Doe', 'jane.doe@example.com', '01710000010', 'Marketing Specialist', 'Active', '8e19aab7-18aa-4025-9976-dc382f2da48b', NULL, 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp011', 'Kevin', 'Anderson', 'kevin.anderson@example.com', '01710000011', 'Marketing Coordinator', 'Active', '8e19aab7-18aa-4025-9976-dc382f2da48b', 'EMP010', 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp012', 'Laura', 'Scott', 'laura.scott@example.com', '01710000012', 'Marketing Manager', 'Active', '8e19aab7-18aa-4025-9976-dc382f2da48b', NULL, 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp013', 'Michael', 'Clark', 'michael.clark@example.com', '01710000013', 'Operations Executive', 'Active', 'c030fa7a-18ac-431b-8661-e5ad115f38c3', NULL, 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp014', 'Nina', 'Adams', 'nina.adams@example.com', '01710000014', 'Logistics Coordinator', 'Active', 'c030fa7a-18ac-431b-8661-e5ad115f38c3', 'EMP013', 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp015', 'Oliver', 'Evans', 'oliver.evans@example.com', '01710000015', 'Operations Manager', 'Active', 'c030fa7a-18ac-431b-8661-e5ad115f38c3', NULL, 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp016', 'Paul', 'Walker', 'paul.walker@example.com', '01710000016', 'Customer Support Rep', 'Active', '66d1e412-5f1e-4bf2-b43b-ac09a3df3999', NULL, 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp017', 'Quinn', 'Harris', 'quinn.harris@example.com', '01710000017', 'Customer Support Rep', 'Active', '66d1e412-5f1e-4bf2-b43b-ac09a3df3999', 'EMP016', 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp018', 'Rachel', 'Green', 'rachel.green@example.com', '01710000018', 'Customer Support Lead', 'Active', '66d1e412-5f1e-4bf2-b43b-ac09a3df3999', NULL, 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp019', 'Sam', 'Wilson', 'sam.wilson@example.com', '01710000019', 'CTO', 'Active', '79030f00-5224-4cf8-a334-e686a56ed27b', NULL, 'system', NOW(), 'system', NOW()),
(gen_random_uuid(), 'emp020', 'Tina', 'Turner', 'tina.turner@example.com', '01710000020', 'HR Coordinator', 'Active', '69cc87ce-6b0c-49f8-80a6-05fe91902deb', 'EMP006', 'system', NOW(), 'system', NOW());
