package com.casadedios.backend.modules.inventory.service;

import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemRegisterRequestDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemSearchCriteriaDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemStockAdjustmentRequestDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemUpdateRequestDto;
import com.casadedios.backend.modules.inventory.dto.response.InventoryItemResponseDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface InventoryItemService {
    PaginationResponseDto<InventoryItemResponseDto> findAll(InventoryItemSearchCriteriaDto criteria);

    InventoryItemResponseDto findById(Long id);

    InventoryItemResponseDto create(InventoryItemRegisterRequestDto request);

    InventoryItemResponseDto update(Long id, InventoryItemUpdateRequestDto request);

    void deleteById(Long id);

    // Stock
    InventoryItemResponseDto adjustStock(Long id, InventoryItemStockAdjustmentRequestDto request);

    // Export
    ByteArrayOutputStream exportToExcel(InventoryItemSearchCriteriaDto criteria) throws IOException;
}
