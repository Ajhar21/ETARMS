CREATE SEQUENCE task_id_seq START 1;

--dropping old constraint
ALTER TABLE tasks
DROP CONSTRAINT fk_tasks_manager;

--establishing relation with users table
ALTER TABLE tasks
ADD CONSTRAINT fk_tasks_manager
FOREIGN KEY (manager_id)
REFERENCES users(user_id);