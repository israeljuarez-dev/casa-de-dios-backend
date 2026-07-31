package com.casadedios.backend.auth.dto.response;

import com.casadedios.backend.auth.enums.RoleEnum;
import lombok.Builder;

@Builder
public record AuthUserRegisterResponseDto(
        Long id,

        String username,

        String email,

        RoleEnum role
) {}
