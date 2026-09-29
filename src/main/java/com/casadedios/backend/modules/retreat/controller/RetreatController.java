package com.casadedios.backend.modules.retreat.controller;

import com.casadedios.backend.common.dto.response.ApiResponseBodyDto;
import com.casadedios.backend.common.export.response.ExcelResponseFactory;
import com.casadedios.backend.modules.retreat.controller.documentation.RetreatControllerDocumentation;
import com.casadedios.backend.modules.retreat.dto.request.*;
import com.casadedios.backend.modules.retreat.dto.response.*;
import com.casadedios.backend.modules.retreat.service.RetreatService;
import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/retreats")
public class RetreatController implements RetreatControllerDocumentation {

    private final RetreatService retreatService;

    private final ExcelResponseFactory excelResponseFactory;

    @GetMapping
    @Override
    public ResponseEntity<ApiResponseBodyDto<PaginationResponseDto<RetreatResponseDto>>> findAll(
            @ModelAttribute RetreatSearchCriteriaDto criteria
    ) {
        PaginationResponseDto<RetreatResponseDto> result = retreatService.findAll(criteria);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Listado obtenido exitosamente", result));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseBodyDto<RetreatResponseDto>> findById(@PathVariable("id") @Min(1) Long id) {
        RetreatResponseDto result = retreatService.findById(id);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Encuentro encontrado", result));
    }

    @PostMapping
    @Override
    public ResponseEntity<ApiResponseBodyDto<RetreatResponseDto>> create(@RequestBody @Valid RetreatRegisterRequestDto request) {
        RetreatResponseDto created = retreatService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseBodyDto<>("Encuentro registrado exitosamente", created));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseBodyDto<RetreatResponseDto>> update(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid RetreatUpdateRequestDto request
    ) {
        RetreatResponseDto updated = retreatService.update(id, request);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Encuentro actualizado exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<ApiResponseBodyDto<Void>> deleteById(@PathVariable("id") @Min(1) Long id) {
        retreatService.deleteById(id);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Encuentro eliminado exitosamente", null));
    }

    @GetMapping("/{id}/enrollments")
    @Override
    public ResponseEntity<ApiResponseBodyDto<List<RetreatEnrollmentResponseDto>>> findEnrolledDisciples(
            @PathVariable("id") @Min(1) Long id,
            @ModelAttribute RetreatEnrollmentSearchCriteriaDto criteria
    ) {
        List<RetreatEnrollmentResponseDto> result = retreatService.findEnrolledDisciples(id, criteria);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Asistentes obtenidos exitosamente", result));
    }

    @PostMapping("/{id}/enrollments")
    @Override
    public ResponseEntity<ApiResponseBodyDto<List<RetreatEnrollmentResponseDto>>> enrollDisciples(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid RetreatEnrollmentRequestDto request
    ) {
        List<RetreatEnrollmentResponseDto> result = retreatService.enrollDisciples(id, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseBodyDto<>("Discípulo(s) inscrito(s) exitosamente", result));
    }

    @PostMapping("/{id}/enrollments/disciples/remove")
    @Override
    public ResponseEntity<ApiResponseBodyDto<Void>> removeEnrolledDisciples(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid RemoveEnrolledDisciplesRequestDto request
    ) {
        retreatService.removeEnrolledDisciples(id, request);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Discípulo(s) removido(s) exitosamente", null));
    }

    @PatchMapping("/{id}/enrollments/disciples/{discipleId}/payment")
    @Override
    public ResponseEntity<ApiResponseBodyDto<RetreatEnrollmentResponseDto>> registerPayment(
            @PathVariable("id") @Min(1) Long id,
            @PathVariable("discipleId") @Min(1) Long discipleId,
            @RequestBody @Valid RegisterPaymentRequestDto request
    ) {
        RetreatEnrollmentResponseDto result = retreatService.registerPayment(id, discipleId, request);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Pago registrado exitosamente", result));
    }

    @GetMapping("/{id}/enrollments/disciples")
    @Override
    public ResponseEntity<ApiResponseBodyDto<RetreatWithEnrollmentsResponseDto>> findRetreatWithEnrolledDisciples(
            @PathVariable("id") @Min(1) Long id
    ) {
        RetreatWithEnrollmentsResponseDto result = retreatService.findRetreatWithEnrolledDisciples(id);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Encuentro y asistentes obtenidos exitosamente", result));
    }

    @GetMapping("/{id}/staffs")
    @Override
    public ResponseEntity<ApiResponseBodyDto<List<RetreatStaffResponseDto>>> findStaff(@PathVariable("id") @Min(1) Long id) {
        List<RetreatStaffResponseDto> result = retreatService.findStaff(id);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Staff obtenido exitosamente", result));
    }

    @PostMapping("/{id}/staffs")
    @Override
    public ResponseEntity<ApiResponseBodyDto<List<RetreatStaffResponseDto>>> addStaff(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid RetreatStaffRequestDto request
    ) {
        List<RetreatStaffResponseDto> result = retreatService.addStaff(id, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseBodyDto<>("Discípulo(s) añadido(s) como staff exitosamente", result));
    }

    @PostMapping("/{id}/staffs/remove")
    @Override
    public ResponseEntity<ApiResponseBodyDto<Void>> removeStaff(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid RemoveStaffRequestDto request
    ) {
        retreatService.removeStaff(id, request);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Discípulo(s) removido(s) del staff exitosamente", null));
    }

    @GetMapping("/{id}/export/excel")
    @Override
    public ResponseEntity<byte[]> exportFullReport(@PathVariable("id") @Min(1) Long id) throws IOException {
        return excelResponseFactory.build(retreatService.exportFullReport(id), "Reporte_Encuentro_" + id + ".xlsx");
    }

    @GetMapping("/{id}/export/excel/attendees")
    @Override
    public ResponseEntity<byte[]> exportAttendees(@PathVariable("id") @Min(1) Long id) throws IOException {
        return excelResponseFactory.build(retreatService.exportAttendees(id), "Asistentes_Encuentro_" + id + ".xlsx");
    }

    @GetMapping("/{id}/export/excel/attendees-payments")
    @Override
    public ResponseEntity<byte[]> exportAttendeesWithPayments(@PathVariable("id") @Min(1) Long id) throws IOException {
        return excelResponseFactory.build(retreatService.exportAttendeesWithPayments(id), "Asistentes_Pagos_Encuentro_" + id + ".xlsx");
    }

    @GetMapping("/{id}/export/excel/staffs")
    @Override
    public ResponseEntity<byte[]> exportStaff(@PathVariable("id") @Min(1) Long id) throws IOException {
        return excelResponseFactory.build(retreatService.exportStaff(id), "Staff_Encuentro_" + id + ".xlsx");
    }
}
