package com.casadedios.backend.modules.retreat.dto.response;

import com.casadedios.backend.modules.retreat.enums.PaymentStatusEnum;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RetreatEnrollmentResponseDto(
        Long enrollmentId,
        Long discipleId,
        String firstName,
        String lastName,
        String phoneCodeNumber,
        String phoneNumber,
        PaymentStatusEnum paymentStatus,
        BigDecimal amountPaid,
        BigDecimal amountPending
) {}
