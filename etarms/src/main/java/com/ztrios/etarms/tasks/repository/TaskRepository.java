package com.ztrios.etarms.tasks.repository;

import com.ztrios.etarms.employee.entity.Employee;
import com.ztrios.etarms.identity.entity.User;
import com.ztrios.etarms.tasks.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    boolean existsByTaskId(String taskId);

    Optional<Task> findByTaskId(String taskId);

    List<Task> findByAssignee(Employee employee);

    List<Task> findByTaskManager(User user);
}
