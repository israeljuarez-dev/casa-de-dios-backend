package com.casadedios.backend.cellgroup.dto.response;

import lombok.Builder;

@Builder
public record CellGroupLeaderResponseDto(
        Long id,
        String firstName,
        String lastName
) {}