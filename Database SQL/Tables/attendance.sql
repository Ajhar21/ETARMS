CREATE TABLE attendance (
    id                  VARCHAR(20) PRIMARY KEY
                        DEFAULT ('A' || nextval('attendance_seq')),

    employee_id         VARCHAR(20) NOT NULL,
    attendance_date     DATE NOT NULL,
    present             BOOLEAN NOT NULL,

    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(20) DEFAULT current_user,
	updated_by VARCHAR(20) DEFAULT current_user,

    CONSTRAINT fk_attendance_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(id),

    CONSTRAINT uq_employee_attendance
        UNIQUE (employee_id, attendance_date)
);
--drop table attendance;
