package com.ztrios.etarms.employee.service.impl;

import com.ztrios.etarms.audit.service.AuditService;
import com.ztrios.etarms.common.exception.InvalidBusinessStateException;
import com.ztrios.etarms.common.exception.ResourceNotFoundException;
import com.ztrios.etarms.employee.dto.DepartmentRequest;
import com.ztrios.etarms.employee.dto.DepartmentResponse;
import com.ztrios.etarms.employee.entity.Department;
import com.ztrios.etarms.employee.mapper.DepartmentMapper;
import com.ztrios.etarms.employee.repository.DepartmentRepository;
import com.ztrios.etarms.employee.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor    //generates a constructor with all final fields
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;
    private final AuditService auditService;
    private final DepartmentMapper departmentMapper;

    // ===================== CREATE Department =====================
    @Override
    public DepartmentResponse create(DepartmentRequest request) {

        if (repository.existsByName(request.name())) {
            throw new InvalidBusinessStateException("Department already exists");
        }

        Department dept = departmentMapper.mapToEntity(request);

        repository.save(dept);

        return departmentMapper.mapToResponse(dept);
    }

    // ===================== GET Department By departmentId=====================
    @Override
    public DepartmentResponse getByDepartmentId(String departmentId) {

        return repository.findByDepartmentId(departmentId)
                .map(departmentMapper::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

    }

    // ===================== GET ALL Departments =====================
    @Override
    public List<DepartmentResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(departmentMapper::mapToResponse)
                .toList();
    }

    // ===================== UPDATE Department =====================
    @Override
    public DepartmentResponse update(String departmentId, DepartmentRequest request) {

        Department dept = repository.findByDepartmentId(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        dept.update(request.name(), request.description());

        return departmentMapper.mapToResponse(dept);
    }

    // ===================== DELETE Department =====================
    @Override
    public void delete(String departmentId) {

        Department dept = repository.findByDepartmentId(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        repository.delete(dept);
    }
}
