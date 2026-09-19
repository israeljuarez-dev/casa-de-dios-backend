package com.casadedios.backend.modules.cellgroup.service;

import com.casadedios.backend.cellgroup.dto.request.*;
import com.casadedios.backend.modules.cellgroup.dto.request.*;
import com.casadedios.backend.modules.cellgroup.dto.response.CellGroupMemberResponseDto;
import com.casadedios.backend.modules.cellgroup.dto.response.CellGroupResponseDto;
import com.casadedios.backend.common.dto.response.PaginationResponseDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface CellGroupService {

    PaginationResponseDto<CellGroupResponseDto> findAll(CellGroupSearchCriteriaDto criteria);

    CellGroupResponseDto findById(Long id);

    CellGroupResponseDto create(CellGroupRegisterRequestDto request);

    CellGroupResponseDto update(Long id, CellGroupUpdateRequestDto request);

    void deleteById(Long id);

    // Miembros
    // List<CellGroupMemberResponseDto> findMembers(Long cellGroupId);

    List<CellGroupMemberResponseDto> findMembers(Long cellGroupId, CellGroupMemberSearchCriteriaDto criteria);

    CellGroupMemberResponseDto addMember(Long cellGroupId, CellGroupMemberRequestDto request);

    void removeMember(Long cellGroupId, Long discipleId);

    // Los 12
    CellGroupMemberResponseDto markAsCoreTwelve(Long cellGroupId, Long discipleId);

    void unmarkAsCoreTwelve(Long cellGroupId, Long discipleId);

    CellGroupMemberResponseDto markAsPastorCoreTwelve(Long cellGroupId, Long discipleId);

    void unmarkAsPastorCoreTwelve(Long cellGroupId, Long discipleId);

    List<CellGroupMemberResponseDto> findPastorCoreTwelve();

    // Export
    ByteArrayOutputStream exportToExcel(CellGroupSearchCriteriaDto criteria) throws IOException;

    String generateExcelFileName();
}
