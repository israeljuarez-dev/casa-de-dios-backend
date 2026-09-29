package com.casadedios.backend.modules.retreat.dto.request;

import com.casadedios.backend.modules.retreat.enums.PaymentStatusEnum;
import lombok.Builder;

@Builder
public record RetreatEnrollmentSearchCriteriaDto(
        String search,
        PaymentStatusEnum paymentStatus
) {
    public RetreatEnrollmentSearchCriteriaDto {
        if (search != null && search.isBlank()) {
            search = null;
        }
    }
}
