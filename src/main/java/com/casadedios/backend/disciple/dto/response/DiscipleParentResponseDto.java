package com.casadedios.backend.disciple.dto.response;

import com.casadedios.backend.common.enums.GenderEnum;

public record DiscipleParentResponseDto(
        Long id,
        String firstName,
        String lastName,
        GenderEnum gender
) {
}
