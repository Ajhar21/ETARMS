package com.ztrios.etarms.tasks.repository;

import com.ztrios.etarms.employee.entity.Employee;
import com.ztrios.etarms.identity.entity.User;
import com.ztrios.etarms.tasks.entity.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    Optional<Task> findByTaskId(String taskId);

    /*** Avoid N+1 query problem ***/
    @EntityGraph(attributePaths = {"assignee", "taskManager"})
    List<Task> findByTaskManager(User user);

    /*** Avoid N+1 query problem ***/
    @Query("""
                SELECT t FROM Task t
                JOIN FETCH t.assignee
                JOIN FETCH t.taskManager
                WHERE t.assignee = :employee
            """)
    List<Task> findByAssigneeWithUsers(Employee employee);

}
