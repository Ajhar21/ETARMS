package com.ztrios.etarms.employee.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record EmployeePageResponse(

        List<EmployeeResponse> content,
        long totalElements,

        int totalPages,

        int pageNumber,

        int pageSize,
        boolean last
) {
}
