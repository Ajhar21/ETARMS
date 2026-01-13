package com.ztrios.etarms.employee.service;

import com.ztrios.etarms.employee.dto.EmployeeCreateRequest;
import com.ztrios.etarms.employee.dto.EmployeePageResponse;
import com.ztrios.etarms.employee.dto.EmployeeResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse create(EmployeeCreateRequest request);

    EmployeeResponse update(String employeeId, EmployeeCreateRequest request);

    void delete(String employeeId);

    EmployeeResponse getByEmployeeId(String employeeId);

    EmployeePageResponse getEmployees(int page, int size, String sort);
}
