package com.ztrios.etarms.attendance.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AttendanceCheckInResponse(
        String employeeId,   // business ID
        LocalDateTime checkInTime
) {
}