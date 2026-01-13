package com.ztrios.etarms.employee.dto;

import com.ztrios.etarms.employee.enums.EmploymentStatus;
import jakarta.validation.constraints.*;


public record EmployeeCreateRequest(

        @NotBlank String firstName,
        @NotBlank String lastName,

        @Email @NotBlank String email,

        @NotBlank String phoneNumber,

        @NotBlank String jobTitle,

//        @NotBlank String departmentId
        @NotBlank String departmentName, // or departmentId (public)
        @NotNull EmploymentStatus employmentStatus
) {}
