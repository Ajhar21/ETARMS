package com.ztrios.etarms.attendance.repository;

import com.ztrios.etarms.attendance.entity.Attendance;
import com.ztrios.etarms.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    /**
     * Find attendance by employee for a specific date
     * Used for checking if attendance already exists
     */
    Optional<Attendance> findByEmployeeAndAttendanceDate(Employee employee, LocalDate attendanceDate);

    /**
     * Find open attendance (checked in but not yet checked out) for a given employee
     * Ensures employee cannot check in twice
     */
//    Optional<Attendance> findByEmployeeAndCheckOutTimeIsNull(Employee employee);
    Optional<Attendance> findTopByEmployeeAndCheckOutTimeIsNullOrderByCheckInTimeDesc(Employee employee);   //solution for multiple open attendance

    /**
     * Fetch attendance history for an employee within a date range
     * Can be used for reports
     */

    Page<Attendance> findByEmployeeAndAttendanceDateBetweenOrderByAttendanceDateAsc(
            Employee employee, LocalDate startDate, LocalDate endDate, Pageable pageable);

}
