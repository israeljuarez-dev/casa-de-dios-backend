package com.casadedios.backend.modules.inventory.controller;

import com.casadedios.backend.common.dto.response.ApiResponseBodyDto;
import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.common.export.response.ExcelResponseFactory;
import com.casadedios.backend.modules.inventory.controller.documentation.InventoryItemControllerDocumentation;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemRegisterRequestDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemSearchCriteriaDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemStockAdjustmentRequestDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemUpdateRequestDto;
import com.casadedios.backend.modules.inventory.dto.response.InventoryItemResponseDto;
import com.casadedios.backend.modules.inventory.service.InventoryItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/inventory-items")
public class InventoryItemController implements InventoryItemControllerDocumentation {

    private final InventoryItemService inventoryItemService;

    private final ExcelResponseFactory excelResponseFactory;

    @GetMapping
    @Override
    public ResponseEntity<ApiResponseBodyDto<PaginationResponseDto<InventoryItemResponseDto>>> findAll(
            @ModelAttribute InventoryItemSearchCriteriaDto criteria
    ) {
        PaginationResponseDto<InventoryItemResponseDto> result = inventoryItemService.findAll(criteria);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Listado obtenido exitosamente", result));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseBodyDto<InventoryItemResponseDto>> findById(@PathVariable("id") @Min(1) Long id) {
        InventoryItemResponseDto result = inventoryItemService.findById(id);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Ítem de inventario encontrado", result));
    }

    @PostMapping
    @Override
    public ResponseEntity<ApiResponseBodyDto<InventoryItemResponseDto>> create(
            @RequestBody @Valid InventoryItemRegisterRequestDto request
    ) {
        InventoryItemResponseDto created = inventoryItemService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseBodyDto<>("Ítem de inventario registrado exitosamente", created));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseBodyDto<InventoryItemResponseDto>> update(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid InventoryItemUpdateRequestDto request
    ) {
        InventoryItemResponseDto updated = inventoryItemService.update(id, request);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Ítem de inventario actualizado exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseBodyDto<Void>> deleteById(@PathVariable("id") @Min(1) Long id) {
        inventoryItemService.deleteById(id);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Ítem de inventario eliminado exitosamente", null));
    }

    @PatchMapping("/{id}/stock")
    @Override
    public ResponseEntity<ApiResponseBodyDto<InventoryItemResponseDto>> adjustStock(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid InventoryItemStockAdjustmentRequestDto request
    ) {
        InventoryItemResponseDto result = inventoryItemService.adjustStock(id, request);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Stock ajustado exitosamente", result));
    }

    @GetMapping("/export/excel")
    @Override
    public ResponseEntity<byte[]> exportToExcel(@ModelAttribute InventoryItemSearchCriteriaDto criteria) throws IOException {
        ByteArrayOutputStream outputStream = inventoryItemService.exportToExcel(criteria);
        String fileName = excelResponseFactory.fileNameWithDate("Reporte_Inventario");
        return excelResponseFactory.build(outputStream, fileName);
    }
}
