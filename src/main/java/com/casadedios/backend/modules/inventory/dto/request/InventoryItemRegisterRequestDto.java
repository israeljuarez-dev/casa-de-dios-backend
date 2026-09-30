package com.casadedios.backend.modules.inventory.dto.request;

import com.casadedios.backend.modules.inventory.enums.InventorySourceEnum;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record InventoryItemRegisterRequestDto(

        @NotBlank(message = "El nombre del ítem es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
        String name,

        String description,

        @DecimalMin(value = "0.0", message = "El costo no puede ser negativo")
        @Digits(integer = 8, fraction = 2, message = "El costo admite hasta 8 enteros y 2 decimales")
        BigDecimal cost,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 0, message = "La cantidad no puede ser negativa")
        Integer quantity,

        @NotBlank(message = "La categoría es obligatoria")
        @Size(max = 100, message = "La categoría no puede superar los 100 caracteres")
        String category,

        @NotNull(message = "El origen del ítem es obligatorio")
        InventorySourceEnum sourceType,

        Long donorDiscipleId,

        @PastOrPresent(message = "La fecha de registro no puede ser futura")
        LocalDate registeredAt

) {}
