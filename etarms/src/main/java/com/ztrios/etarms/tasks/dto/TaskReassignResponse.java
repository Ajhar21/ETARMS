package com.ztrios.etarms.tasks.dto;

public record TaskReassignResponse(
        String taskId,
        String previousAssignedTo,
        String newAssignedTo,
        String status,
        String updatedBy,
        java.time.LocalDateTime updatedAt
) {}
