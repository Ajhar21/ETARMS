package com.ztrios.etarms.employee.controller;

import com.ztrios.etarms.common.response.ApiResponse;
import com.ztrios.etarms.employee.dto.EmployeeCreateRequest;
import com.ztrios.etarms.employee.dto.EmployeePageResponse;
import com.ztrios.etarms.employee.dto.EmployeeResponse;
import com.ztrios.etarms.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<ApiResponse<EmployeePageResponse>> employeeList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        EmployeePageResponse response = service.getEmployees(page, size, sort);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Employees page retrieved successfully",
                        response));
    }

    // ===================== GET Employee By employeeId =====================
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> get(@PathVariable String employeeId) {
        EmployeeResponse response = service.getByEmployeeId(employeeId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Employee retrieved successfully",
                        response));
    }

    // ===================== CREATE Employees =====================
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(
            @RequestBody @Valid EmployeeCreateRequest request
    ) {
        EmployeeResponse response = service.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "Employee created successfully",
                        response
                ));
    }

    // ===================== UPDATE Employee =====================
    @PutMapping("/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<EmployeeResponse>> update(
            @PathVariable String employeeId,
            @Valid @RequestBody EmployeeCreateRequest request) {

        EmployeeResponse response = service.update(employeeId, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Employee Updated successfully",
                        response
                ));
    }

    // ===================== DELETE Employee =====================
    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable String employeeId) {
        service.delete(employeeId);
        return ResponseEntity.noContent().build();
    }

    // ===================== POST Employee Image=====================
    @PostMapping(value = "/{id}/photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)     // adding this attribute to make swagger support
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<String>> uploadPhoto(
            @PathVariable("id") String employeeId,
            @Valid @RequestParam("file") MultipartFile file     //never use @RequestBody here, because swagger won't detect it as form-data
    ) {
        String photoUrl = service.uploadEmployeePhoto(employeeId, file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        HttpStatus.OK.value(),
                        "Employee Image Uploaded Successfully",
                        photoUrl
                ));
    }
}
