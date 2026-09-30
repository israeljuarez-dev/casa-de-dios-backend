package com.casadedios.backend.modules.inventory.dto.response;

import lombok.Builder;

@Builder
public record InventoryItemDonorResponseDto(
        Long id,
        String firstName,
        String lastName
) {}
