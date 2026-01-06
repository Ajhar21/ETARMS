package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Attendance {

    private String id;

    private String employeeId;
    private LocalDate attendanceDate;
    private boolean present;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    /* ------------ Constructors ------------ */

    public Attendance() {
    }

    /**
     * Constructor for marking attendance (before insert)
     */
    public Attendance(String employeeId, LocalDate attendanceDate, boolean present) {
        this.employeeId = employeeId;
        this.attendanceDate = attendanceDate;
        this.present = present;
    }

    /**
     * Constructor for reading attendance from DB
     */
    public Attendance(String id,
                      String employeeId,
                      LocalDate attendanceDate,
                      boolean present,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt,
                      String createdBy,
                      String updatedBy) {

        this.id = id;
        this.employeeId = employeeId;
        this.attendanceDate = attendanceDate;
        this.present = present;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    /* ------------ Getters & Setters ------------ */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }
    
    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public boolean isPresent() {
        return present;
    }
    
    public void setPresent(boolean present) {
        this.present = present;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
