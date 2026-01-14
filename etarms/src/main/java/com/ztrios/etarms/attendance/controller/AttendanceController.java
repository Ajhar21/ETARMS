package com.ztrios.etarms.attendance.controller;

import com.ztrios.etarms.attendance.dto.*;
import com.ztrios.etarms.attendance.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    /**
     * Daily Check-In
     */
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @PostMapping("/check-in")
    public ResponseEntity<AttendanceCheckInResponse> checkIn(
            @Valid @RequestBody AttendanceCheckInRequest request
    ) {
        AttendanceCheckInResponse response = attendanceService.checkIn(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Daily Check-Out
     */
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    @PostMapping("/check-out")
    public ResponseEntity<AttendanceCheckOutResponse> checkOut(
            @Valid @RequestBody AttendanceCheckOutRequest request
    ) {
        AttendanceCheckOutResponse response = attendanceService.checkOut(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Attendance History
     * Supports optional pagination via query params: page & size
     * Supports optional date range filters: startDate & endDate
     */
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/history")
    public ResponseEntity<Page<AttendanceHistoryResponse>> getHistory(
            @RequestParam String employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        AttendanceHistoryRequest request = new AttendanceHistoryRequest(employeeId, startDate, endDate);
        Pageable pageable = PageRequest.of(page, size);
        Page<AttendanceHistoryResponse> response = attendanceService.getHistory(request, pageable);
        return ResponseEntity.ok(response);
    }
}
