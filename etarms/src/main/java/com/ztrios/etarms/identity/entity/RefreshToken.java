package com.ztrios.etarms.identity.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "refresh_tokens",
        indexes = {
                @Index(name = "idx_refresh_tokens_user_id", columnList = "user_id"),
                @Index(name = "idx_refresh_tokens_expires_at", columnList = "expires_at"),
                @Index(name = "idx_refresh_tokens_revoked", columnList = "revoked")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    /**
     * Many RefreshTokens -> One user
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    //insertable=false, updatable=false → Hibernate won’t try to write user_id twice
    //adding for performance optimization
    private UUID userId;

    @Column(name = "token", nullable = false, unique = true, length = 255)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "revoked_reason", length = 50)
    private String revokedReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by_ip", length = 45)
    private String createdByIp;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    /* =====================
       Lifecycle Callbacks
       ===================== */

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}

