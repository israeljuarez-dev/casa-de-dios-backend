package com.casadedios.backend.disciple.persistence.model;

import com.casadedios.backend.common.listener.EntityAuditListener;
import com.casadedios.backend.disciple.enums.MaritalStatus;
import com.casadedios.backend.disciple.enums.SpiritualLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(EntityAuditListener.class)
@Table(name = "disciples")
public class Disciple {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "first_name", length = 150, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 150, nullable = false)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(length = 150)
    private String occupation;

    @Column(name = "phone_number", unique = true, length = 20)
    private String phoneNumber;

    @Column
    private String address;

    @Column(length = 20, unique = true)
    private String dni;

    @Column(name = "marital_status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MaritalStatus maritalStatus = MaritalStatus.SINGLE;

    @Column(name = "couple_name", length = 150)
    private String coupleName;

    @Column(name = "spiritual_level", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SpiritualLevel spiritualLevel = SpiritualLevel.GUEST;

    @Column(name = "is_leader", nullable = false)
    @Builder.Default
    private boolean isLeader = false;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}

