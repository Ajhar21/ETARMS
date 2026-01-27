package com.ztrios.etarms.attendance.mapper;

import com.ztrios.etarms.attendance.dto.AttendanceCheckInResponse;
import com.ztrios.etarms.attendance.dto.AttendanceCheckOutResponse;
import com.ztrios.etarms.attendance.dto.AttendanceHistoryResponse;
import com.ztrios.etarms.attendance.dto.AttendanceRequest;
import com.ztrios.etarms.attendance.entity.Attendance;
import com.ztrios.etarms.attendance.enums.AttendanceStatus;
import com.ztrios.etarms.employee.entity.Employee;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class AttendanceMapper {

    /**
     * Request -> Entity (Check-in)
     */
    public Attendance mapToEntity(AttendanceRequest request, Employee employee) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        return new Attendance(
                employee,
                today,
                now,
                AttendanceStatus.INCOMPLETE
        );
    }

    /**
     * Entity -> Check-in Response
     */
    public AttendanceCheckInResponse toCheckInResponse(Attendance attendance) {

        return AttendanceCheckInResponse.builder()
                .employeeId(attendance.getEmployee().getEmployeeId())
                .checkInTime(attendance.getCheckInTime())
                .build();
    }

    /**
     * Entity -> Check-out Response
     */
    public AttendanceCheckOutResponse toCheckOutResponse(Attendance openAttendance) {

        return AttendanceCheckOutResponse.builder()
                .employeeId(openAttendance.getEmployee().getEmployeeId())
                .checkInTime(openAttendance.getCheckInTime())
                .checkOutTime(openAttendance.getCheckOutTime())
                .workingMinutes(openAttendance.getWorkingMinutes())
                .build();
    }

    /**
     * Entity -> Attendance History Response
     */
    public AttendanceHistoryResponse toHistoryResponse(Attendance attendance) {


        return new AttendanceHistoryResponse(
                attendance.getEmployee().getEmployeeId(),
                attendance.getAttendanceDate(),
                attendance.getCheckInTime(),
                attendance.getCheckOutTime(),
                attendance.getWorkingMinutes(),
                attendance.getAttendanceStatus().name()
        );
    }

}
