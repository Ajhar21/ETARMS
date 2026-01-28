package com.ztrios.etarms.tasks.controller;

import com.ztrios.etarms.common.response.ApiResponse;
import com.ztrios.etarms.tasks.dto.*;
import com.ztrios.etarms.tasks.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@Valid @RequestBody TaskCreateRequest request) {
        TaskResponse response = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(),
                        "Task created successfully",
                        response));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/reassignment")
    public ResponseEntity<ApiResponse<TaskReassignResponse>> reassignTask(@Valid @RequestBody TaskReassignRequest request) {
        TaskReassignResponse response = taskService.reassignTask(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(),
                        "Task successfully reassigned",
                        response));
    }

    @PreAuthorize("hasAnyRole('MANAGER','EMPLOYEE')")
    @PatchMapping("/status")
    public ResponseEntity<ApiResponse<TaskResponse>> statusUpdate(@Valid @RequestBody TaskStatusUpdateReq request) {
        TaskResponse response = taskService.statusUpdate(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(),
                        "Task status updated successfully",
                        response));
    }

    @PreAuthorize("hasAnyRole('MANAGER','EMPLOYEE')")
    @GetMapping("/employee/{employee_id}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByEmployee(@PathVariable String employee_id) {
        List<TaskResponse> response = taskService.getTasksByEmployee(employee_id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(),
                        "Tasks retrieved for employee",
                        response));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager/{manager}")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByManager(@PathVariable String manager) {
        List<TaskResponse> response = taskService.getTasksByManager(manager);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(),
                        "Tasks retrieved for manager",
                        response));
    }
}
