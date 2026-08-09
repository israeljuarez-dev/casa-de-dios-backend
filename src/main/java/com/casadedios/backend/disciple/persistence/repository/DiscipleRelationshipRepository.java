package com.casadedios.backend.disciple.persistence.repository;

import com.casadedios.backend.disciple.enums.RelationshipType;
import com.casadedios.backend.disciple.persistence.model.DiscipleRelationship;
import com.casadedios.backend.disciple.persistence.projection.ChildProjection;
import com.casadedios.backend.disciple.persistence.projection.ChildRelationshipProjection;
import com.casadedios.backend.disciple.persistence.projection.InviterProjection;
import com.casadedios.backend.disciple.persistence.projection.ParentProjection;
import com.casadedios.backend.disciple.persistence.queries.DiscipleRelationshipQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscipleRelationshipRepository extends JpaRepository<DiscipleRelationship, Long> {

    void deleteByTargetDisciple_IdAndRelationshipType(Long targetDiscipleId, RelationshipType relationshipType);

    @Query(value = DiscipleRelationshipQueries.FIND_CHILDREN_BY_SOURCE_IDS, nativeQuery = true)
    List<ChildProjection> findChildrenBySourceIds(@Param("sourceIds") List<Long> sourceIds, @Param("type") String type);

    @Query(value = DiscipleRelationshipQueries.FIND_INVITERS_BY_TARGET_IDS, nativeQuery = true)
    List<InviterProjection> findInvitersByTargetIds(@Param("targetIds") List<Long> targetIds, @Param("type") String type );

    @Query(value = DiscipleRelationshipQueries.FIND_PARENTS_BY_CHILD_IDS, nativeQuery = true)
    List<ParentProjection> findParentsByChildIds(@Param("childIds") List<Long> childIds, @Param("type") String type);

    @Query(value = DiscipleRelationshipQueries.FIND_CHILD_IDS_BY_PARENT_ID, nativeQuery = true)
    List<ChildRelationshipProjection> findChildRelationshipsByParentId(@Param("parentId") Long parentId, @Param("type") String type);
}
