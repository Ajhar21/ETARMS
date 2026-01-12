package com.ztrios.etarms.employee.service;

import com.ztrios.etarms.employee.dto.EmployeeCreateRequest;
import com.ztrios.etarms.employee.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse create(EmployeeCreateRequest request);

    EmployeeResponse getById(String employeeId);

    List<EmployeeResponse> getAll();
}
