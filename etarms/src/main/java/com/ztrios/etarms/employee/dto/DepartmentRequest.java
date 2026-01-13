package com.ztrios.etarms.employee.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartmentRequest (
    @NotBlank String name,
    @NotBlank String description
) {}

