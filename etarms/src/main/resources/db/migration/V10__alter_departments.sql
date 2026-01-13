ALTER TABLE departments ALTER COLUMN department_id
    SET DEFAULT ('dep' || nextval('department_seq'));