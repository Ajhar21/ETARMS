package com.ztrios.etarms.employee.service.impl;

import com.ztrios.etarms.audit.model.AuditAction;
import com.ztrios.etarms.audit.service.AuditService;
import com.ztrios.etarms.common.exception.BusinessException;
import com.ztrios.etarms.common.exception.InvalidBusinessStateException;
import com.ztrios.etarms.common.exception.InvalidFileException;
import com.ztrios.etarms.common.exception.ResourceNotFoundException;
import com.ztrios.etarms.employee.dto.EmployeeCreateRequest;
import com.ztrios.etarms.employee.dto.EmployeePageResponse;
import com.ztrios.etarms.employee.dto.EmployeeResponse;
import com.ztrios.etarms.employee.entity.Department;
import com.ztrios.etarms.employee.entity.Employee;
import com.ztrios.etarms.employee.enums.EmploymentStatus;
import com.ztrios.etarms.employee.repository.DepartmentRepository;
import com.ztrios.etarms.employee.repository.EmployeeRepository;
import com.ztrios.etarms.employee.service.CloudinaryService;
import com.ztrios.etarms.employee.service.EmployeeService;
import com.ztrios.etarms.employee.util.EmployeeIdGenerator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final CloudinaryService cloudinaryService;
    private final EmployeeRepository repository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeIdGenerator idGenerator;
    private final AuditService auditService;

    public EmployeeServiceImpl(CloudinaryService cloudinaryService, EmployeeRepository repository, DepartmentRepository departmentRepository,
                               EmployeeIdGenerator idGenerator, AuditService auditService) {
        this.cloudinaryService = cloudinaryService;
        this.repository = repository;
        this.departmentRepository = departmentRepository;
        this.idGenerator = idGenerator;
        this.auditService = auditService;
    }

    // ===================== CREATE Employee =====================
    @Override
    public EmployeeResponse create(EmployeeCreateRequest request) {

        if (repository.existsByEmail(request.email())) {

//            throw new IllegalArgumentException("Email already exists");
            throw new InvalidBusinessStateException("Email already exists");
        }

        if (repository.existsByPhoneNumber(request.phoneNumber())) {
//            throw new IllegalArgumentException("Phone number already exists");
            throw new InvalidBusinessStateException("Phone number already exists");
        }
        Department department = departmentRepository
//                .findById(request.departmentId())
                .findByName(request.departmentName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invalid department Name: " + request.departmentName()
                        )
                );

        Employee employee = new Employee(
                idGenerator.generate(),
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phoneNumber(),
                request.jobTitle(),
                EmploymentStatus.ACTIVE,
                department
        );

        repository.save(employee);

//        auditService.log(
//                AuditAction.CREATE_EMPLOYEE,
//                "Employee",
//                employee.getEmployeeId(),
//                "Employee created with name " + employee.getFirstName()+" "+ employee.getLastName()
//        );
//
        return mapToResponse(employee);
    }

    // ===================== UPDATE Employee =====================
    @Override
    public EmployeeResponse update(String employeeId, EmployeeCreateRequest request) {
        Employee emp = repository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Department department = departmentRepository
//                .findById(request.departmentId())
                .findByName(request.departmentName())
                .orElseThrow(() ->
//                        new IllegalArgumentException(
//                                "Invalid department Name: " + request.departmentName()
                        new ResourceNotFoundException("Invalid department Name: " + request.departmentName()));

        emp.update(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phoneNumber(),
                request.jobTitle(),
                department,
                request.employmentStatus());

//        auditService.log(
//                AuditAction.UPDATE_EMPLOYEE,
//                "Employee",
//                emp.getEmployeeId(),
//                "Employee updated: " + emp.getFirstName()+" "+ emp.getLastName()
//        );

        return mapToResponse(emp);
    }

    // ===================== DELETE Employee =====================
    @Override
    public void delete(String employeeId) {
        Employee emp = repository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

//        auditService.log(
//                AuditAction.DELETE_EMPLOYEE,
//                "Employee",
//                emp.getEmployeeId(),
//                "Employee deleted: " + emp.getFirstName()+" "+ emp.getLastName()
//        );

        repository.delete(emp);

//        repository.deleteByEmployeeId(employeeId);
    }

    // ===================== GET Employee By employeeId=====================
    @Override
    public EmployeeResponse getByEmployeeId(String employeeId) {
        return repository.findByEmployeeId(employeeId)
                .map(this::mapToResponse)
//                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
    }

    // ===================== GET Employees Pagination=====================
//    @Transactional(readOnly = true)
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
                .map(this::mapToResponse)
                .collect(Collectors.toList());


        return new EmployeePageResponse(
                content,
                employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.isLast()
        );
    }

    @Transactional
    public String uploadEmployeePhoto(String employeeId, MultipartFile file) {
        Employee employee = repository.findByEmployeeId(employeeId)
//                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        //business rules
        if (!repository.isActive(employeeId)) {
            throw new BusinessException("Cannot upload photo for inactive/Suspended/terminated employee");
        }

        validateImage(file);

        log.warn("after validate {}", file);


        String photoUrl = cloudinaryService.uploadEmployeeImage(employeeId, file);

        employee.setPhotoUrl(photoUrl);
//        thumbnailUrl = photoUrl != null ? cloudinaryService.getThumbnailUrl(employeeId) : null;
        repository.save(employee);

        auditService.log(
                AuditAction.UPLOAD_EMPLOYEE_PHOTO,
                "Employee",
                employee.getEmployeeId(),
                "Employee photo uploaded" + employee.getFirstName() + " " + employee.getLastName()
        );

        return photoUrl;
    }

//    private void validateImage(MultipartFile file) {
//        if (file.isEmpty()) {
////            throw new IllegalArgumentException("File is empty");
//            throw new InvalidFileException("File is empty");
//        }
//
//        if (file.getSize() > 2 * 1024 * 1024) { // 2MB limit
////            throw new IllegalArgumentException("File size exceeds 2MB");
//            throw new InvalidFileException("File size exceeds 2MB");
//        }
//
//        String contentType = file.getContentType();
//        log.warn("contentType : {}", contentType);
//
//        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {

    /// /            throw new IllegalArgumentException("Only JPEG or PNG images are allowed");
//            throw new InvalidFileException("Only JPEG or PNG images are allowed");
//        }
//    }
    private void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("File is empty");
        }

        if (file.getSize() > 2 * 1024 * 1024) {
            throw new InvalidFileException("File size exceeds 2MB");
        }

        try (InputStream is = file.getInputStream()) {
            Tika tika = new Tika();
            String detectedType = tika.detect(is);

            log.warn("Detected content type: {}", detectedType);

            if (!"image/jpeg".equals(detectedType) && !"image/png".equals(detectedType)) {
                throw new InvalidFileException("Only JPEG or PNG images are allowed");
            }
        } catch (IOException e) {
            throw new InvalidFileException("Failed to read file content");
        }
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
//                e.getDepartmentId(),
                e.getDepartment().getDepartmentId(),
                e.getPhotoUrl(),
//                thumbnailUrl,
                e.getPhotoUrl() != null ? cloudinaryService.getThumbnailUrl(e.getEmployeeId()) : null,
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }


}
