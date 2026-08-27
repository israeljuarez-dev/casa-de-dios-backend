package com.casadedios.backend.cellgroup.controller;

import com.casadedios.backend.cellgroup.controller.documentation.CellGroupControllerDocumentation;
import com.casadedios.backend.cellgroup.dto.request.*;
import com.casadedios.backend.cellgroup.dto.response.CellGroupMemberResponseDto;
import com.casadedios.backend.cellgroup.dto.response.CellGroupResponseDto;
import com.casadedios.backend.cellgroup.service.CellGroupService;
import com.casadedios.backend.common.dto.response.ApiResponseDto;
import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/cell-groups")
public class CellGroupController implements CellGroupControllerDocumentation {

    private final CellGroupService cellGroupService;

    @GetMapping
    @Override
    public ResponseEntity<ApiResponseDto<PaginationResponseDto<CellGroupResponseDto>>> findAll(
            @ModelAttribute CellGroupSearchCriteriaDto criteria
    ) {
        PaginationResponseDto<CellGroupResponseDto> result = cellGroupService.findAll(criteria);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Listado obtenido exitosamente", result));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseDto<CellGroupResponseDto>> findById(@PathVariable @Min(1) Long id) {
        CellGroupResponseDto result = cellGroupService.findById(id);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Célula encontrada", result));
    }

    @PostMapping
    @Override
    public ResponseEntity<ApiResponseDto<CellGroupResponseDto>> create(@RequestBody @Valid CellGroupRegisterRequestDto request) {
        CellGroupResponseDto created = cellGroupService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(HttpStatus.CREATED.value(), "Célula registrada exitosamente", created));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseDto<CellGroupResponseDto>> update(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid CellGroupUpdateRequestDto request
    ) {
        CellGroupResponseDto updated = cellGroupService.update(id, request);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Célula actualizada exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseDto<Void>> deleteById(@PathVariable @Min(1) Long id) {
        cellGroupService.deleteById(id);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Célula eliminada exitosamente", null));
    }

    @GetMapping("/{id}/members")
    @Override
    public ResponseEntity<ApiResponseDto<List<CellGroupMemberResponseDto>>> findMembers(
            @PathVariable @Min(1) Long id,
            @ModelAttribute CellGroupMemberSearchCriteriaDto criteria
    ) {
        List<CellGroupMemberResponseDto> result = cellGroupService.findMembers(id, criteria);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Miembros obtenidos exitosamente", result));
    }

    @PostMapping("/{id}/members")
    @Override
    public ResponseEntity<ApiResponseDto<CellGroupMemberResponseDto>> addMember(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid CellGroupMemberRequestDto request
    ) {
        CellGroupMemberResponseDto added = cellGroupService.addMember(id, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(HttpStatus.CREATED.value(), "Discípulo añadido a la célula", added));
    }

    @DeleteMapping("/{id}/members/{discipleId}")
    @Override
    public ResponseEntity<ApiResponseDto<Void>> removeMember(@PathVariable @Min(1) Long id, @PathVariable @Min(1) Long discipleId) {
        cellGroupService.removeMember(id, discipleId);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Discípulo removido de la célula", null));
    }

    @PatchMapping("/{id}/members/{discipleId}/core-twelve")
    @Override
    public ResponseEntity<ApiResponseDto<CellGroupMemberResponseDto>> markAsCoreTwelve(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long discipleId
    ) {
        CellGroupMemberResponseDto result = cellGroupService.markAsCoreTwelve(id, discipleId);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Discípulo marcado como Los 12", result));
    }

    @DeleteMapping("/{id}/members/{discipleId}/core-twelve")
    @Override
    public ResponseEntity<ApiResponseDto<Void>> unmarkAsCoreTwelve(@PathVariable @Min(1) Long id, @PathVariable @Min(1) Long discipleId) {
        cellGroupService.unmarkAsCoreTwelve(id, discipleId);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Discípulo removido de Los 12", null));
    }

    @PatchMapping("/{id}/members/{discipleId}/pastor-core-twelve")
    @Override
    public ResponseEntity<ApiResponseDto<CellGroupMemberResponseDto>> markAsPastorCoreTwelve(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long discipleId
    ) {
        CellGroupMemberResponseDto result = cellGroupService.markAsPastorCoreTwelve(id, discipleId);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Discípulo marcado como Los 12 del pastor", result));
    }

    @DeleteMapping("/{id}/members/{discipleId}/pastor-core-twelve")
    @Override
    public ResponseEntity<ApiResponseDto<Void>> unmarkAsPastorCoreTwelve(
            @PathVariable @Min(1) Long id,
            @PathVariable @Min(1) Long discipleId
    ) {
        cellGroupService.unmarkAsPastorCoreTwelve(id, discipleId);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Discípulo removido de Los 12 del pastor", null));
    }

    @GetMapping("/pastor-core-twelve")
    @Override
    public ResponseEntity<ApiResponseDto<List<CellGroupMemberResponseDto>>> findPastorCoreTwelve() {
        List<CellGroupMemberResponseDto> result = cellGroupService.findPastorCoreTwelve();
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Los 12 del pastor obtenidos exitosamente", result));
    }

    @GetMapping("/export/excel")
    @Override
    public ResponseEntity<byte[]> exportToExcel(@ModelAttribute CellGroupSearchCriteriaDto criteria) {
        try {
            ByteArrayOutputStream outputStream = cellGroupService.exportToExcel(criteria);
            byte[] excelBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", cellGroupService.generateExcelFileName());
            headers.setContentLength(excelBytes.length);

            log.info("Reporte de células exportado exitosamente");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IOException exception) {
            log.error("Error al exportar reporte de células", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}