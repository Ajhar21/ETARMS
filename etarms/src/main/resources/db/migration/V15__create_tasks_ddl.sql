-- =========================================
-- Script:V15__create_tasks_ddl
-- TASKS TABLE
-- =========================================
CREATE TABLE tasks (
    -- Internal primary key
    id UUID PRIMARY KEY,

    -- Business-readable task identifier
    task_id VARCHAR(20) NOT NULL UNIQUE,

    -- Core task fields
    title VARCHAR(150) NOT NULL,
    description TEXT,

    -- Stored as VARCHAR (validated in Java)
    status VARCHAR(30) NOT NULL,
    priority VARCHAR(30) NOT NULL,

    deadline TIMESTAMP NOT NULL,

    -- Ownership & responsibility (UUID-based)
    assigned_to UUID NOT NULL,
    manager_id UUID NOT NULL,

    -- Optimistic locking
    version INTEGER NOT NULL DEFAULT 0,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(50),

    -- Data integrity
    CONSTRAINT chk_deadline_after_creation
        CHECK (deadline >= created_at)
);

--add default generate id for test data
ALTER TABLE tasks
ALTER COLUMN id SET DEFAULT gen_random_uuid();

-- =========================================
-- FOREIGN KEY CONSTRAINTS
-- =========================================
ALTER TABLE tasks
ADD CONSTRAINT fk_tasks_assigned_to
FOREIGN KEY (assigned_to)
REFERENCES employees(id);

ALTER TABLE tasks
ADD CONSTRAINT fk_tasks_manager
FOREIGN KEY (manager_id)
REFERENCES employees(id);

-- =========================================
-- INDEXES (QUERY-DRIVEN)
-- =========================================
CREATE INDEX idx_tasks_task_id
    ON tasks (task_id);

CREATE INDEX idx_tasks_assigned_to
    ON tasks (assigned_to);

CREATE INDEX idx_tasks_status
    ON tasks (status);

CREATE INDEX idx_tasks_deadline
    ON tasks (deadline);

-- High-value composite index for task inbox
CREATE INDEX idx_tasks_assignee_status_deadline
    ON tasks (assigned_to, status, deadline);
