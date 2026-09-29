package com.casadedios.backend.modules.retreat.persistence.repository;

import com.casadedios.backend.modules.retreat.persistence.model.Retreat;
import com.casadedios.backend.modules.retreat.persistence.projection.RetreatSummaryProjection;
import com.casadedios.backend.modules.retreat.persistence.queries.RetreatQueries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RetreatRepository extends JpaRepository<Retreat, Long> {

    @Query(
            value = RetreatQueries.FIND_ALL_WITH_COUNTS,
            countQuery = RetreatQueries.COUNT_ALL,
            nativeQuery = true
    )
    Page<RetreatSummaryProjection> findAllWithCounts(
            @Param("name") String name,
            @Param("location") String location,
            @Param("startDateFrom") Instant startDateFrom,
            @Param("startDateTo") Instant startDateTo,
            Pageable pageable
    );

    @Query(value = RetreatQueries.FIND_BY_ID_WITH_COUNTS, nativeQuery = true)
    Optional<RetreatSummaryProjection> findByIdWithCounts(@Param("id") Long id);
}
