package com.ztrios.etarms.identity.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Setter
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    // Many Users -> One Role
    @ManyToOne(fetch = FetchType.EAGER) // acceptable for small, static roles
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Domain constructor — enforces valid state at creation
     */
    public User(String username, String password, Role role) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = true;
    }

    /**
     * Intent-revealing domain methods (controlled mutation)
     */
    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void changeRole(Role newRole) {
        this.role = newRole;
    }

    public void disable() {
        this.enabled = false;
    }

    public void enable() {
        this.enabled = true;
    }

    @PrePersist
    protected void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
