package com.casadedios.backend.modules.inventory.dto.response;

import com.casadedios.backend.modules.inventory.enums.InventorySourceEnum;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record InventoryItemResponseDto(
        Long id,
        String name,
        String description,
        BigDecimal cost,
        int quantity,
        String category,
        InventorySourceEnum sourceType,
        InventoryItemDonorResponseDto donor,
        LocalDate registeredAt
) {}
