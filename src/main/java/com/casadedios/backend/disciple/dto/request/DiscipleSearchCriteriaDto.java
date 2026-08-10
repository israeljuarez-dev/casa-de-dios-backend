package com.casadedios.backend.disciple.dto.request;

import com.casadedios.backend.common.dto.request.PaginationCriteriaDto;
import com.casadedios.backend.common.enums.GenderEnum;
import com.casadedios.backend.disciple.enums.MaritalStatus;
import com.casadedios.backend.disciple.enums.SpiritualLevel;
import lombok.Builder;

@Builder
public record DiscipleSearchCriteriaDto(
        String search,

        String firstName,

        String lastName,

        GenderEnum gender,

        SpiritualLevel spiritualLevel,

        MaritalStatus maritalStatus,

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

        if (search != null && search.isBlank()) {
            search = null;
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
