
CREATE INDEX idx_employees_role
ON employees(role);

-- Tasks for an employee (very frequent)
CREATE INDEX idx_tasks_assigned_to
ON tasks(assigned_to_id);

-- Pending / active tasks for an employee
CREATE INDEX idx_tasks_assigned_to_status
ON tasks(assigned_to_id, status);

-- Manager workload / audit
CREATE INDEX idx_tasks_assigned_by
ON tasks(assigned_by_id);

-- Task processing queues
CREATE INDEX idx_tasks_status_priority
ON tasks(status, priority);

-- Attendance history for employee
CREATE INDEX idx_attendance_employee
ON attendance(employee_id);