package com.casadedios.backend.modules.retreat.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record RetreatResponseDto(
        Long id,
        String name,
        String location,
        BigDecimal price,
        Instant startDate,
        Instant endDate,
        int enrolledCount,
        int staffCount
) {}