package com.ztrios.etarms.attendance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceHistoryResponse(
        String employeeId,     // business ID
        LocalDate attendanceDate,
        LocalDateTime checkInTime,
        LocalDateTime checkOutTime,
        Integer workingMinutes, //type change for null value
        String attendanceStatus
) {
}
