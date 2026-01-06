package model;

import java.time.LocalDateTime;

import enums.TaskPriority;
import enums.TaskStatus;

public class Task {

    private String id;

    private String title;
    private String description;

    private TaskStatus status;
    private TaskPriority priority;

    private String assignedToId;
    private String assignedById;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    private LocalDateTime completedAt;

    /* ------------ Constructors ------------ */

    public Task() {
    }

    /**
     * Constructor for creating a new task (before insert)
     */
    public Task(String title,
                String description,
                TaskStatus status,
                TaskPriority priority,
                String assignedToId,
                String assignedById) {

        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignedToId = assignedToId;
        this.assignedById = assignedById;
    }

    /**
     * Constructor for reading task from DB
     */
    public Task(String id,
                String title,
                String description,
                TaskStatus status,
                TaskPriority priority,
                String assignedToId,
                String assignedById,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,
                String createdBy,
                String updatedBy,
                LocalDateTime completedAt) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assignedToId = assignedToId;
        this.assignedById = assignedById;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.completedAt = completedAt;
    }

    /* ------------ Getters & Setters ------------ */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }
    
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }
    
    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public String getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(String assignedToId) {
        this.assignedToId = assignedToId;
    }

    public String getAssignedById() {
        return assignedById;
    }

    public void setAssignedById(String assignedById) {
        this.assignedById = assignedById;
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

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
