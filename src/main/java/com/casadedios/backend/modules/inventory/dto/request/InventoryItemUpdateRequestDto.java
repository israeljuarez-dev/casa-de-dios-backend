package com.casadedios.backend.modules.inventory.dto.request;

import com.casadedios.backend.modules.inventory.enums.InventorySourceEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record InventoryItemUpdateRequestDto(

        @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
        String name,

        String description,

        @DecimalMin(value = "0.0", message = "El costo no puede ser negativo")
        @Digits(integer = 8, fraction = 2, message = "El costo admite hasta 8 enteros y 2 decimales")
        BigDecimal cost,

        @Size(max = 100, message = "La categoría no puede superar los 100 caracteres")
        String category,

        InventorySourceEnum sourceType,

        Long donorDiscipleId,

        @PastOrPresent(message = "La fecha de registro no puede ser futura")
        LocalDate registeredAt

) {}