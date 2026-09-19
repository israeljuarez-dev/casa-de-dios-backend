package com.casadedios.backend.modules.disciple.service;

import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.modules.disciple.dto.request.DiscipleRegisterRequestDto;
import com.casadedios.backend.modules.disciple.dto.request.DiscipleSearchCriteriaDto;
import com.casadedios.backend.modules.disciple.dto.request.DiscipleUpdateRequestDto;
import com.casadedios.backend.modules.disciple.dto.response.DiscipleResponseDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface DiscipleService {
    PaginationResponseDto<DiscipleResponseDto> findAll(DiscipleSearchCriteriaDto criteria);

    DiscipleResponseDto findById(Long id);

    DiscipleResponseDto create(DiscipleRegisterRequestDto request);

    DiscipleResponseDto update(Long id, DiscipleUpdateRequestDto request);

    void softDeleteById(Long id);

    ByteArrayOutputStream exportToExcel(DiscipleSearchCriteriaDto criteria) throws IOException;

    String generateExcelFileName();
}
