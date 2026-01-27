package com.ztrios.etarms.attendance.service;

import com.ztrios.etarms.attendance.dto.AttendanceRequest;
import com.ztrios.etarms.attendance.dto.AttendanceCheckInResponse;
import com.ztrios.etarms.attendance.dto.AttendanceRequest;
import com.ztrios.etarms.attendance.dto.AttendanceCheckOutResponse;
import com.ztrios.etarms.attendance.dto.AttendanceHistoryRequest;
import com.ztrios.etarms.attendance.dto.AttendanceHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttendanceService {

    /**
     * Daily Check-In
     * Input: employeeId (business ID)
     * Output: employeeId + checkInTime
     * Rules:
     * - Only if no open attendance exists today
     * - Employee must exist and be ACTIVE
     */
    AttendanceCheckInResponse checkIn(AttendanceRequest request);

    /**
     * Daily Check-Out
     * Input: employeeId (business ID)
     * Output: employeeId + checkInTime, checkOutTime, workingMinutes
     * Rules:
     * - Only if an open attendance exists
     * - Check-out must be after check-in
     */
    AttendanceCheckOutResponse checkOut(AttendanceRequest request);

    /**
     * Fetch attendance history
     * Input: employeeId + date range
     * Output: list of AttendanceHistoryResponse
     * Rules:
     * - Returns all attendance records in the date range
     * - Optional: sorted by attendanceDate ascending
     */
//    List<AttendanceHistoryResponse> getHistory(AttendanceHistoryRequest request);
    Page<AttendanceHistoryResponse> getHistory(AttendanceHistoryRequest request, Pageable pageable);
}
