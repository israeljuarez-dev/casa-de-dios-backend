package com.casadedios.backend.disciple.dto.response;

import lombok.Builder;

@Builder
public record DiscipleInviterResponseDto(
        Long id,

        String firstName,

        String lastName
) {}

