package com.ztrios.etarms.attendance.dto;

import java.time.LocalDateTime;

public record AttendanceCheckOutResponse(
        String employeeId,   // business ID
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime,
        int workingMinutes
) {}
