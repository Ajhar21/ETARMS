package com.ztrios.etarms.employee.util;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentIdGenerator {

    private final JdbcTemplate jdbcTemplate;

    public String generate() {
        Long seq = jdbcTemplate.queryForObject("SELECT nextval('department_seq')", Long.class);
        return "dep" + String.format("%03d", seq);
    }
}