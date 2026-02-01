package com.ztrios.etarms.reporting.service;

import com.ztrios.etarms.audit.model.AuditAction;
import com.ztrios.etarms.audit.service.AuditService;
import com.ztrios.etarms.common.exception.ResourceNotFoundException;
import com.ztrios.etarms.employee.repository.DepartmentRepository;
import com.ztrios.etarms.reporting.projection.MonthlyAttendanceSummaryProjection;
import com.ztrios.etarms.reporting.repository.AttendanceReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReportService {

    private final AttendanceReportRepository attendanceReportRepository;
    private final DepartmentRepository departmentRepository;
    private final AuditService auditService;

    /*======================== Report generation =======================*/
    public List<MonthlyAttendanceSummaryProjection> getMonthlyReport(
            int year, int month, String departmentId
    ) {
        if (departmentId != null && !departmentId.isBlank()) {
            departmentRepository.findByDepartmentId(departmentId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Department not found with id: " + departmentId
                            )
                    );
        }
        return attendanceReportRepository.getMonthlyAttendanceSummary(year, month, departmentId);
    }

    /*======================== Export Report with generation =======================*/
    public void exportMonthlyAttendanceCsv(
            int year,
            int month,
            String departmentId,
            Writer writer
    ) throws IOException {

        // Validate department
        if (departmentId != null && !departmentId.isBlank()) {
            departmentRepository.findByDepartmentId(departmentId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Department not found with id: " + departmentId
                            )
                    );
        }

        List<MonthlyAttendanceSummaryProjection> data =
                attendanceReportRepository.getMonthlyAttendanceSummary(
                        year, month, departmentId
                );

        // CSV Header
        writer.write("Employee ID,Employee Name,Present Days,Total Hours\n");

        // CSV Rows
        for (MonthlyAttendanceSummaryProjection row : data) {
            writer.write(String.format(
                    "%s,%s,%d,%.2f\n",
                    row.getEmployeeId(),
//                    escapeCsv(row.getEmployeeName()),
                    row.getEmployeeName(),
                    row.getPresentDays(),
                    row.getTotalHours()
            ));
        }

        writer.flush();
    }

}

