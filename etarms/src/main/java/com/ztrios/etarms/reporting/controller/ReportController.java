package com.ztrios.etarms.reporting.controller;

import com.ztrios.etarms.reporting.projection.MonthlyAttendanceSummaryProjection;
import com.ztrios.etarms.reporting.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/attendance/monthly")
    public List<MonthlyAttendanceSummaryProjection> getMonthlyAttendance(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) String departmentId
    ) {
        return reportService.getMonthlyReport(year, month, departmentId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping(value = "/attendance/monthly/export", produces = "text/csv")
    public void exportMonthlyAttendanceCsv(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) String departmentId,
            HttpServletResponse response
    ) throws IOException {

        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=monthly-attendance-" + year + "-" + month + ".csv"    //attachment used for download report automatically, replace attachment by inline can stop auto download
        );

        reportService.exportMonthlyAttendanceCsv(
                year, month, departmentId, response.getWriter()
        );
    }
}

