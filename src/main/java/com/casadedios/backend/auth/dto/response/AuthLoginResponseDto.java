package com.casadedios.backend.auth.dto.response;
import lombok.Builder;

@Builder
public record AuthLoginResponseDto(
        String usernameOrEmail,
        String jwt
) {}