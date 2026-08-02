package com.casadedios.backend.disciple.controller;

import com.casadedios.backend.common.dto.response.ApiResponseDto;
import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.disciple.controller.documentation.DiscipleControllerDocumentation;
import com.casadedios.backend.disciple.dto.request.DiscipleRegisterRequestDto;
import com.casadedios.backend.disciple.dto.request.DiscipleSearchCriteriaDto;
import com.casadedios.backend.disciple.dto.request.DiscipleUpdateRequestDto;
import com.casadedios.backend.disciple.dto.response.DiscipleResponseDto;
import com.casadedios.backend.disciple.service.DiscipleService;
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

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/v1/disciples")
public class DiscipleController implements DiscipleControllerDocumentation {

    private final DiscipleService discipleService;

    @GetMapping
    @Override
    public ResponseEntity<ApiResponseDto<PaginationResponseDto<DiscipleResponseDto>>> findAll(@ModelAttribute DiscipleSearchCriteriaDto criteria) {
        PaginationResponseDto<DiscipleResponseDto> result = discipleService.findAll(criteria);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Listado obtenido exitosamente", result));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseDto<DiscipleResponseDto>> findById(@PathVariable @Min(1) Long id) {
        DiscipleResponseDto result = discipleService.findById(id);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Discípulo encontrado", result));
    }

    @PostMapping
    @Override
    public ResponseEntity<ApiResponseDto<DiscipleResponseDto>> create(@RequestBody @Valid DiscipleRegisterRequestDto request) {
        DiscipleResponseDto created = discipleService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(HttpStatus.CREATED.value(), "Discípulo registrado exitosamente", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<DiscipleResponseDto>> update(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid DiscipleUpdateRequestDto request
    ) {
        DiscipleResponseDto updated = discipleService.update(id, request);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Discípulo actualizado exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseDto<Void>> softDeleteById(@PathVariable @Min(1) Long id) {
        discipleService.softDeleteById(id);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Discípulo eliminado exitosamente", null));
    }

    @GetMapping("/export/excel")
    @Override
    public ResponseEntity<byte[]> exportToExcel(@ModelAttribute DiscipleSearchCriteriaDto criteria) {
        try {
            ByteArrayOutputStream outputStream = discipleService.exportToExcel(criteria);
            byte[] excelBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", discipleService.generateExcelFileName());
            headers.setContentLength(excelBytes.length);

            log.info("Reporte de discípulos exportado exitosamente");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IOException exception) {
            log.error("Error al exportar reporte de discípulos", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
