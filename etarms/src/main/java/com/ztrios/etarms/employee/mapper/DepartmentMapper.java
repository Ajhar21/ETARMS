package com.ztrios.etarms.employee.mapper;

import com.ztrios.etarms.common.mapper.BaseMapper;
import com.ztrios.etarms.employee.dto.DepartmentRequest;
import com.ztrios.etarms.employee.dto.DepartmentResponse;
import com.ztrios.etarms.employee.entity.Department;
import com.ztrios.etarms.employee.util.DepartmentIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentMapper implements BaseMapper<Department, DepartmentResponse, DepartmentRequest> {

    private final DepartmentIdGenerator departmentIdGenerator;

    @Override
    public Department mapToEntity(DepartmentRequest request) {
        return new Department(
                departmentIdGenerator.generate(),
                request.name(),
                request.description());

    }

    @Override
    public DepartmentResponse mapToResponse(Department department) {
        return DepartmentResponse.builder()
                .departmentId(department.getDepartmentId())
                .name(department.getName())
                .description(department.getDescription())
                .build();
    }
}
