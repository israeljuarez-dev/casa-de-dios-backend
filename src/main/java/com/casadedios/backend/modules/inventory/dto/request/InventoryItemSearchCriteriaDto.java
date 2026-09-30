package com.casadedios.backend.modules.inventory.dto.request;

import com.casadedios.backend.common.dto.request.PaginationCriteriaDto;
import com.casadedios.backend.modules.inventory.enums.InventorySourceEnum;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record InventoryItemSearchCriteriaDto(
        String name,
        String category,
        InventorySourceEnum sourceType,
        String donorName,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate registeredFrom,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate registeredTo,
        Integer page,
        Integer size,
        String sortField,
        String sortDirection
) {
    private static final String DEFAULT_SORT_FIELD = "name";

    public PaginationCriteriaDto pagination() {
        return PaginationCriteriaDto.builder()
                .page(page)
                .size(size)
                .sortField(sortField == null || sortField.isBlank() ? DEFAULT_SORT_FIELD : sortField)
                .sortDirection(sortDirection)
                .build();
    }
}
