package com.ztrios.etarms.attendance.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AttendanceHistoryRequest(
        @NotNull
        String employeeId,     // business ID
        LocalDate startDate,
        LocalDate endDate
) {}

