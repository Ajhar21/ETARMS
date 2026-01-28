package com.ztrios.etarms.tasks.service.impl;

import com.ztrios.etarms.audit.config.AuditorAwareImpl;
import com.ztrios.etarms.audit.model.AuditAction;
import com.ztrios.etarms.audit.service.AuditService;
import com.ztrios.etarms.common.exception.BusinessException;
import com.ztrios.etarms.common.exception.ResourceNotFoundException;
import com.ztrios.etarms.employee.entity.Employee;
import com.ztrios.etarms.employee.repository.EmployeeRepository;
import com.ztrios.etarms.identity.entity.User;
import com.ztrios.etarms.identity.repository.UserRepository;
import com.ztrios.etarms.tasks.dto.*;
import com.ztrios.etarms.tasks.entity.Task;
import com.ztrios.etarms.tasks.enums.TaskStatus;
import com.ztrios.etarms.tasks.mapper.TaskMapper;
import com.ztrios.etarms.tasks.repository.TaskRepository;
import com.ztrios.etarms.tasks.service.TaskService;
import com.ztrios.etarms.tasks.util.TaskIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final TaskIdGenerator taskIdGenerator;
    private final AuditorAwareImpl auditorAwareImpl;
    private final AuditService auditService;
    private final TaskMapper taskMapper;

    @Transactional
    @Override
    public TaskResponse createTask(TaskCreateRequest request) {
        // Fetch assignee
        Employee assignee = employeeRepository.findByEmployeeId(request.assignedTo())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid assignedTo(employee)"));

        // Fetch manager from JWT
        String managerUsername = auditorAwareImpl.getCurrentAuditor()
                .orElseThrow(() -> new ResourceNotFoundException("No authenticated user found"));
        User manager = userRepository.findByUsername(managerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid manager from JWT"));

        // Generate taskId
        String taskId = taskIdGenerator.nextTaskId();

        Task task = taskMapper.mapToEntity(request, assignee, manager);

        taskRepository.save(task);

        auditService.log(
                AuditAction.CREATE_TASK,
                "Task",
                task.getId().toString(),
                "Task created with title: " + task.getTitle()
        );

        return taskMapper.mapToResponse(task);
    }

    @Transactional
    @Override
    public TaskReassignResponse reassignTask(TaskReassignRequest request) {

        // Fetch task
        Task task = taskRepository.findByTaskId(request.taskId())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid taskId, no task found by taskId"));

        // Check status
        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new BusinessException("Cannot reassign a COMPLETED task");
        }

        // Fetch new assignee
        Employee newAssignee = employeeRepository.findByEmployeeId(request.newAssignedTo())
                .orElseThrow(() -> new ResourceNotFoundException("New Assignee(employee) not found"));

        // Save previous assignee
        String previousAssignee = task.getAssignee().getEmployeeId();

        // Update assignee
        task.setNewAssignee(newAssignee);

        // Save task
        taskRepository.save(task);

        auditService.log(
                AuditAction.REASSIGN_TASK,
                "Task",
                task.getId().toString(),
                "Task reassigned from " + previousAssignee + " to " + newAssignee.getEmployeeId()
        );

        return taskMapper.mapToReassignResponse(task, previousAssignee, newAssignee);
    }

    @Transactional
    @Override
    public TaskResponse statusUpdate(TaskStatusUpdateReq request) {

        // Fetch task
        Task task = taskRepository.findByTaskId(request.taskId())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid taskId, no task found by taskId"));

        // Check status
        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new BusinessException("Cannot update a COMPLETED task");
        } else {
            task.updateStatus(TaskStatus.valueOf(request.status()));
        }

        taskRepository.save(task);

        auditService.log(
                AuditAction.UPDATE_TASK_STATUS,
                "Task",
                task.getId().toString(),
                "Task status updated to " + task.getStatus()
        );

        return taskMapper.mapToResponse(task);
    }

    @Override
    public List<TaskResponse> getTasksByEmployee(String employeeId) {

        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found: " + employeeId)
                );

        return taskRepository.findByAssigneeWithUsers(employee)
                .stream()
                .map(taskMapper::mapToResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> getTasksByManager(String manager) {

        User user = userRepository.findByUsername(manager)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Manager not exists in user: " + manager)
                );

        return taskRepository.findByTaskManager(user)
                .stream()
                .map(taskMapper::mapToResponse)
                .toList();
    }
}
