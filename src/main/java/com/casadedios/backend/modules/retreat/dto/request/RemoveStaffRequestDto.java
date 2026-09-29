package com.casadedios.backend.modules.retreat.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record RemoveStaffRequestDto(
        @NotEmpty(message = "Debe indicar al menos un discípulo a eliminar")
        List<Long> discipleIds
) {}