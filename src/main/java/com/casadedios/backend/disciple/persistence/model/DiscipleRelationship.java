package com.casadedios.backend.disciple.persistence.model;

import com.casadedios.backend.common.listener.EntityAuditListener;
import com.casadedios.backend.disciple.enums.RelationshipType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(EntityAuditListener.class)
@Table(name = "disciple_relationships")
public class DiscipleRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(
            targetEntity = Disciple.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "source_disciple_id", nullable = false)
    @ToString.Exclude
    private Disciple sourceDisciple;

    @ManyToOne(
            targetEntity = Disciple.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "target_disciple_id", nullable = false)
    @ToString.Exclude
    private Disciple targetDisciple;

    @Column(name = "relationship_type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationshipType relationshipType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}