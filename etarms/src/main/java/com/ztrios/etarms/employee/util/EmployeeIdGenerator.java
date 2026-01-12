package com.ztrios.etarms.employee.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmployeeIdGenerator {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeIdGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String generate() {
        Long seq = jdbcTemplate.queryForObject(
                "SELECT nextval('employee_seq')", Long.class
        );
        return "emp" + String.format("%03d", seq);
    }
}
