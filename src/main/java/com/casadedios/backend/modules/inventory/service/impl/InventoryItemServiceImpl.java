package com.casadedios.backend.modules.inventory.service.impl;

import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.common.exception.model.CasaDeDiosException;
import com.casadedios.backend.common.util.StringUtils;
import com.casadedios.backend.modules.disciple.exception.DiscipleErrorEnum;
import com.casadedios.backend.modules.disciple.persistence.model.Disciple;
import com.casadedios.backend.modules.disciple.persistence.repository.DiscipleRepository;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemRegisterRequestDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemSearchCriteriaDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemStockAdjustmentRequestDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemUpdateRequestDto;
import com.casadedios.backend.modules.inventory.dto.response.InventoryItemResponseDto;
import com.casadedios.backend.modules.inventory.enums.InventorySourceEnum;
import com.casadedios.backend.modules.inventory.exception.InventoryErrorEnum;
import com.casadedios.backend.modules.inventory.export.InventoryItemExcelExporter;
import com.casadedios.backend.modules.inventory.mapper.InventoryItemMapper;
import com.casadedios.backend.modules.inventory.persistence.model.InventoryItem;
import com.casadedios.backend.modules.inventory.persistence.projection.InventoryItemSummaryProjection;
import com.casadedios.backend.modules.inventory.persistence.repository.InventoryItemRepository;
import com.casadedios.backend.modules.inventory.service.InventoryItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;

    private final DiscipleRepository discipleRepository;

    private final InventoryItemMapper inventoryItemMapper;

    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<InventoryItemResponseDto> findAll(InventoryItemSearchCriteriaDto criteria) {
        Pageable pageable = criteria.pagination().toPageable();

        Page<InventoryItemSummaryProjection> page = searchWithDonor(criteria, pageable);

        List<InventoryItemResponseDto> content = page.getContent().stream()
                .map(inventoryItemMapper::toResponseDto)
                .toList();

        return PaginationResponseDto.of(content, page);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryItemResponseDto findById(Long id) {
        return inventoryItemRepository.findByIdWithDonor(id)
                .map(inventoryItemMapper::toResponseDto)
                .orElseThrow(() -> {
                    log.debug("No existe ítem de inventario con el id {}", id);
                    return new CasaDeDiosException(InventoryErrorEnum.INVENTORY_ITEM_NOT_FOUND);
                });
    }

    @Override
    @Transactional
    public InventoryItemResponseDto create(InventoryItemRegisterRequestDto request) {
        InventoryItem item = inventoryItemMapper.toEntity(request);
        item.setDonor(resolveDonor(request.sourceType(), request.donorDiscipleId()));

        if (item.getRegisteredAt() == null) {
            item.setRegisteredAt(LocalDate.now(clock));
        }

        InventoryItem saved = inventoryItemRepository.save(item);

        log.info("Ítem de inventario '{}' registrado exitosamente con id {}", saved.getName(), saved.getId());

        return findById(saved.getId());
    }

    @Override
    @Transactional
    public InventoryItemResponseDto update(Long id, InventoryItemUpdateRequestDto request) {
        InventoryItem item = getInventoryItemOrThrow(id);

        InventorySourceEnum finalSourceType = Objects.requireNonNullElse(request.sourceType(), item.getSourceType());
        Long finalDonorDiscipleId = request.donorDiscipleId() != null
                ? request.donorDiscipleId()
                : getCurrentDonorId(item);

        inventoryItemMapper.updateEntityFromDto(request, item);
        item.setDonor(resolveDonor(finalSourceType, finalDonorDiscipleId));

        InventoryItem updated = inventoryItemRepository.save(item);

        log.info("Ítem de inventario con id {} actualizado exitosamente", updated.getId());

        return findById(updated.getId());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!inventoryItemRepository.existsById(id)) {
            log.debug("No existe ítem de inventario con id {} para eliminar", id);
            throw new CasaDeDiosException(InventoryErrorEnum.INVENTORY_ITEM_NOT_FOUND);
        }

        inventoryItemRepository.deleteById(id);

        log.warn("Ítem de inventario con id {} eliminado", id);
    }

    @Override
    @Transactional
    public InventoryItemResponseDto adjustStock(Long id, InventoryItemStockAdjustmentRequestDto request) {
        int signedQuantity = request.type().toSignedQuantity(request.quantity());

        int updatedRows = inventoryItemRepository.adjustStock(id, signedQuantity, Instant.now(clock));

        if (updatedRows == 0) {
            throw buildStockAdjustmentFailure(id);
        }

        log.info("Stock del ítem {} ajustado en {} unidades ({})", id, signedQuantity, request.type());

        return findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ByteArrayOutputStream exportToExcel(InventoryItemSearchCriteriaDto criteria) throws IOException {
        // Sin paginación (se exportan todos los resultados), pero respetando el orden solicitado
        Pageable unpagedWithSort = Pageable.unpaged(criteria.pagination().toPageable().getSort());

        List<InventoryItemResponseDto> items = searchWithDonor(criteria, unpagedWithSort).getContent().stream()
                .map(inventoryItemMapper::toResponseDto)
                .toList();

        InventoryItemExcelExporter exporter = new InventoryItemExcelExporter(items);
        ByteArrayOutputStream outputStream = exporter.export();

        log.info("Reporte de inventario exportado exitosamente con {} registros", items.size());

        return outputStream;
    }

    /* ============================ PRIVADOS  ============================  */

    private Page<InventoryItemSummaryProjection> searchWithDonor(InventoryItemSearchCriteriaDto criteria, Pageable pageable) {
        String sourceTypeParam = criteria.sourceType() != null
                ? criteria.sourceType().name()
                : null;

        return inventoryItemRepository.findAllWithDonor(
                StringUtils.blankToNull(criteria.name()),
                StringUtils.blankToNull(criteria.category()),
                sourceTypeParam,
                StringUtils.blankToNull(criteria.donorName()),
                criteria.registeredFrom(),
                criteria.registeredTo(),
                pageable
        );
    }

    private InventoryItem getInventoryItemOrThrow(Long id) {
        return inventoryItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.debug("No existe ítem de inventario con id {}", id);
                    return new CasaDeDiosException(InventoryErrorEnum.INVENTORY_ITEM_NOT_FOUND);
                });
    }

    private Disciple getDiscipleOrThrow(Long id) {
        return discipleRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.debug("No existe discípulo activo con id {}", id);
                    return new CasaDeDiosException(DiscipleErrorEnum.DISCIPLE_NOT_FOUND);
                });
    }

    private Long getCurrentDonorId(InventoryItem item) {
        return item.getDonor() != null ? item.getDonor().getId() : null;
    }

    // Regla única de origen/donante, compartida por create y update
    private Disciple resolveDonor(InventorySourceEnum sourceType, Long donorDiscipleId) {
        if (sourceType == InventorySourceEnum.PURCHASED) {
            return null;
        }

        if (donorDiscipleId == null) {
            log.warn("Intento de registrar/editar un ítem donado sin especificar el donante");
            throw new CasaDeDiosException(InventoryErrorEnum.INVENTORY_DONOR_REQUIRED);
        }

        return getDiscipleOrThrow(donorDiscipleId);
    }

    private CasaDeDiosException buildStockAdjustmentFailure(Long id) {
        if (inventoryItemRepository.existsById(id)) {
            log.warn("Stock insuficiente para el ítem {}", id);
            return new CasaDeDiosException(InventoryErrorEnum.INVENTORY_INSUFFICIENT_STOCK);
        }

        log.debug("No existe ítem de inventario con id {}", id);
        return new CasaDeDiosException(InventoryErrorEnum.INVENTORY_ITEM_NOT_FOUND);
    }
}
