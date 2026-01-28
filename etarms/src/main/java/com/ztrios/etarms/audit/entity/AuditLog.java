package com.ztrios.etarms.audit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "actor", updatable = false)
    private String actor;        // username

    @Column(name = "action", updatable = false)
    private String action;       // CREATE_TASK, UPDATE_STATUS, CHECK_IN

    @Column(name = "entity_type", updatable = false)
    private String entityType;   // Task, Attendance, Employee

    @Column(name = "entity_id", updatable = false)
    private String entityId;     // UUID

    @Column(name = "timestamp", updatable = false, nullable = false)
    private Instant timestamp;

    @Column(length = 1000)
    private String description;  // human-readable context
}

