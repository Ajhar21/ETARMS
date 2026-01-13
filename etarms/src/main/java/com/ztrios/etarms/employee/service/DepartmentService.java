package com.ztrios.etarms.employee.service;

import com.ztrios.etarms.employee.dto.DepartmentRequest;
import com.ztrios.etarms.employee.dto.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse create(DepartmentRequest request);

//    DepartmentResponse getById(String departmentId);

    DepartmentResponse getByDepartmentId(String departmentId);

    List<DepartmentResponse> getAll();

    DepartmentResponse update(String departmentId, DepartmentRequest request);

    void delete(String departmentId);
}
