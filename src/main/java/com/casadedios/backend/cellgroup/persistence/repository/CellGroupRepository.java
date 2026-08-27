package com.casadedios.backend.cellgroup.persistence.repository;

import com.casadedios.backend.cellgroup.persistence.model.CellGroup;
import com.casadedios.backend.cellgroup.persistence.projection.CellGroupSummaryProjection;
import com.casadedios.backend.cellgroup.persistence.queries.CellGroupQueries;
import com.casadedios.backend.common.enums.GenderEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CellGroupRepository extends JpaRepository<CellGroup, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    long countByIsPastorCellTrue();

    boolean existsByIsPastorCellTrueAndPastorCellGender(GenderEnum pastorCellGender);

    @Query(
            value = CellGroupQueries.FIND_ALL_WITH_LEADER_AND_COUNT,
            countQuery = CellGroupQueries.COUNT_ALL_WITH_LEADER,
            nativeQuery = true
    )
    Page<CellGroupSummaryProjection> findAllWithLeaderAndCount(
            @Param("name") String name,
            @Param("leaderName") String leaderName,
            @Param("meetingDay") String meetingDay,
            @Param("isPastorCell") Boolean isPastorCell,
            Pageable pageable
    );
}
