package com.casadedios.backend.modules.retreat.dto.request;

import com.casadedios.backend.common.dto.request.PaginationCriteriaDto;
import lombok.Builder;

import java.time.Instant;

@Builder
public record RetreatSearchCriteriaDto(
        String name,
        String location,
        Instant startDateFrom,
        Instant startDateTo,
        Integer page,
        Integer size,
        String sortField,
        String sortDirection
) {
    public PaginationCriteriaDto pagination() {
        return PaginationCriteriaDto.builder()
                .page(page)
                .size(size)
                .sortField(sortField)
                .sortDirection(sortDirection)
                .build();
    }
}
