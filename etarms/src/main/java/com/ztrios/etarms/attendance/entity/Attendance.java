package com.ztrios.etarms.attendance.entity;

import com.ztrios.etarms.common.exception.BusinessException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.ztrios.etarms.attendance.enums.AttendanceStatus;
import com.ztrios.etarms.employee.entity.Employee;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "attendance",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_employee_attendance_day",
                columnNames = {"employee_ref_id", "attendance_date"}
        )
)
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Attendance {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = UUID.randomUUID();

    /**
     * Many Attendance records → One Employee
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "employee_ref_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_attendance_employee")
    )
    private Employee employee;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Column(name = "working_minutes")
    private Integer workingMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", nullable = false, length = 20)
    private AttendanceStatus attendanceStatus;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * Domain constructor – enforces valid creation
     */
    public Attendance(Employee employee, LocalDate attendanceDate, LocalDateTime checkInTime, AttendanceStatus status) {
        this.employee = employee;
        this.attendanceDate = attendanceDate;
        this.checkInTime = checkInTime;
        this.attendanceStatus = status;
    }

    /**
     * Domain method – perform check-out
     */
    public void checkOut(LocalDateTime checkOutTime) {
        if (this.checkOutTime != null) {
            throw new BusinessException("Attendance already checked out");
        }
        if (checkOutTime.isBefore(this.checkInTime)) {
            throw new BusinessException("Check-out time cannot be before check-in");
        }
        this.checkOutTime = checkOutTime;
        this.workingMinutes = (int) java.time.Duration.between(this.checkInTime, this.checkOutTime).toMinutes();
        this.attendanceStatus = AttendanceStatus.PRESENT;
    }

    /**
     * Optional: mark as absent (future use)
     */
    public void markAbsent() {
        this.attendanceStatus = AttendanceStatus.ABSENT;
        this.checkInTime = null;
        this.checkOutTime = null;
        this.workingMinutes = 0;
    }
}
