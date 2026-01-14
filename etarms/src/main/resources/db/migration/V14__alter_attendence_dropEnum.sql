--Postgres enum/type creating issue of type mismatching with java enum

--change type of column
ALTER TABLE attendance
ALTER COLUMN attendance_status TYPE VARCHAR(20) USING attendance_status::VARCHAR;

--drop enum type
DROP TYPE IF EXISTS attendance_status_type;
