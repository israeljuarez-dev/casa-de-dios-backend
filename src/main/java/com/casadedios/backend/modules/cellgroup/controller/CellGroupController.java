package com.casadedios.backend.modules.cellgroup.controller;

import com.casadedios.backend.common.dto.response.ApiResponseBodyDto;
import com.casadedios.backend.common.export.response.ExcelResponseFactory;
import com.casadedios.backend.modules.cellgroup.controller.documentation.CellGroupControllerDocumentation;
import com.casadedios.backend.modules.cellgroup.dto.request.*;
import com.casadedios.backend.modules.cellgroup.dto.response.CellGroupMemberResponseDto;
import com.casadedios.backend.modules.cellgroup.dto.response.CellGroupResponseDto;
import com.casadedios.backend.modules.cellgroup.service.CellGroupService;
import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/cell-groups")
public class CellGroupController implements CellGroupControllerDocumentation {

    private final CellGroupService cellGroupService;

    private final ExcelResponseFactory excelResponseFactory;

    @GetMapping
    @Override
    public ResponseEntity<ApiResponseBodyDto<PaginationResponseDto<CellGroupResponseDto>>> findAll(
            @ModelAttribute CellGroupSearchCriteriaDto criteria
    ) {
        PaginationResponseDto<CellGroupResponseDto> result = cellGroupService.findAll(criteria);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Listado obtenido exitosamente", result));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseBodyDto<CellGroupResponseDto>> findById(@PathVariable("id") @Min(1) Long id) {
        CellGroupResponseDto result = cellGroupService.findById(id);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Célula encontrada", result));
    }

    @PostMapping
    @Override
    public ResponseEntity<ApiResponseBodyDto<CellGroupResponseDto>> create(@RequestBody @Valid CellGroupRegisterRequestDto request) {
        CellGroupResponseDto created = cellGroupService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseBodyDto<>("Célula registrada exitosamente", created));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseBodyDto<CellGroupResponseDto>> update(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid CellGroupUpdateRequestDto request
    ) {
        CellGroupResponseDto updated = cellGroupService.update(id, request);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Célula actualizada exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseBodyDto<Void>> deleteById(@PathVariable("id") @Min(1) Long id) {
        cellGroupService.deleteById(id);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Célula eliminada exitosamente", null));
    }

    @GetMapping("/{id}/members")
    @Override
    public ResponseEntity<ApiResponseBodyDto<List<CellGroupMemberResponseDto>>> findMembers(
            @PathVariable("id") @Min(1) Long id,
            @ModelAttribute CellGroupMemberSearchCriteriaDto criteria
    ) {
        List<CellGroupMemberResponseDto> result = cellGroupService.findMembers(id, criteria);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Miembros obtenidos exitosamente", result));
    }

    @PostMapping("/{id}/members")
    @Override
    public ResponseEntity<ApiResponseBodyDto<CellGroupMemberResponseDto>> addMember(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid CellGroupMemberRequestDto request
    ) {
        CellGroupMemberResponseDto added = cellGroupService.addMember(id, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseBodyDto<>("Discípulo añadido a la célula", added));
    }

    @DeleteMapping("/{id}/members/{discipleId}")
    @Override
    public ResponseEntity<ApiResponseBodyDto<Void>> removeMember(
            @PathVariable("id") @Min(1) Long id,
            @PathVariable("discipleId") @Min(1) Long discipleId
    ) {
        cellGroupService.removeMember(id, discipleId);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Discípulo removido de la célula", null));
    }

    @PatchMapping("/{id}/members/{discipleId}/core-twelve")
    @Override
    public ResponseEntity<ApiResponseBodyDto<CellGroupMemberResponseDto>> markAsCoreTwelve(
            @PathVariable("id") @Min(1) Long id,
            @PathVariable("discipleId") @Min(1) Long discipleId
    ) {
        CellGroupMemberResponseDto result = cellGroupService.markAsCoreTwelve(id, discipleId);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Discípulo marcado como Los 12", result));
    }

    @DeleteMapping("/{id}/members/{discipleId}/core-twelve")
    @Override
    public ResponseEntity<ApiResponseBodyDto<Void>> unmarkAsCoreTwelve(
            @PathVariable("id") @Min(1) Long id,
            @PathVariable("discipleId") @Min(1) Long discipleId
    ) {
        cellGroupService.unmarkAsCoreTwelve(id, discipleId);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Discípulo removido de Los 12", null));
    }

    @PatchMapping("/{id}/members/{discipleId}/pastor-core-twelve")
    @Override
    public ResponseEntity<ApiResponseBodyDto<CellGroupMemberResponseDto>> markAsPastorCoreTwelve(
            @PathVariable("id") @Min(1) Long id,
            @PathVariable("discipleId") @Min(1) Long discipleId
    ) {
        CellGroupMemberResponseDto result = cellGroupService.markAsPastorCoreTwelve(id, discipleId);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Discípulo marcado como Los 12 del pastor", result));
    }

    @DeleteMapping("/{id}/members/{discipleId}/pastor-core-twelve")
    @Override
    public ResponseEntity<ApiResponseBodyDto<Void>> unmarkAsPastorCoreTwelve(
            @PathVariable("id") @Min(1) Long id,
            @PathVariable("discipleId") @Min(1) Long discipleId
    ) {
        cellGroupService.unmarkAsPastorCoreTwelve(id, discipleId);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Discípulo removido de Los 12 del pastor", null));
    }

    @GetMapping("/pastor-core-twelve")
    @Override
    public ResponseEntity<ApiResponseBodyDto<List<CellGroupMemberResponseDto>>> findPastorCoreTwelve() {
        List<CellGroupMemberResponseDto> result = cellGroupService.findPastorCoreTwelve();
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Los 12 del pastor obtenidos exitosamente", result));
    }

    @GetMapping("/export/excel")
    @Override
    public ResponseEntity<byte[]> exportToExcel(@ModelAttribute CellGroupSearchCriteriaDto criteria) throws IOException{
        ByteArrayOutputStream outputStream = cellGroupService.exportToExcel(criteria);
        String fileName = excelResponseFactory.fileNameWithDate("Reporte_Celulas");
        return excelResponseFactory.build(outputStream, fileName);
    }
}