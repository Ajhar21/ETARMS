package com.ztrios.etarms.employee.service.impl;

import com.ztrios.etarms.employee.dto.EmployeeCreateRequest;
import com.ztrios.etarms.employee.dto.EmployeeResponse;
import com.ztrios.etarms.employee.entity.Employee;
import com.ztrios.etarms.employee.enums.EmploymentStatus;
import com.ztrios.etarms.employee.repository.EmployeeRepository;
import com.ztrios.etarms.employee.service.EmployeeService;
import com.ztrios.etarms.employee.util.EmployeeIdGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeIdGenerator idGenerator;

    public EmployeeServiceImpl(EmployeeRepository repository,
                               EmployeeIdGenerator idGenerator) {
        this.repository = repository;
        this.idGenerator = idGenerator;
    }

    @Override
    public EmployeeResponse create(EmployeeCreateRequest request) {

        if (repository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (repository.existsByPhoneNumber(request.phoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        Employee employee = Employee.builder()
                .employeeId(idGenerator.generate())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .jobTitle(request.jobTitle())
                .departmentId(request.departmentId())
                .employmentStatus(EmploymentStatus.ACTIVE)
                .build();

        repository.save(employee);
        return mapToResponse(employee);
    }

    @Override
    public EmployeeResponse getById(String employeeId) {
        return repository.findById(employeeId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    @Override
    public List<EmployeeResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private EmployeeResponse mapToResponse(Employee e) {
        return new EmployeeResponse(
                e.getEmployeeId(),
                e.getFirstName(),
                e.getLastName(),
                e.getEmail(),
                e.getPhoneNumber(),
                e.getJobTitle(),
                e.getEmploymentStatus(),
                e.getDepartmentId(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }
}
