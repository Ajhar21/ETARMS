package com.ztrios.etarms.attendance.dto;

import jakarta.validation.constraints.NotNull;

public record AttendanceRequest(
        @NotNull
        String employeeId    // business ID from client
) {
}