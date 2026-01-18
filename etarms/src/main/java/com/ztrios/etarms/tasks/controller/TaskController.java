package com.ztrios.etarms.tasks.controller;

import com.ztrios.etarms.tasks.dto.*;
import com.ztrios.etarms.tasks.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest request) {
        TaskResponse response = taskService.createTask(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/reassignment")
    public ResponseEntity<TaskReassignResponse> reassignTask(@Valid @RequestBody TaskReassignRequest request) {
        TaskReassignResponse response = taskService.reassignTask(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PatchMapping("/status")
    public ResponseEntity<TaskResponse> statusUpdate(@Valid @RequestBody TaskStatusUpdateReq request) {
        TaskResponse response = taskService.statusUpdate(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/employee/{employee_id}")
    public ResponseEntity<TaskResponse> getTasksByEmployee(@PathVariable String employee_id) {
        List<TaskResponse> response = taskService.getTasksByEmployee(employee_id);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager/{manager}")
    public ResponseEntity<TaskResponse> getTasksByManager(@PathVariable String manager) {
        List<TaskResponse> response = taskService.getTasksByManager(manager);
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
