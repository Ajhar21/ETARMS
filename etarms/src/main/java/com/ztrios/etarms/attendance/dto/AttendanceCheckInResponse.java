package com.ztrios.etarms.attendance.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AttendanceCheckInResponse(
        String employeeId,   // business ID
        LocalDateTime checkInTime
) {}