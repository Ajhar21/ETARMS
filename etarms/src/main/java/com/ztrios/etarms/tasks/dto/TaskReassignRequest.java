package com.ztrios.etarms.tasks.dto;

import jakarta.validation.constraints.NotNull;

public record TaskReassignRequest(
        @NotNull
        String taskId,  // existing task
        @NotNull
        String newAssignedTo    // new employee
) {}