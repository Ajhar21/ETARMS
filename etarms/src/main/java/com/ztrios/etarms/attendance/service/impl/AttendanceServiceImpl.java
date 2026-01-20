package com.ztrios.etarms.attendance.service.impl;

import com.ztrios.etarms.attendance.dto.*;
import com.ztrios.etarms.attendance.entity.Attendance;
import com.ztrios.etarms.attendance.enums.AttendanceStatus;
import com.ztrios.etarms.attendance.repository.AttendanceRepository;
import com.ztrios.etarms.attendance.service.AttendanceService;
import com.ztrios.etarms.audit.model.AuditAction;
import com.ztrios.etarms.audit.service.AuditService;
import com.ztrios.etarms.employee.entity.Employee;
import com.ztrios.etarms.employee.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AuditService auditService;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository,
                                 EmployeeRepository employeeRepository,AuditService auditService) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.auditService = auditService;
    }

    @Override
    @Transactional
    public AttendanceCheckInResponse checkIn(AttendanceCheckInRequest request) {
        // Resolve Employee
        Employee employee = employeeRepository.findByEmployeeId(request.employeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        // Validate ACTIVE status
        if (!employee.getEmploymentStatus().name().equals("ACTIVE")) {
            throw new IllegalStateException("Only ACTIVE employees can check in");
        }

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // Check for existing attendance today
        attendanceRepository.findByEmployeeAndAttendanceDate(employee, today)
                .ifPresent(a -> { throw new IllegalStateException("Attendance already exists for today"); });

        // Create attendance
        Attendance attendance = new Attendance(employee, today, now, AttendanceStatus.INCOMPLETE);
        attendanceRepository.save(attendance);

        auditService.log(
                AuditAction.CHECK_IN,
                "Attendance",
                attendance.getId().toString(),
                "Employee checked in at " + now
        );


        return new AttendanceCheckInResponse(employee.getEmployeeId(), attendance.getCheckInTime());
    }

    @Override
    @Transactional
    public AttendanceCheckOutResponse checkOut(AttendanceCheckOutRequest request) {
        Employee employee = employeeRepository.findByEmployeeId(request.employeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        Attendance openAttendance = attendanceRepository
                .findByEmployeeAndCheckOutTimeIsNull(employee)
                .orElseThrow(() -> new IllegalStateException("No open attendance found for check-out"));

        // Perform check-out using entity method
        openAttendance.checkOut(LocalDateTime.now());

        attendanceRepository.save(openAttendance);

        auditService.log(
                AuditAction.CHECK_OUT,
                "Attendance",
                openAttendance.getId().toString(),
                "Employee "+employee.getEmployeeId()+" Employee checked out at " + openAttendance.getCheckOutTime()
        );

        return new AttendanceCheckOutResponse(
                employee.getEmployeeId(),
                openAttendance.getCheckInTime(),
                openAttendance.getCheckOutTime(),
                openAttendance.getWorkingMinutes()
        );
    }
//
//    @Override
//    public List<AttendanceHistoryResponse> getHistory(AttendanceHistoryRequest request) {
//        Employee employee = employeeRepository.findByEmployeeId(request.employeeId())
//                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
//
//        LocalDate startDate = request.startDate() != null ? request.startDate() : LocalDate.MIN;
//        LocalDate endDate = request.endDate() != null ? request.endDate() : LocalDate.MAX;
//
//        List<Attendance> attendanceList = attendanceRepository
//                .findByEmployeeAndAttendanceDateBetweenOrderByAttendanceDateAsc(employee, startDate, endDate);
//
//        return attendanceList.stream()
//                .map(a -> new AttendanceHistoryResponse(
//                        employee.getEmployeeId(),
//                        a.getAttendanceDate(),
//                        a.getCheckInTime(),
//                        a.getCheckOutTime(),
//                        a.getWorkingMinutes() != null ? a.getWorkingMinutes() : 0,
//                        a.getAttendanceStatus().name()
//                ))
//                .collect(Collectors.toList());
//    }

    @Override
    public Page<AttendanceHistoryResponse> getHistory(AttendanceHistoryRequest request, Pageable pageable) {

        // Fetch Employee by business ID
        Employee employee = employeeRepository.findByEmployeeId(request.employeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        // Handle null dates
        LocalDate startDate = request.startDate() != null ? request.startDate() : LocalDate.now().minusYears(1); // default 1 year
        LocalDate endDate = request.endDate() != null ? request.endDate() : LocalDate.now(); // default today

        // Validate range
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate cannot be after endDate");
        }

        // Fetch paginated attendance records
        Page<Attendance> attendancePage = attendanceRepository
                .findByEmployeeAndAttendanceDateBetweenOrderByAttendanceDateAsc(employee, startDate, endDate, pageable);

        // Map entities to DTOs
        return attendancePage.map(att -> new AttendanceHistoryResponse(
                employee.getEmployeeId(),
                att.getAttendanceDate(),
                att.getCheckInTime(),
                att.getCheckOutTime(),
                att.getWorkingMinutes(),
                att.getAttendanceStatus().name()
        ));
    }
}
