package com.casadedios.backend.modules.retreat.dto.response;

import lombok.Builder;

@Builder
public record RetreatStaffResponseDto(
        Long id,
        Long discipleId,
        String firstName,
        String lastName,
        String phoneCodeNumber,
        String phoneNumber
) {}
