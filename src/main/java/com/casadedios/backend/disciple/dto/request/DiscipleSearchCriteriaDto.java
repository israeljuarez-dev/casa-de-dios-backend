package com.casadedios.backend.disciple.dto.request;

import com.casadedios.backend.common.dto.request.PaginationCriteriaDto;
import com.casadedios.backend.disciple.enums.MaritalStatus;
import com.casadedios.backend.disciple.enums.SpiritualLevel;
import lombok.Builder;

@Builder
public record DiscipleSearchCriteriaDto(
        String firstName,

        String lastName,

        SpiritualLevel spiritualLevel,

        MaritalStatus maritalStatus,

        Boolean isLeader,

        Integer page,
        Integer size,
        String sortField,
        String sortDirection
) {
    public DiscipleSearchCriteriaDto {
        if (page == null || page < 0) {
            page = 0;
        }

        if (size == null || size < 1) {
            size = 10;
        }
    }

    public PaginationCriteriaDto pagination() {
        return PaginationCriteriaDto.builder()
                .page(page)
                .size(size)
                .sortField(sortField)
                .sortDirection(sortDirection)
                .build();
    }
}
