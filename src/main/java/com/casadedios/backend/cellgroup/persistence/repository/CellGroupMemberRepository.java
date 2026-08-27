package com.casadedios.backend.cellgroup.persistence.repository;

import com.casadedios.backend.cellgroup.persistence.model.CellGroupMember;
import com.casadedios.backend.cellgroup.persistence.projection.CellGroupMemberProjection;
import com.casadedios.backend.cellgroup.persistence.queries.CellGroupQueries;
import com.casadedios.backend.common.enums.GenderEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CellGroupMemberRepository extends JpaRepository<CellGroupMember, Long> {

    boolean existsByDisciple_Id(Long discipleId);

    long countByCellGroup_Id(Long cellGroupId);

    boolean existsByCellGroup_IdAndDisciple_Id(Long cellGroupId, Long discipleId);

    Optional<CellGroupMember> findByCellGroup_IdAndDisciple_Id(Long cellGroupId, Long discipleId);

    long countByCellGroup_IdAndIsPastorCoreTwelveTrue(Long cellGroupId);

    long countByIsPastorCoreTwelveTrue();

    long countByCellGroup_IdAndDisciple_Gender(Long cellGroupId, GenderEnum gender);

    @Query(value = CellGroupQueries.FIND_MEMBERS_BY_CELL_GROUP_ID, nativeQuery = true)
    List<CellGroupMemberProjection> findMembersByCellGroupId(
            @Param("cellGroupId") Long cellGroupId,
            @Param("search") String search,
            @Param("spiritualLevel") String spiritualLevel,
            @Param("gender") String gender
    );
    @Query(value = CellGroupQueries.FIND_MEMBERS_BY_CELL_GROUP_ID, nativeQuery = true)
    List<CellGroupMemberProjection> findMembersByCellGroupId(@Param("cellGroupId") Long cellGroupId);

    @Query(value = CellGroupQueries.FIND_PASTOR_CORE_TWELVE, nativeQuery = true)
    List<CellGroupMemberProjection> findPastorCoreTwelve();

    @Query(CellGroupQueries.FIND_DISCIPLE_IDS_BY_DISCIPLE_ID_IN)
    List<Long> findDiscipleIdsByDiscipleIdIn(@Param("ids") List<Long> ids);
}