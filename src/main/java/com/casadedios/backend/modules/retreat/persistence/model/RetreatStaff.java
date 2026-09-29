package com.casadedios.backend.modules.retreat.persistence.model;

import com.casadedios.backend.common.listener.EntityAuditListener;
import com.casadedios.backend.modules.disciple.persistence.model.Disciple;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@EntityListeners(EntityAuditListener.class)
@Table(
        name = "retreat_staff",
        uniqueConstraints = @UniqueConstraint(columnNames = {"retreat_id", "disciple_id"})
)
public class RetreatStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "retreat_id", nullable = false)
    private Retreat retreat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "disciple_id", nullable = false)
    private Disciple disciple;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
