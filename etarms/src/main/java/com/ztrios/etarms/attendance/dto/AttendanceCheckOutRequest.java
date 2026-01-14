package com.ztrios.etarms.attendance.dto;

import jakarta.validation.constraints.NotNull;

public record AttendanceCheckOutRequest(
        @NotNull
        String employeeId    // business ID from client
) {}