package com.casadedios.backend.modules.inventory.persistence.repository;

import com.casadedios.backend.modules.inventory.persistence.model.InventoryItem;
import com.casadedios.backend.modules.inventory.persistence.projection.InventoryItemSummaryProjection;
import com.casadedios.backend.modules.inventory.persistence.queries.InventoryItemQueries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    @Query(
            value = InventoryItemQueries.FIND_ALL_WITH_DONOR,
            countQuery = InventoryItemQueries.COUNT_ALL_WITH_DONOR,
            nativeQuery = true
    )
    Page<InventoryItemSummaryProjection> findAllWithDonor(
            @Param("name") String name,
            @Param("category") String category,
            @Param("sourceType") String sourceType,
            @Param("donorName") String donorName,
            @Param("registeredFrom") LocalDate registeredFrom,
            @Param("registeredTo") LocalDate registeredTo,
            Pageable pageable
    );

    @Query(value = InventoryItemQueries.FIND_BY_ID_WITH_DONOR, nativeQuery = true)
    Optional<InventoryItemSummaryProjection> findByIdWithDonor(@Param("id") Long id);

    @Query(InventoryItemQueries.ADJUST_STOCK)
    @Modifying
    int adjustStock(@Param("id") Long id, @Param("delta") int delta, @Param("updatedAt") Instant updatedAt);
}
