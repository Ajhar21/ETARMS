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

    @Override
    public DepartmentResponse create(DepartmentRequest request) {

        if (repository.existsByName(request.getName())) {
            throw new RuntimeException("Department already exists");
        }

        Long seq = jdbcTemplate.queryForObject(
                "SELECT nextval('department_seq')", Long.class);

        Department dept = Department.builder()
                .departmentId(DepartmentIdGenerator.nextId(seq))
                .name(request.getName())
                .description(request.getDescription())
                .build();

        repository.save(dept);
        return map(dept);
    }

    @Override
    public DepartmentResponse getById(String id) {
        return repository.findById(id)
                .map(this::map)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    @Override
    public List<DepartmentResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public DepartmentResponse update(String id, DepartmentRequest request) {
        Department dept = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        dept.setName(request.getName());
        dept.setDescription(request.getDescription());

        return map(dept);
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Department not found");
        }
        repository.deleteById(id);
    }

    private DepartmentResponse map(Department dept) {
        DepartmentResponse res = new DepartmentResponse();
        res.setDepartmentId(dept.getDepartmentId());
        res.setName(dept.getName());
        res.setDescription(dept.getDescription());
        return res;
    }
}
