package com.ztrios.etarms.identity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_id", nullable = false,updatable = false)
    private UUID id=UUID.randomUUID();

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    // Many Users -> One Role
    @ManyToOne(fetch = FetchType.EAGER) // EAGER is okay here since roles are small
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private java.time.Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private java.time.Instant updatedAt;

    @PrePersist
    public void prePersist() {
        java.time.Instant now = java.time.Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = java.time.Instant.now();
    }
}
