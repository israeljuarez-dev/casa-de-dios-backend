package com.casadedios.backend.disciple.persistence.repository;

import com.casadedios.backend.disciple.enums.RelationshipType;
import com.casadedios.backend.disciple.persistence.model.DiscipleRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscipleRelationshipRepository extends JpaRepository<DiscipleRelationship, Long> {
    List<DiscipleRelationship> findBySourceDisciple_IdAndRelationshipType(Long sourceDiscipleId, RelationshipType relationshipType);

    Optional<DiscipleRelationship> findByTargetDisciple_IdAndRelationshipType(Long targetDiscipleId, RelationshipType relationshipType);

    void deleteBySourceDisciple_IdAndRelationshipType(Long sourceDiscipleId, RelationshipType relationshipType);

    void deleteByTargetDisciple_IdAndRelationshipType(Long targetDiscipleId, RelationshipType relationshipType);
}
