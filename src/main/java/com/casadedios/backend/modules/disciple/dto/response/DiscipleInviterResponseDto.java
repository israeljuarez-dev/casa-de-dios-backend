package com.casadedios.backend.modules.disciple.dto.response;

import lombok.Builder;

@Builder
public record DiscipleInviterResponseDto(
        Long id,

        String firstName,

        String lastName
) {}

