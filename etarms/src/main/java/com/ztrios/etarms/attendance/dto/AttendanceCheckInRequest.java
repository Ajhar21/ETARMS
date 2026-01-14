package com.ztrios.etarms.attendance.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AttendanceCheckInRequest(
        @NotNull
        String employeeId    // business ID from client
) {}