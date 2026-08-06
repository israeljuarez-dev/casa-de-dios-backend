package com.casadedios.backend.disciple.persistence.repository;

import com.casadedios.backend.disciple.enums.RelationshipType;
import com.casadedios.backend.disciple.persistence.model.DiscipleRelationship;
import com.casadedios.backend.disciple.persistence.queries.DiscipleRelationshipQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscipleRelationshipRepository extends JpaRepository<DiscipleRelationship, Long> {
    List<DiscipleRelationship> findBySourceDisciple_IdAndRelationshipType(Long sourceDiscipleId, RelationshipType relationshipType);

    Optional<DiscipleRelationship> findByTargetDisciple_IdAndRelationshipType(Long targetDiscipleId, RelationshipType relationshipType);

    void deleteBySourceDisciple_IdAndRelationshipType(Long sourceDiscipleId, RelationshipType relationshipType);

    void deleteByTargetDisciple_IdAndRelationshipType(Long targetDiscipleId, RelationshipType relationshipType);

    // Trae todas las relaciones de tipo X donde el source está en la lista de IDs
    List<DiscipleRelationship> findBySourceDisciple_IdInAndRelationshipType(List<Long> sourceIds, RelationshipType relationshipType);

    // Trae todas las relaciones de tipo X donde el target está en la lista de IDs
    List<DiscipleRelationship> findByTargetDisciple_IdInAndRelationshipType(List<Long> targetIds, RelationshipType relationshipType);

    @Query(DiscipleRelationshipQueries.FIND_CHILDREN_BY_SOURCE_IDS_AND_TYPE)
    List<DiscipleRelationship> findChildrenBySourceIdsAndType(@Param("sourceIds") List<Long> sourceIds, @Param("type") RelationshipType type);

    @Query(DiscipleRelationshipQueries.FIND_INVITERS_BY_TARGET_IDS_AND_TYPE)
    List<DiscipleRelationship> findInvitersByTargetIdsAndType(@Param("targetIds") List<Long> targetIds, @Param("type") RelationshipType type);
}
