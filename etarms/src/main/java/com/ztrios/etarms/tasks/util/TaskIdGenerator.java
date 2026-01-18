package com.ztrios.etarms.tasks.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskIdGenerator {

    private static final String PREFIX = "TASK";
    private static final int NUM_DIGITS = 5; // TASK00001

    private final JdbcTemplate jdbcTemplate;

    public TaskIdGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Generate next task_id in format TASK00001 using PostgreSQL sequence
     */
    public String nextTaskId() {
        // Get next value from DB sequence
        Long seq = jdbcTemplate.queryForObject("SELECT nextval('task_id_seq')", Long.class);

        // Format with prefix and leading zeros
        return PREFIX + String.format("%0" + NUM_DIGITS + "d", seq);
    }
}
