package com.casadedios.backend.cellgroup.dto.request;

import com.casadedios.backend.common.enums.GenderEnum;
import com.casadedios.backend.disciple.enums.SpiritualLevel;
import lombok.Builder;

@Builder
public record CellGroupMemberSearchCriteriaDto(
        String search,
        SpiritualLevel spiritualLevel,
        GenderEnum gender
) {
    public CellGroupMemberSearchCriteriaDto {
        if (search != null && search.isBlank()) {
            search = null;
        }
    }
}
