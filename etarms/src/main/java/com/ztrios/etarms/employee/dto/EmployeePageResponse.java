package com.ztrios.etarms.employee.dto;

import java.util.List;

public record EmployeePageResponse(

        List<EmployeeResponse> content,
        long totalElements,

        int totalPages,

        int pageNumber,

        int pageSize,
        boolean last
) {}
