package com.ztrios.etarms.tasks.mapper;

import com.ztrios.etarms.employee.entity.Employee;
import com.ztrios.etarms.identity.entity.User;
import com.ztrios.etarms.tasks.dto.TaskCreateRequest;
import com.ztrios.etarms.tasks.dto.TaskReassignResponse;
import com.ztrios.etarms.tasks.dto.TaskResponse;
import com.ztrios.etarms.tasks.entity.Task;
import com.ztrios.etarms.tasks.enums.TaskPriority;
import com.ztrios.etarms.tasks.enums.TaskStatus;
import com.ztrios.etarms.tasks.util.TaskIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    private final TaskIdGenerator taskIdGenerator;

    public Task mapToEntity(TaskCreateRequest request, Employee assignee, User manager) {
        return new Task(
                taskIdGenerator.nextTaskId(),
                request.title(),
                request.description(),
                TaskStatus.CREATED,        // default status
                TaskPriority.valueOf(request.priority()),
                request.deadline(),
                assignee,
                manager
        );
    }

    public TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getTaskId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getPriority().name(),
                task.getDeadline(),
                task.getAssignee().getEmployeeId(),      // business employee id
                task.getTaskManager().getUsername()      // manager username from User
        );
    }

    public TaskReassignResponse mapToReassignResponse(Task task, String previousAssignee, Employee newAssignee) {
        return new TaskReassignResponse(
                task.getTaskId(),
                previousAssignee,
                newAssignee.getEmployeeId(),
                task.getStatus().name(),
                task.getUpdatedBy(),
                task.getUpdatedAt()
        );
    }
}
