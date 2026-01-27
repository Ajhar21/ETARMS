package com.ztrios.etarms.attendance.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AttendanceCheckOutResponse(
        String employeeId,   // business ID
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime,
        int workingMinutes
) {
}
