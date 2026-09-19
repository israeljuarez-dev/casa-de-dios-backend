package com.casadedios.backend.modules.auth.dto.response;

import com.casadedios.backend.modules.auth.enums.RoleEnum;
import lombok.Builder;

@Builder
public record AuthUserRegisterResponseDto(
        Long id,

        String username,

        String email,

        RoleEnum role
) {}
