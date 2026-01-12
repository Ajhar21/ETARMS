package com.ztrios.etarms.employee.controller;

import com.ztrios.etarms.employee.dto.EmployeeCreateRequest;
import com.ztrios.etarms.employee.dto.EmployeeResponse;
import com.ztrios.etarms.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping
    public EmployeeResponse create(@Valid @RequestBody EmployeeCreateRequest request) {
        return service.create(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/{id}")
    public EmployeeResponse get(@PathVariable String id) {
        return service.getById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping
    public List<EmployeeResponse> list() {
        return service.getAll();
    }
}
