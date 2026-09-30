package com.casadedios.backend.modules.inventory.dto.request;

import com.casadedios.backend.modules.inventory.enums.StockAdjustmentTypeEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record InventoryItemStockAdjustmentRequestDto(

        @NotNull(message = "El tipo de ajuste es obligatorio")
        StockAdjustmentTypeEnum type,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a cero")
        Integer quantity

) {}
