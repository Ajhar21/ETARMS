package com.ztrios.etarms.tasks.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TaskCreateRequest(
        @NotNull
        String title,
        String description,
        @NotNull
        String assignedTo,
        @NotNull
        String priority,
        @NotNull
        LocalDateTime deadline
) {}
