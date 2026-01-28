package com.ztrios.etarms.tasks.entity;

import com.ztrios.etarms.employee.entity.Employee;
import com.ztrios.etarms.identity.entity.User;
import com.ztrios.etarms.tasks.enums.TaskPriority;
import com.ztrios.etarms.tasks.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "tasks",
        uniqueConstraints = @UniqueConstraint(columnNames = "task_id"),
        indexes = {
                @Index(name = "idx_tasks_task_id", columnList = "task_id"),
                @Index(name = "idx_tasks_assigned_to", columnList = "assigned_to"),
                @Index(name = "idx_tasks_status", columnList = "status"),
                @Index(name = "idx_tasks_deadline", columnList = "deadline"),
                @Index(
                        name = "idx_tasks_assignee_status_deadline",
                        columnList = "assigned_to, status, deadline"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Task {

    /* ===============================
       Primary & Business Identifiers
       =============================== */

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id = UUID.randomUUID();

    @Column(name = "task_id", length = 20, updatable = false, nullable = false, unique = true)
    private String taskId;

    /* ===============================
       Core Task Fields
       =============================== */

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", length = 30, nullable = false)
    private TaskPriority priority;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;

    /* ===============================
       Ownership & Responsibility
       =============================== */

    //    @Column(name = "assigned_to", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "assigned_to",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_tasks_assigned_to")
    )
    private Employee assignee;

    //    @Column(name = "manager_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "manager_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_tasks_manager")
    )
    private User taskManager;

    /* ===============================
       Optimistic Locking
       =============================== */

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    /* ===============================
       Audit Fields
       =============================== */

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "created_by", length = 50, nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    /**
     * Domain constructor – enforces valid creation
     */

    public Task(String taskId, String title, String description, TaskStatus status, TaskPriority priority, LocalDateTime deadline,
                Employee assignee, User taskManager) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.deadline = deadline;
        this.assignee = assignee;
        this.taskManager = taskManager;
    }

    public void setNewAssignee(Employee newAssignee) {
        this.assignee = newAssignee;
    }

    public void updateStatus(TaskStatus status) {
        this.status = status;
    }

}
