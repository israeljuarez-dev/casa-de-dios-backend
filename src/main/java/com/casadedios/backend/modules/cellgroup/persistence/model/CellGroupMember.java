package com.casadedios.backend.modules.cellgroup.persistence.model;

import com.casadedios.backend.modules.disciple.persistence.model.Disciple;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Table(name = "cell_group_members")
public class CellGroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cell_group_id", nullable = false)
    private CellGroup cellGroup;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "disciple_id", nullable = false)
    private Disciple disciple;

    @Column(name = "is_core_twelve", nullable = false)
    @Builder.Default
    private boolean isCoreTwelve = false;

    @Column(name = "is_pastor_core_twelve", nullable = false)
    @Builder.Default
    private boolean isPastorCoreTwelve = false;

    @Column(name = "joined_at", nullable = false)
    private LocalDate joinedAt;
}
