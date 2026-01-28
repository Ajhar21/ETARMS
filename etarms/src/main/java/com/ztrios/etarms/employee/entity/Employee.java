package com.ztrios.etarms.employee.entity;

import com.ztrios.etarms.employee.enums.EmploymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Employee {

    /**
     * Internal primary key (NOT exposed to client)
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = UUID.randomUUID();

    /**
     * Business identifier (safe to expose)
     */
    @Column(name = "employee_id", nullable = false, unique = true, updatable = false)
    private String employeeId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @Column(name = "job_title", nullable = false, length = 100)
    private String jobTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status", nullable = false, length = 20)
    private EmploymentStatus employmentStatus;

    /**
     * Many Employees -> One Department
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_ref_id", nullable = false)
    //will create department_ref_id, store id of departments table
    private Department department;

    /**
     * add field for storing image url
     *
     */
    @Column(name = "photo_url", length = 500)
    private String photoUrl;

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
    public Employee(
            String employeeId,
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String jobTitle,
            EmploymentStatus employmentStatus,
            Department department
    ) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.jobTitle = jobTitle;
        this.employmentStatus = employmentStatus;
        this.department = department;
    }

    /**
     * Intent-revealing domain methods (controlled updates)
     */
    public void changeDepartment(Department newDepartment) {
        this.department = newDepartment;
    }

    public void changeEmploymentStatus(EmploymentStatus status) {
        this.employmentStatus = status;
    }

    public void updateContactInfo(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Updates mutable fields of the employee.
     * Only allows controlled changes.
     */
    public void update(
            String firstName,
            String lastName,
            String email,
            String phoneNumber,
            String jobTitle,
            Department department,
            EmploymentStatus employmentStatus
    ) {
        if (firstName != null && !firstName.isBlank()) {
            this.firstName = firstName;
        }
        if (lastName != null && !lastName.isBlank()) {
            this.lastName = lastName;
        }
        if (email != null && !email.isBlank()) {
            this.email = email;
        }
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            this.phoneNumber = phoneNumber;
        }
        if (jobTitle != null && !jobTitle.isBlank()) {
            this.jobTitle = jobTitle;
        }
        if (department != null) {
            this.department = department;
        }
        if (employmentStatus != null) {
            this.employmentStatus = employmentStatus;
        }
    }

}
