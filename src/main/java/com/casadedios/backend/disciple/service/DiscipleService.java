package com.casadedios.backend.disciple.service;

import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.disciple.dto.request.DiscipleRegisterRequestDto;
import com.casadedios.backend.disciple.dto.request.DiscipleSearchCriteriaDto;
import com.casadedios.backend.disciple.dto.request.DiscipleUpdateRequestDto;
import com.casadedios.backend.disciple.dto.response.DiscipleChildResponseDto;
import com.casadedios.backend.disciple.dto.response.DiscipleResponseDto;

public interface DiscipleService {
    PaginationResponseDto<DiscipleResponseDto> findAll(DiscipleSearchCriteriaDto criteria);

    DiscipleResponseDto findById(Long id);

    DiscipleResponseDto create(DiscipleRegisterRequestDto request);

    DiscipleResponseDto update(Long id, DiscipleUpdateRequestDto request);

    void softDeleteById(Long id);
}
