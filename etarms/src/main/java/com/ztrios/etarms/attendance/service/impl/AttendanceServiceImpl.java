package com.ztrios.etarms.attendance.service.impl;

import com.ztrios.etarms.attendance.dto.*;
import com.ztrios.etarms.attendance.entity.Attendance;
import com.ztrios.etarms.attendance.mapper.AttendanceMapper;
import com.ztrios.etarms.attendance.repository.AttendanceRepository;
import com.ztrios.etarms.attendance.service.AttendanceService;
import com.ztrios.etarms.audit.service.AuditService;
import com.ztrios.etarms.common.exception.BusinessException;
import com.ztrios.etarms.common.exception.ResourceNotFoundException;
import com.ztrios.etarms.employee.entity.Employee;
import com.ztrios.etarms.employee.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AuditService auditService;
    private final AttendanceMapper attendanceMapper;

    @Override
    @Transactional
    public AttendanceCheckInResponse checkIn(AttendanceRequest request) {
        // Resolve Employee
        Employee employee = employeeRepository.findByEmployeeId(request.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        // Validate ACTIVE status
        if (!employee.getEmploymentStatus().name().equals("ACTIVE")) {
            throw new BusinessException("Only ACTIVE employees can check in");
        }

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        // Check for existing attendance today
        attendanceRepository.findByEmployeeAndAttendanceDate(employee, today)
                .ifPresent(a -> {
                    throw new BusinessException("Attendance already exists for today");
                });

        // Create attendance
        Attendance attendance = attendanceMapper.mapToEntity(request, employee);

        attendanceRepository.save(attendance);
        return attendanceMapper.toCheckInResponse(attendance);
    }

    @Override
    @Transactional
    public AttendanceCheckOutResponse checkOut(AttendanceRequest request) {
        Employee employee = employeeRepository.findByEmployeeId(request.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        Attendance openAttendance = attendanceRepository
//                .findByEmployeeAndCheckOutTimeIsNull(employee)
                .findTopByEmployeeAndCheckOutTimeIsNullOrderByCheckInTimeDesc(employee)     // solution for multiple open attendances, taking the latest one
                .orElseThrow(() -> new BusinessException("No open attendance found for check-out"));

        // Perform check-out using entity method
        openAttendance.checkOut(LocalDateTime.now());

        attendanceRepository.save(openAttendance);
        return attendanceMapper.toCheckOutResponse(openAttendance);
    }

    @Override
    public Page<AttendanceHistoryResponse> getHistory(AttendanceHistoryRequest request, Pageable pageable) {

        // Fetch Employee by business ID
        Employee employee = employeeRepository.findByEmployeeId(request.employeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        // Handle null dates
        LocalDate startDate = request.startDate() != null ? request.startDate() : LocalDate.now().minusYears(1); // default 1 year
        LocalDate endDate = request.endDate() != null ? request.endDate() : LocalDate.now(); // default today

        // Validate range
        if (startDate.isAfter(endDate)) {
            throw new BusinessException("startDate cannot be after endDate");
        }

        // Fetch paginated attendance records
        Page<Attendance> attendancePage = attendanceRepository
                .findByEmployeeAndAttendanceDateBetweenOrderByAttendanceDateAsc(employee, startDate, endDate, pageable);

        return attendancePage.map(attendanceMapper::toHistoryResponse);

    }
}
