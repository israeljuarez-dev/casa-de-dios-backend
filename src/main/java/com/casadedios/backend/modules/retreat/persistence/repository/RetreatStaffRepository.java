package com.casadedios.backend.modules.retreat.persistence.repository;

import com.casadedios.backend.modules.retreat.persistence.model.RetreatStaff;
import com.casadedios.backend.modules.retreat.persistence.projection.RetreatStaffProjection;
import com.casadedios.backend.modules.retreat.persistence.queries.RetreatQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetreatStaffRepository extends JpaRepository<RetreatStaff, Long> {

    boolean existsByRetreat_IdAndDisciple_Id(Long retreatId, Long discipleId);

    long countByRetreat_Id(Long retreatId);

    void deleteByRetreat_IdAndDisciple_IdIn(Long retreatId, List<Long> discipleIds);

    @Query(value = RetreatQueries.FIND_STAFF_BY_RETREAT_ID, nativeQuery = true)
    List<RetreatStaffProjection> findStaffByRetreatId(@Param("retreatId") Long retreatId);
}