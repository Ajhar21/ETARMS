package com.ztrios.etarms.tasks.dto;

import jakarta.validation.constraints.NotNull;

public record TaskStatusUpdateReq(
    @NotNull
    String taskId,
    @NotNull
    String status
){}
