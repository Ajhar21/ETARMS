package com.ztrios.etarms.employee.dto;

import com.ztrios.etarms.employee.enums.EmploymentStatus;

import java.time.Instant;

public record EmployeeResponse(
        String employeeId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String jobTitle,
        EmploymentStatus employmentStatus,
        String departmentId,
        Instant createdAt,
        Instant updatedAt
) {}
