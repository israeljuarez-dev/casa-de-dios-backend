package com.casadedios.backend.cellgroup.persistence.model;

import com.casadedios.backend.cellgroup.enums.MeetingDay;
import com.casadedios.backend.common.enums.GenderEnum;
import com.casadedios.backend.common.listener.EntityAuditListener;
import com.casadedios.backend.disciple.persistence.model.Disciple;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@EntityListeners(EntityAuditListener.class)
@Table(name = "cell_groups")
public class CellGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_disciple_id")
    private Disciple leader;

    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_day", length = 15)
    private MeetingDay meetingDay;

    @Column(name = "meeting_time")
    private LocalTime meetingTime;

    @Column(name = "location")
    private String location;

    @Column(name = "is_pastor_cell", nullable = false)
    @Builder.Default
    private boolean isPastorCell = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "pastor_cell_gender", length = 10)
    private GenderEnum pastorCellGender;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
