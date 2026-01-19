package com.ztrios.etarms.employee.controller;

import com.ztrios.etarms.employee.dto.EmployeeCreateRequest;
import com.ztrios.etarms.employee.dto.EmployeePageResponse;
import com.ztrios.etarms.employee.dto.EmployeeResponse;
import com.ztrios.etarms.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    // ===================== GET ALL Employees =====================
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public ResponseEntity<EmployeePageResponse> employeeList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        EmployeePageResponse response=service.getEmployees(page,size,sort);
        return ResponseEntity.ok(response);
    }

    // ===================== GET Employee By employeeId =====================
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/{employeeId}")
    public EmployeeResponse get(@PathVariable String employeeId) {
        return service.getByEmployeeId(employeeId);
    }

    // ===================== CREATE Employee =====================
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping
    public EmployeeResponse create(@Valid @RequestBody EmployeeCreateRequest request) {
        return service.create(request);
    }

    // ===================== UPDATE Employee =====================
    @PutMapping("/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeResponse> update(
            @PathVariable String employeeId,
            @Valid @RequestBody EmployeeCreateRequest request) {
        return ResponseEntity.ok(service.update(employeeId, request));
    }

    // ===================== DELETE Employee =====================
    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String employeeId) {
        service.delete(employeeId);
        return ResponseEntity.noContent().build();
    }

    // ===================== POST Employee Image=====================
    @PostMapping("/{id}/photo")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<String> uploadPhoto(
            @PathVariable("id") String employeeId,
            @RequestParam("file") MultipartFile file
    ) {
        String photoUrl = service.uploadEmployeePhoto(employeeId, file);
        return ResponseEntity.ok(photoUrl);
    }
}
