package com.ztrios.etarms.employee.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class DepartmentResponse {
    private String departmentId;
    private String name;
    private String description;

//    public DepartmentResponse(){};
}
