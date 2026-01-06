package model;

import enums.Role;

import java.time.LocalDateTime;

public class Employee {

    private String id;
    private String name;
    private String email;
    private Role role;
    private String department;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    /* ------------ Constructors ------------ */

    public Employee() {
    }

    public Employee(String name, String email, Role role, String department) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.department = department;
    }

    public Employee(String id, String name, String email, Role role, String department,
                    LocalDateTime createdAt, LocalDateTime updatedAt,
                    String createdBy, String updatedBy) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.department = department;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
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
