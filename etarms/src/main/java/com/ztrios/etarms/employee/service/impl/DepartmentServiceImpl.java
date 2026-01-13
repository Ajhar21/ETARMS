package com.ztrios.etarms.employee.service.impl;

import com.ztrios.etarms.employee.dto.DepartmentRequest;
import com.ztrios.etarms.employee.dto.DepartmentResponse;
import com.ztrios.etarms.employee.entity.Department;
import com.ztrios.etarms.employee.repository.DepartmentRepository;
import com.ztrios.etarms.employee.service.DepartmentService;
import com.ztrios.etarms.employee.util.DepartmentIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;
    private final JdbcTemplate jdbcTemplate;

    // ===================== CREATE Department =====================
    @Override
    public DepartmentResponse create(DepartmentRequest request) {

        if (repository.existsByName(request.name())) {
            throw new RuntimeException("Department already exists");
        }

        Long seq = jdbcTemplate.queryForObject(
                "SELECT nextval('department_seq')", Long.class);

//        Department dept = Department.builder()
//                .departmentId(DepartmentIdGenerator.nextId(seq))
//                .name(request.getName())
//                .description(request.getDescription())
//                .build();
        Department dept = new Department(
                        DepartmentIdGenerator.nextId(seq),
                        request.name(),
                        request.description());

        repository.save(dept);
        return map(dept);
    }

    // ===================== GET Department By departmentId=====================
    @Override
    public DepartmentResponse getByDepartmentId(String departmentId) {
        return repository.findByDepartmentId(departmentId)
                .map(this::map)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    // ===================== GET ALL Departments =====================
    @Override
    public List<DepartmentResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    // ===================== UPDATE Department =====================
    @Override
    public DepartmentResponse update(String departmentId, DepartmentRequest request) {
        Department dept = repository.findByDepartmentId(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

//        dept.setName(request.getName());
//        dept.setDescription(request.getDescription());
        dept.update(request.name(),request.description());

        return map(dept);
    }

    // ===================== DELETE Department =====================
    @Override
    public void delete(String departmentId) {
        if (!repository.existsByDepartmentId(departmentId)) {
            throw new RuntimeException("Department not found");
        }
        repository.deleteByDepartmentId(departmentId);
    }

    private DepartmentResponse map(Department dept) {
        DepartmentResponse res = new DepartmentResponse();
        res.setDepartmentId(dept.getDepartmentId());
        res.setName(dept.getName());
        res.setDescription(dept.getDescription());
        return res;
    }
}
