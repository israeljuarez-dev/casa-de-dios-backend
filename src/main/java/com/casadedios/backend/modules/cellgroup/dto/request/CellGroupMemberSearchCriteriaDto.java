package com.casadedios.backend.modules.cellgroup.dto.request;

import com.casadedios.backend.common.enums.GenderEnum;
import com.casadedios.backend.modules.disciple.enums.SpiritualLevel;
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
