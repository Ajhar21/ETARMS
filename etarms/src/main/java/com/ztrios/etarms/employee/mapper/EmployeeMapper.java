package com.ztrios.etarms.employee.mapper;

import com.ztrios.etarms.employee.dto.EmployeeCreateRequest;
import com.ztrios.etarms.employee.dto.EmployeeResponse;
import com.ztrios.etarms.employee.entity.Department;
import com.ztrios.etarms.employee.entity.Employee;
import com.ztrios.etarms.employee.enums.EmploymentStatus;
import com.ztrios.etarms.employee.service.CloudinaryService;
import com.ztrios.etarms.employee.util.EmployeeIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    /**
     * DTO -> Entity
     * Department is resolved in service layer and injected here
     */
    private final EmployeeIdGenerator idGenerator;
    private final CloudinaryService cloudinaryService;

    public Employee mapToEntity(EmployeeCreateRequest request, Department department) {

        return new Employee(
                idGenerator.generate(),          // business identifier
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phoneNumber(),
                request.jobTitle(),
                EmploymentStatus.ACTIVE,
                department
        );
    }

    /**
     * Entity -> Response DTO (Builder-based)
     */
    public EmployeeResponse mapToResponse(Employee employee) {

        return EmployeeResponse.builder()
                .employeeId(employee.getEmployeeId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .jobTitle(employee.getJobTitle())
                .employmentStatus(employee.getEmploymentStatus())
                .departmentId(employee.getDepartment().getDepartmentId())
                .photoUrl(employee.getPhotoUrl())
                .thumbnailUrl(employee.getPhotoUrl() != null ? cloudinaryService.getThumbnailUrl(employee.getEmployeeId()) : null)
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .build();
    }
}
