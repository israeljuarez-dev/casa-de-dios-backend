package com.casadedios.backend.modules.inventory.persistence.model;

import com.casadedios.backend.common.listener.EntityAuditListener;
import com.casadedios.backend.modules.disciple.persistence.model.Disciple;
import com.casadedios.backend.modules.inventory.enums.InventorySourceEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@EntityListeners(EntityAuditListener.class)
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "category", length = 100, nullable = false)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", length = 10, nullable = false)
    private InventorySourceEnum sourceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_disciple_id")
    private Disciple donor;

    @Column(name = "registered_at", nullable = false)
    private LocalDate registeredAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
