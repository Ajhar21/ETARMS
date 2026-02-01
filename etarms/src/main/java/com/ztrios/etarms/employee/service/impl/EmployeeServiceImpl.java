package com.ztrios.etarms.employee.service.impl;

import com.ztrios.etarms.audit.model.AuditAction;
import com.ztrios.etarms.audit.service.AuditService;
import com.ztrios.etarms.common.exception.BusinessException;
import com.ztrios.etarms.common.exception.InvalidBusinessStateException;
import com.ztrios.etarms.common.exception.ResourceNotFoundException;
import com.ztrios.etarms.employee.dto.EmployeeCreateRequest;
import com.ztrios.etarms.employee.dto.EmployeePageResponse;
import com.ztrios.etarms.employee.dto.EmployeeResponse;
import com.ztrios.etarms.employee.entity.Department;
import com.ztrios.etarms.employee.entity.Employee;
import com.ztrios.etarms.employee.mapper.EmployeeMapper;
import com.ztrios.etarms.employee.repository.DepartmentRepository;
import com.ztrios.etarms.employee.repository.EmployeeRepository;
import com.ztrios.etarms.employee.service.CloudinaryService;
import com.ztrios.etarms.employee.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final CloudinaryService cloudinaryService;
    private final EmployeeRepository repository;
    private final DepartmentRepository departmentRepository;
    private final AuditService auditService;
    private final EmployeeMapper employeeMapper;

    // ===================== CREATE Employee =====================
    @Override
    public EmployeeResponse create(EmployeeCreateRequest request) {

        if (repository.existsByEmail(request.email())) {
            throw new InvalidBusinessStateException("Email already exists");
        }

        if (repository.existsByPhoneNumber(request.phoneNumber())) {
            throw new InvalidBusinessStateException("Phone number already exists");
        }
        Department department = departmentRepository
                .findByName(request.departmentName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invalid department Name: " + request.departmentName()
                        )
                );

        Employee employee = employeeMapper.mapToEntity(request, department);
        repository.save(employee);
        return employeeMapper.mapToResponse(employee);
    }

    // ===================== UPDATE Employee =====================
    @Override
    public EmployeeResponse update(String employeeId, EmployeeCreateRequest request) {
        Employee employee = repository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Department department = departmentRepository
                .findByName(request.departmentName())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid department Name: " + request.departmentName()));

        employee.update(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phoneNumber(),
                request.jobTitle(),
                department,
                request.employmentStatus());

        return employeeMapper.mapToResponse(employee);
    }

    // ===================== DELETE Employee =====================
    @Override
    public void delete(String employeeId) {
        Employee emp = repository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        repository.delete(emp);
    }

    // ===================== GET Employee By employeeId=====================
    @Override
    public EmployeeResponse getByEmployeeId(String employeeId) {
        return repository.findByEmployeeId(employeeId)
                .map(employeeMapper::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    // ===================== GET Employees Pagination=====================
    @Override
    public EmployeePageResponse getEmployees(int page, int size, String sort) {
        // parse sort string like "id,asc"
        String sortField = "id";
        Sort.Direction direction = Sort.Direction.ASC;

        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            if (parts.length > 0 && !parts[0].isBlank()) {
                sortField = parts[0];
            }
            if (parts.length > 1 && !parts[1].isBlank()) {
                direction = Sort.Direction.fromString(parts[1]);
            }
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<Employee> employeePage = repository.findAll(pageable);

        List<EmployeeResponse> content = employeePage.getContent()
                .stream()
                .map(employeeMapper::mapToResponse)
                .collect(Collectors.toList());

        return EmployeePageResponse.builder()
                .content(content)
                .totalElements(employeePage.getTotalElements())
                .totalPages(employeePage.getTotalPages())
                .pageNumber(employeePage.getNumber())
                .pageSize(employeePage.getSize())
                .last(employeePage.isLast())
                .build();
    }

    // ===================== POST Upload Employee Photo=====================
    @Transactional
    public String uploadEmployeePhoto(String employeeId, MultipartFile file) {
        Employee employee = repository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        //business rules
        if (!repository.isActive(employeeId)) {
            throw new BusinessException("Cannot upload photo for inactive/Suspended/terminated employee");
        }

        cloudinaryService.validateImage(file);
        String photoUrl = cloudinaryService.uploadEmployeeImage(employeeId, file);
        employee.setPhotoUrl(photoUrl);
        repository.save(employee);

        return photoUrl;
    }
}
