package com.casadedios.backend.modules.retreat.dto.request;

import com.casadedios.backend.modules.retreat.dto.response.RetreatEnrollmentResponseDto;
import com.casadedios.backend.modules.retreat.dto.response.RetreatResponseDto;
import lombok.Builder;

import java.util.List;

@Builder
public record RetreatWithEnrollmentsResponseDto(
        RetreatResponseDto retreat,
        List<RetreatEnrollmentResponseDto> enrollments
) {}