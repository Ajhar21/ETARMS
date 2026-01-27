package com.ztrios.etarms.employee.controller;

import com.ztrios.etarms.common.response.ApiResponse;
import com.ztrios.etarms.employee.dto.DepartmentRequest;
import com.ztrios.etarms.employee.dto.DepartmentResponse;
import com.ztrios.etarms.employee.service.DepartmentService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService service;

//    public DepartmentController(DepartmentService service) {
//        this.service = service;
//    }     //served by @RequiredArgsConstructor

    // ===================== GET ALL Departments =====================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> depList() {

        List<DepartmentResponse> response = service.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(),
                        "Departments retrieved successfully",
                        response));
    }

    // ===================== GET Department By departmentId=====================
    @GetMapping("/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> get(@PathVariable String departmentId) {
        DepartmentResponse response = service.getByDepartmentId(departmentId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(),
                        "Department retrieved successfully",
                        response));
    }

    // ===================== CREATE Department=====================
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> create(
            @Valid @RequestBody DepartmentRequest request) {

        DepartmentResponse response = service.create(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED.value(),
                        "Department created successfully",
                        response));
    }

    // ===================== UPDATE Department=====================
    @PutMapping("/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> update(
            @PathVariable
            @Pattern(regexp = "dep\\d{3}", message = "Invalid department ID") String departmentId,  //not good practice to use it for path variable
            @Valid @RequestBody DepartmentRequest request) {

        DepartmentResponse response = service.update(departmentId, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK.value(),
                        "Department updated successfully",
                        response));
    }

    // ===================== DELETE Department=====================
    @DeleteMapping("/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable String departmentId) {
        service.delete(departmentId);
        return ResponseEntity.noContent().build();
    }
}
