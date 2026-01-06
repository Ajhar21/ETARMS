CREATE TABLE tasks (
    id                  VARCHAR(20) PRIMARY KEY
                        DEFAULT ('T' || nextval('task_seq')),

    title               VARCHAR(200) NOT NULL,
    description         TEXT,
    status              task_status NOT NULL,
    priority            task_priority NOT NULL,

    assigned_to_id      VARCHAR(20) NOT NULL,
    assigned_by_id      VARCHAR(20) NOT NULL,

    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(20) DEFAULT current_user,
	updated_by VARCHAR(20) DEFAULT current_user,

    completed_at        TIMESTAMP,

    CONSTRAINT fk_task_assigned_to
        FOREIGN KEY (assigned_to_id)
        REFERENCES employees(id),

    CONSTRAINT fk_task_assigned_by
        FOREIGN KEY (assigned_by_id)
        REFERENCES employees(id)
);
--drop table tasks;