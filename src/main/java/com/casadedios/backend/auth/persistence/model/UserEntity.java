package com.casadedios.backend.auth.persistence.model;

import com.casadedios.backend.auth.enums.RoleEnum;
import com.casadedios.backend.common.listener.EntityAuditListener;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(EntityAuditListener.class)
@Table(name="users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50, nullable = false)
    private String username;

    @Column(unique = true, length = 150, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    @Column(name = "account_non_expired", nullable = false)
    @Builder.Default
    private boolean accountNotExpired = true;

    @Column(name = "account_non_locked", nullable = false)
    @Builder.Default
    private boolean accountNotLocked = true;

    @Column(name = "credentials_non_expired", nullable = false)
    @Builder.Default
    private boolean credentialNotExpired = true;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
