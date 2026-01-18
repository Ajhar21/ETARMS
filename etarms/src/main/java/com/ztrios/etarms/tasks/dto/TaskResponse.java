package com.ztrios.etarms.tasks.dto;

import java.time.LocalDateTime;

public record TaskResponse(
        String taskId,
        String title,
        String description,
        String status,
        String priority,
        LocalDateTime deadline,
        String assignedTo,
        String managerUserName
) {}
