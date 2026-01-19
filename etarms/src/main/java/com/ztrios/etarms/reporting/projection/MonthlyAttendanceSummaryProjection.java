package com.ztrios.etarms.reporting.projection;

public interface MonthlyAttendanceSummaryProjection {
    String getEmployeeId();
    String getEmployeeName();
    Long getPresentDays();
    Double getTotalHours();
}

