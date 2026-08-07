package com.casadedios.backend.auth.dto.response;

import com.casadedios.backend.common.enums.GenderEnum;
import com.casadedios.backend.auth.enums.RoleEnum;
import lombok.Builder;

@Builder
public record AuthUserEntityProfileResponseDto(
        Long id,

        String firstName,

        String lastName,

        GenderEnum gender,

        String username,

        String email,

        RoleEnum role
) {
}
