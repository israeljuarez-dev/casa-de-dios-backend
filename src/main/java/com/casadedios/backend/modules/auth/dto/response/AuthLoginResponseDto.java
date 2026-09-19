package com.casadedios.backend.modules.auth.dto.response;
import lombok.Builder;

@Builder
public record AuthLoginResponseDto(
        String usernameOrEmail,
        String jwt
) {}