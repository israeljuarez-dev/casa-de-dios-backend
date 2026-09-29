package com.casadedios.backend.modules.retreat.persistence.repository;

import com.casadedios.backend.modules.retreat.persistence.model.RetreatEnrollment;
import com.casadedios.backend.modules.retreat.persistence.projection.RetreatEnrollmentProjection;
import com.casadedios.backend.modules.retreat.persistence.queries.RetreatQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RetreatEnrollmentRepository extends JpaRepository<RetreatEnrollment, Long> {

    boolean existsByRetreat_IdAndDisciple_Id(Long retreatId, Long discipleId);

    Optional<RetreatEnrollment> findByRetreat_IdAndDisciple_Id(Long retreatId, Long discipleId);

    long countByRetreat_Id(Long retreatId);

    void deleteByRetreat_IdAndDisciple_IdIn(Long retreatId, List<Long> discipleIds);

    @Query(value = RetreatQueries.FIND_ENROLLED_DISCIPLES, nativeQuery = true)
    List<RetreatEnrollmentProjection> findEnrolledDisciples(
            @Param("retreatId") Long retreatId,
            @Param("search") String search,
            @Param("paymentStatus") String paymentStatus
    );
}