package com.ztrios.etarms.employee.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

/****************************************
 @Data and @Getter together → redundant

 @Builder on a JPA entity → risky

 Lombok all-args constructor (@AllArgsConstructor) is risky for entities

 id field (PK) vs departmentId field (business ID)
 *********************************************/

@Entity
@Table(name = "departments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Department {

    /**
     * Internal primary key (not exposed to clients)
     */
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id = UUID.randomUUID();

    /**
     * Business ID (exposed to clients)
     */
    @Column(name = "department_id", nullable = false, updatable = false, length = 10, unique = true)
    private String departmentId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

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
     * Constructor to enforce valid creation
     */
    public Department(String departmentId, String name, String description) {
        this.departmentId = departmentId;
        this.name = name;
        this.description = description;
    }

    /**
     * Domain method to update department info
     */
    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
