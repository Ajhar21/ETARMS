package com.ztrios.etarms.reporting.repository;

import com.ztrios.etarms.attendance.entity.Attendance;
import com.ztrios.etarms.reporting.projection.MonthlyAttendanceSummaryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceReportRepository extends JpaRepository<Attendance, UUID> {

    @Query(value = """
            SELECT e.employee_id                           AS employeeId,
                   Concat(e.first_name, ' ', e.last_name)  AS employeeName,
                   Count(a.id)                             AS presentDays,
                   Round(Sum(a.working_minutes) / 60.0, 2) AS totalHours	-- convert minutes to hours
            FROM   attendance a
                   JOIN employees e
                     ON a.employee_ref_id = e.id
            WHERE  Extract(year FROM a.attendance_date) = :year
                   AND Extract(month FROM a.attendance_date) = :month
                   AND ( :departmentId IS NULL
                          OR e.department_ref_id = (SELECT id
                                                    FROM   departments
                                                    WHERE  department_id = :departmentId) )
            GROUP  BY e.id,
                      e.first_name,
                      e.last_name;
        """, nativeQuery = true)
    List<MonthlyAttendanceSummaryProjection> getMonthlyAttendanceSummary(
            @Param("year") int year,
            @Param("month") int month,
            @Param("departmentId") String departmentId
    );
}

