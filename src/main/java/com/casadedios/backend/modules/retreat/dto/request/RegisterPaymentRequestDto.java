package com.casadedios.backend.modules.retreat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

@Builder
public record RegisterPaymentRequestDto(
        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
        BigDecimal amount
) {}