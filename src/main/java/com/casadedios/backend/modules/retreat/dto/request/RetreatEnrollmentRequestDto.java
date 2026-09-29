package com.casadedios.backend.modules.retreat.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record RetreatEnrollmentRequestDto(
        @NotEmpty(message = "Debe indicar al menos un discípulo")
        List<Long> discipleIds
) {}