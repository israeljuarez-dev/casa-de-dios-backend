package com.casadedios.backend.modules.cellgroup.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CellGroupMemberRequestDto(
        @NotNull(message = "El ID del discípulo es obligatorio")
        Long discipleId
) {}
