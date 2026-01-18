package com.ztrios.etarms.tasks.service.impl;

import com.ztrios.etarms.config.AuditorAwareImpl;
import com.ztrios.etarms.employee.entity.Employee;
import com.ztrios.etarms.employee.repository.EmployeeRepository;
import com.ztrios.etarms.identity.entity.User;
import com.ztrios.etarms.identity.repository.UserRepository;
import com.ztrios.etarms.tasks.dto.*;
import com.ztrios.etarms.tasks.entity.Task;
import com.ztrios.etarms.tasks.enums.TaskPriority;
import com.ztrios.etarms.tasks.enums.TaskStatus;
import com.ztrios.etarms.tasks.repository.TaskRepository;
import com.ztrios.etarms.tasks.service.TaskService;
import com.ztrios.etarms.tasks.util.TaskIdGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final TaskIdGenerator taskIdGenerator;
    private final AuditorAwareImpl auditorAwareImpl;

    public TaskServiceImpl(TaskRepository taskRepository,
                       EmployeeRepository employeeRepository,
                       UserRepository userRepository,
                       TaskIdGenerator taskIdGenerator, AuditorAwareImpl auditorAwareImpl) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.taskIdGenerator = taskIdGenerator;
        this.auditorAwareImpl = auditorAwareImpl;
    }

    @Transactional
    @Override
    public TaskResponse createTask(TaskCreateRequest request) {
        // Fetch assignee
        Employee assignee = employeeRepository.findByEmployeeId(request.assignedTo())
                .orElseThrow(() -> new IllegalArgumentException("Invalid assigned_to employee"));

        // Fetch manager from JWT
//        String managerUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        String managerUsername  = auditorAwareImpl.getCurrentAuditor()
                .orElseThrow(() -> new IllegalArgumentException("No authenticated user found"));
        User manager = userRepository.findByUsername(managerUsername)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manager from JWT"));

        // Generate taskId
        String taskId = taskIdGenerator.nextTaskId();

        // Set defaults
        Task task = new Task(
                taskId,
                request.title(),
                request.description(),
                TaskStatus.CREATED,        // default status
//                TaskPriority.MEDIUM,       // default priority
                TaskPriority.valueOf(request.priority()),
                request.deadline(),
                assignee,
                manager
        );

        taskRepository.save(task);

        // Build response
//        return new TaskResponse(
//                task.getTaskId(),
//                task.getTitle(),
//                task.getDescription(),
//                task.getStatus().name(),
//                task.getPriority().name(),
//                task.getDeadline(),
//                task.getAssignee().getEmployeeId(),
//                task.getTaskManager().getUsername()
//        );
        return map(task);
    }

    @Transactional
    @Override
    public TaskReassignResponse reassignTask(TaskReassignRequest request) {
        // Fetch task
//        Task task = taskRepository.findById(taskRepository.findId(request.taskId()))
        Task task = taskRepository.findByTaskId(request.taskId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid taskId, no task found by taskId"));

        // Check status
        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new IllegalStateException("Cannot reassign a COMPLETED task");
        }

        // Fetch new assignee
        Employee newAssignee = employeeRepository.findByEmployeeId(request.newAssignedTo())
                .orElseThrow(() -> new IllegalArgumentException("Assigned employee not found"));

        // Save previous assignee
        String previousAssignee = task.getAssignee().getEmployeeId();

        // Update assignee
        task.setNewAssignee(newAssignee);

        // Save task
        taskRepository.save(task);

        return new TaskReassignResponse(
                task.getTaskId(),
                previousAssignee,
                newAssignee.getEmployeeId(),
                task.getStatus().name(),
                task.getUpdatedBy(),
                task.getUpdatedAt()
        );
    }

    @Transactional
    @Override
    public TaskResponse statusUpdate(TaskStatusUpdateReq request) {
        // Fetch task
//        Task task = taskRepository.findById(taskRepository.findId(request.taskId()))
        Task task = taskRepository.findByTaskId(request.taskId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid taskId, no task found by taskId"));

        // Check status
        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new IllegalStateException("Cannot update a COMPLETED task");
        }
        else {
            task.updateStatus(TaskStatus.valueOf(request.status()));
        }

        taskRepository.save(task);

        // Build response
//        return new TaskResponse(
//                task.getTaskId(),
//                task.getTitle(),
//                task.getDescription(),
//                task.getStatus().name(),
//                task.getPriority().name(),
//                task.getDeadline(),
//                task.getAssignee().getEmployeeId(),
//                task.getTaskManager().getUsername()
//        );
        return map(task);
    }

    @Override
    public List<TaskResponse> getTasksByEmployee(String employeeId) {

        Employee employee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Employee not found: " + employeeId)
                );

        return taskRepository.findByAssignee(employee)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<TaskResponse> getTasksByManager(String manager) {

        User user = userRepository.findByUsername(manager)
                .orElseThrow(() ->
                        new IllegalArgumentException("Manager not exists in user: " + manager)
                );

        return taskRepository.findByTaskManager(user)
                .stream()
                .map(this::map)
                .toList();
    }

    private TaskResponse map(Task task) {
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

}
