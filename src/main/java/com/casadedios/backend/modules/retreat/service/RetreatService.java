package com.casadedios.backend.modules.retreat.service;

import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.modules.retreat.dto.request.*;
import com.casadedios.backend.modules.retreat.dto.response.RetreatEnrollmentResponseDto;
import com.casadedios.backend.modules.retreat.dto.response.RetreatResponseDto;
import com.casadedios.backend.modules.retreat.dto.response.RetreatStaffResponseDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface RetreatService {
    // ENCUENTRO
    PaginationResponseDto<RetreatResponseDto> findAll(RetreatSearchCriteriaDto criteria);

    RetreatResponseDto findById(Long id);

    RetreatResponseDto create(RetreatRegisterRequestDto request);

    RetreatResponseDto update(Long id, RetreatUpdateRequestDto request);

    void deleteById(Long id);

    // ASISTENTES
    List<RetreatEnrollmentResponseDto> findEnrolledDisciples(Long retreatId, RetreatEnrollmentSearchCriteriaDto criteria);

    List<RetreatEnrollmentResponseDto> enrollDisciples(Long retreatId, RetreatEnrollmentRequestDto request);

    void removeEnrolledDisciples(Long retreatId, RemoveEnrolledDisciplesRequestDto request);

    RetreatEnrollmentResponseDto registerPayment(Long retreatId, Long discipleId, RegisterPaymentRequestDto request);

    RetreatWithEnrollmentsResponseDto findRetreatWithEnrolledDisciples(Long retreatId);

    // STAFF
    List<RetreatStaffResponseDto> findStaff(Long retreatId);

    List<RetreatStaffResponseDto> addStaff(Long retreatId, RetreatStaffRequestDto request);

    void removeStaff(Long retreatId, RemoveStaffRequestDto request);

    ByteArrayOutputStream exportFullReport(Long retreatId) throws IOException;

    ByteArrayOutputStream exportAttendees(Long retreatId) throws IOException;

    ByteArrayOutputStream exportAttendeesWithPayments(Long retreatId) throws IOException;

    ByteArrayOutputStream exportStaff(Long retreatId) throws IOException;
}
