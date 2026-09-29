package com.casadedios.backend.modules.disciple.controller;

import com.casadedios.backend.common.dto.response.ApiResponseBodyDto;
import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.common.export.response.ExcelResponseFactory;
import com.casadedios.backend.modules.disciple.controller.documentation.DiscipleControllerDocumentation;
import com.casadedios.backend.modules.disciple.dto.request.DiscipleRegisterRequestDto;
import com.casadedios.backend.modules.disciple.dto.request.DiscipleSearchCriteriaDto;
import com.casadedios.backend.modules.disciple.dto.request.DiscipleUpdateRequestDto;
import com.casadedios.backend.modules.disciple.dto.response.DiscipleResponseDto;
import com.casadedios.backend.modules.disciple.service.DiscipleService;
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
@RequestMapping("/disciples")
public class DiscipleController implements DiscipleControllerDocumentation {

    private final DiscipleService discipleService;

    private final ExcelResponseFactory excelResponseFactory;

    @GetMapping
    @Override
    public ResponseEntity<ApiResponseBodyDto<PaginationResponseDto<DiscipleResponseDto>>> findAll(@ModelAttribute DiscipleSearchCriteriaDto criteria) {
        PaginationResponseDto<DiscipleResponseDto> result = discipleService.findAll(criteria);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Listado obtenido exitosamente", result));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseBodyDto<DiscipleResponseDto>> findById(@PathVariable("id") @Min(1) Long id) {
        DiscipleResponseDto result = discipleService.findById(id);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Discípulo encontrado", result));
    }

    @PostMapping
    @Override
    public ResponseEntity<ApiResponseBodyDto<DiscipleResponseDto>> create(@RequestBody @Valid DiscipleRegisterRequestDto request) {
        DiscipleResponseDto created = discipleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseBodyDto<>("Discípulo registrado exitosamente", created));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseBodyDto<DiscipleResponseDto>> update(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid DiscipleUpdateRequestDto request
    ) {
        DiscipleResponseDto updated = discipleService.update(id, request);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Discípulo actualizado exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseBodyDto<Void>> softDeleteById(@PathVariable("id") @Min(1) Long id) {
        discipleService.softDeleteById(id);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Discípulo eliminado exitosamente", null));
    }

    @GetMapping("/export/excel")
    @Override
    public ResponseEntity<byte[]> exportToExcel(@ModelAttribute DiscipleSearchCriteriaDto criteria) throws IOException {
        ByteArrayOutputStream outputStream = discipleService.exportToExcel(criteria);
        String fileName = excelResponseFactory.fileNameWithDate("Reporte_Discipulos");
        return excelResponseFactory.build(outputStream, fileName);
    }
}
