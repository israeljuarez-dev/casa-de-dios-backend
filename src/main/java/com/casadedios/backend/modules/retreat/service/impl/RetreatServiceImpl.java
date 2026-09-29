package com.casadedios.backend.modules.retreat.service.impl;

import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.common.exception.model.CasaDeDiosException;
import com.casadedios.backend.common.util.StringUtils;
import com.casadedios.backend.modules.disciple.exception.DiscipleErrorEnum;
import com.casadedios.backend.modules.disciple.persistence.model.Disciple;
import com.casadedios.backend.modules.disciple.persistence.repository.DiscipleRepository;
import com.casadedios.backend.modules.retreat.dto.request.*;
import com.casadedios.backend.modules.retreat.dto.response.RetreatEnrollmentResponseDto;
import com.casadedios.backend.modules.retreat.dto.response.RetreatResponseDto;
import com.casadedios.backend.modules.retreat.dto.response.RetreatStaffResponseDto;
import com.casadedios.backend.modules.retreat.enums.PaymentStatusEnum;
import com.casadedios.backend.modules.retreat.exception.RetreatErrorEnum;
import com.casadedios.backend.modules.retreat.mapper.RetreatMapper;
import com.casadedios.backend.modules.retreat.persistence.model.Retreat;
import com.casadedios.backend.modules.retreat.persistence.model.RetreatEnrollment;
import com.casadedios.backend.modules.retreat.persistence.model.RetreatStaff;
import com.casadedios.backend.modules.retreat.persistence.projection.RetreatEnrollmentProjection;
import com.casadedios.backend.modules.retreat.persistence.projection.RetreatStaffProjection;
import com.casadedios.backend.modules.retreat.persistence.projection.RetreatSummaryProjection;
import com.casadedios.backend.modules.retreat.persistence.repository.RetreatEnrollmentRepository;
import com.casadedios.backend.modules.retreat.persistence.repository.RetreatRepository;
import com.casadedios.backend.modules.retreat.persistence.repository.RetreatStaffRepository;
import com.casadedios.backend.modules.retreat.service.RetreatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RetreatServiceImpl implements RetreatService {

    private final RetreatRepository retreatRepository;

    private final RetreatEnrollmentRepository retreatEnrollmentRepository;

    private final RetreatStaffRepository retreatStaffRepository;

    private final RetreatMapper retreatMapper;

    private final DiscipleRepository discipleRepository;

    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<RetreatResponseDto> findAll(RetreatSearchCriteriaDto criteria) {
        Pageable pageable = criteria.pagination().toPageable();

        Page<RetreatSummaryProjection> page = retreatRepository.findAllWithCounts(
                StringUtils.blankToNull(criteria.name()),
                StringUtils.blankToNull(criteria.location()),
                criteria.startDateFrom(),
                criteria.startDateTo(),
                pageable
        );

        List<RetreatResponseDto> content = page.getContent().stream()
                .map(this::toResponseDtoFromProjection)
                .toList();

        return PaginationResponseDto.of(content, page);
    }

    @Override
    public RetreatResponseDto findById(Long id) {
        RetreatSummaryProjection projection = retreatRepository.findByIdWithCounts(id)
                .orElseThrow(() -> {
                    log.debug("No existe encuentro con el id {}", id);
                    return new CasaDeDiosException(RetreatErrorEnum.RETREAT_NOT_FOUND);
                });

        return toResponseDtoFromProjection(projection);
    }

    @Override
    @Transactional
    public RetreatResponseDto create(RetreatRegisterRequestDto request) {
        Retreat retreat = retreatMapper.toEntity(request);
        Retreat saved = retreatRepository.save(retreat);

        log.info("Encuentro '{}' registrado exitosamente con id {}", saved.getName(), saved.getId());

        return toResponseDto(saved, 0, 0);
    }

    @Override
    @Transactional
    public RetreatResponseDto update(Long id, RetreatUpdateRequestDto request) {
        Retreat retreat = getRetreatOrThrow(id);

        if (request.startDate() != null || request.endDate() != null) {
            Instant finalStartDate = request.startDate() != null ? request.startDate() : retreat.getStartDate();
            Instant finalEndDate = request.endDate() != null ? request.endDate() : retreat.getEndDate();

            if (!finalEndDate.isAfter(finalStartDate)) {
                throw new CasaDeDiosException(RetreatErrorEnum.RETREAT_INVALID_DATE_RANGE);
            }

            validateDatesAreFuture(finalStartDate, finalEndDate);
        }

        retreatMapper.updateEntityFromDto(request, retreat);
        Retreat updated = retreatRepository.save(retreat);

        log.info("Encuentro con id {} actualizado exitosamente", updated.getId());

        int enrolledCount = (int) retreatEnrollmentRepository.countByRetreat_Id(id);
        int staffCount = (int) retreatStaffRepository.countByRetreat_Id(id);

        return toResponseDto(updated, enrolledCount, staffCount);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!retreatRepository.existsById(id)) {
            log.debug("No existe encuentro con id {} para eliminar", id);
            throw new CasaDeDiosException(RetreatErrorEnum.RETREAT_NOT_FOUND);
        }

        retreatRepository.deleteById(id);

        log.warn("Encuentro con id {} eliminado", id);
    }

    @Override
    public List<RetreatEnrollmentResponseDto> findEnrolledDisciples(Long retreatId, RetreatEnrollmentSearchCriteriaDto criteria) {
        Retreat retreat = getRetreatOrThrow(retreatId);

        String paymentStatusParam = criteria.paymentStatus() != null
                ? criteria.paymentStatus().name()
                : null;

        return retreatEnrollmentRepository.findEnrolledDisciples(
                        retreatId,
                        StringUtils.blankToNull(criteria.search()),
                        paymentStatusParam
                ).stream()
                .map(projection -> toEnrollmentResponseDto(projection, retreat.getPrice()))
                .toList();
    }

    @Override
    @Transactional
    public List<RetreatEnrollmentResponseDto> enrollDisciples(Long retreatId, RetreatEnrollmentRequestDto request) {
        Retreat retreat = getRetreatOrThrow(retreatId);

        List<RetreatEnrollment> enrollments = request.discipleIds().stream()
                .map(discipleId -> buildEnrollment(retreat, discipleId))
                .toList();

        List<RetreatEnrollment> saved = retreatEnrollmentRepository.saveAll(enrollments);

        log.info("{} discípulo(s) inscrito(s) en el encuentro {}", saved.size(), retreatId);

        return saved.stream()
                .map(this::toEnrollmentResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void removeEnrolledDisciples(Long retreatId, RemoveEnrolledDisciplesRequestDto request) {
        validateRetreatExists(retreatId);

        retreatEnrollmentRepository.deleteByRetreat_IdAndDisciple_IdIn(retreatId, request.discipleIds());

        log.info("Discípulo(s) {} removido(s) del encuentro {}", request.discipleIds(), retreatId);
    }

    @Override
    @Transactional
    public RetreatEnrollmentResponseDto registerPayment(Long retreatId, Long discipleId, RegisterPaymentRequestDto request) {
        RetreatEnrollment enrollment = getEnrollmentOrThrow(retreatId, discipleId);

        BigDecimal newAmountPaid = enrollment.getAmountPaid().add(request.amount());
        BigDecimal price = enrollment.getRetreat().getPrice();

        if (newAmountPaid.compareTo(price) > 0) {
            throw new CasaDeDiosException(RetreatErrorEnum.RETREAT_PAYMENT_EXCEEDS_PRICE);
        }

        enrollment.setAmountPaid(newAmountPaid);
        enrollment.setPaymentStatus(resolvePaymentStatus(newAmountPaid, price));

        RetreatEnrollment saved = retreatEnrollmentRepository.save(enrollment);

        log.info("Pago de {} registrado para discípulo {} en encuentro {}", request.amount(), discipleId, retreatId);

        return toEnrollmentResponseDto(saved);
    }
    @Override
    @Transactional(readOnly = true)
    public RetreatWithEnrollmentsResponseDto findRetreatWithEnrolledDisciples(Long retreatId) {
        RetreatResponseDto retreat = findById(retreatId);

        List<RetreatEnrollmentResponseDto> enrollments = findEnrolledDisciples(
                retreatId,
                RetreatEnrollmentSearchCriteriaDto.builder().build()
        );

        return RetreatWithEnrollmentsResponseDto.builder()
                .retreat(retreat)
                .enrollments(enrollments)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RetreatStaffResponseDto> findStaff(Long retreatId) {
        validateRetreatExists(retreatId);

        return retreatStaffRepository.findStaffByRetreatId(retreatId).stream()
                .map(this::toStaffResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public List<RetreatStaffResponseDto> addStaff(Long retreatId, RetreatStaffRequestDto request) {
        Retreat retreat = getRetreatOrThrow(retreatId);

        List<RetreatStaff> staffList = request.discipleIds().stream()
                .map(discipleId -> buildStaff(retreat, discipleId))
                .toList();

        List<RetreatStaff> saved = retreatStaffRepository.saveAll(staffList);

        log.info("{} discípulo(s) añadido(s) como staff del encuentro {}", saved.size(), retreatId);

        return saved.stream()
                .map(this::toStaffResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void removeStaff(Long retreatId, RemoveStaffRequestDto request) {
        validateRetreatExists(retreatId);

        retreatStaffRepository.deleteByRetreat_IdAndDisciple_IdIn(retreatId, request.discipleIds());

        log.info("Discípulo(s) {} removido(s) del staff del encuentro {}", request.discipleIds(), retreatId);
    }

    @Override
    public ByteArrayOutputStream exportFullReport(Long retreatId) throws IOException {
        return null;
    }

    @Override
    public ByteArrayOutputStream exportAttendees(Long retreatId) throws IOException {
        return null;
    }

    @Override
    public ByteArrayOutputStream exportAttendeesWithPayments(Long retreatId) throws IOException {
        return null;
    }

    @Override
    public ByteArrayOutputStream exportStaff(Long retreatId) throws IOException {
        return null;
    }

    /* ============================ PRIVADOS  ============================  */
    private Retreat getRetreatOrThrow(Long id) {
        return retreatRepository.findById(id)
                .orElseThrow(() -> {
                    log.debug("No existe encuentro con id {}", id);
                    return new CasaDeDiosException(RetreatErrorEnum.RETREAT_NOT_FOUND);
                });
    }

    private void validateRetreatExists(Long id) {
        if (!retreatRepository.existsById(id)) {
            throw new CasaDeDiosException(RetreatErrorEnum.RETREAT_NOT_FOUND);
        }
    }

    private Disciple getDiscipleOrThrow(Long id) {
        return discipleRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.debug("No existe discípulo activo con id {}", id);
                    return new CasaDeDiosException(DiscipleErrorEnum.DISCIPLE_NOT_FOUND);
                });
    }

    private void validateDatesAreFuture(Instant startDate, Instant endDate) {
        Instant now = Instant.now(clock);
        if (!startDate.isAfter(now) || !endDate.isAfter(now)) {
            log.warn("Intento de registrar/editar un encuentro con fechas no futuras");
            throw new CasaDeDiosException(RetreatErrorEnum.RETREAT_DATES_MUST_BE_FUTURE);
        }
    }

    private RetreatEnrollment buildEnrollment(Retreat retreat, Long discipleId) {
        Disciple disciple = getDiscipleOrThrow(discipleId);

        if (retreatEnrollmentRepository.existsByRetreat_IdAndDisciple_Id(retreat.getId(), discipleId)) {
            log.warn("Discípulo {} ya está inscrito en el encuentro {}", discipleId, retreat.getId());
            throw new CasaDeDiosException(RetreatErrorEnum.RETREAT_ENROLLMENT_ALREADY_EXISTS);
        }

        return RetreatEnrollment.builder()
                .retreat(retreat)
                .disciple(disciple)
                .build();
    }

    private PaymentStatusEnum resolvePaymentStatus(BigDecimal amountPaid, BigDecimal price) {
        if (amountPaid.compareTo(price) >= 0) return PaymentStatusEnum.PAID;
        if (amountPaid.compareTo(BigDecimal.ZERO) > 0) return PaymentStatusEnum.PARTIAL;
        return PaymentStatusEnum.PENDING;
    }

    private RetreatStaff buildStaff(Retreat retreat, Long discipleId) {
        Disciple disciple = getDiscipleOrThrow(discipleId);

        if (retreatStaffRepository.existsByRetreat_IdAndDisciple_Id(retreat.getId(), discipleId)) {
            log.warn("Discípulo {} ya es staff del encuentro {}", discipleId, retreat.getId());
            throw new CasaDeDiosException(RetreatErrorEnum.RETREAT_STAFF_ALREADY_EXISTS);
        }

        return RetreatStaff.builder()
                .retreat(retreat)
                .disciple(disciple)
                .build();
    }

    private RetreatEnrollment getEnrollmentOrThrow(Long retreatId, Long discipleId) {
        validateRetreatExists(retreatId);
        return retreatEnrollmentRepository.findByRetreat_IdAndDisciple_Id(retreatId, discipleId)
                .orElseThrow(() -> {
                    log.debug("Discípulo {} no está inscrito en el encuentro {}", discipleId, retreatId);
                    return new CasaDeDiosException(RetreatErrorEnum.RETREAT_ENROLLMENT_NOT_FOUND);
                });
    }

    private RetreatResponseDto toResponseDto(Retreat entity, int enrolledCount, int staffCount) {
        return RetreatResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .location(entity.getLocation())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .enrolledCount(enrolledCount)
                .staffCount(staffCount)
                .build();
    }

    private RetreatResponseDto toResponseDtoFromProjection(RetreatSummaryProjection projection) {
        return RetreatResponseDto.builder()
                .id(projection.getId())
                .name(projection.getName())
                .location(projection.getLocation())
                .startDate(projection.getStartDate())
                .endDate(projection.getEndDate())
                .enrolledCount(projection.getEnrolledCount())
                .staffCount(projection.getStaffCount())
                .build();
    }

    private RetreatEnrollmentResponseDto toEnrollmentResponseDto(RetreatEnrollment entity) {
        BigDecimal amountPending = entity.getRetreat().getPrice().subtract(entity.getAmountPaid());

        return RetreatEnrollmentResponseDto.builder()
                .enrollmentId(entity.getId())
                .discipleId(entity.getDisciple().getId())
                .firstName(entity.getDisciple().getFirstName())
                .lastName(entity.getDisciple().getLastName())
                .phoneCodeNumber(entity.getDisciple().getPhoneCodeNumber())
                .phoneNumber(entity.getDisciple().getPhoneNumber())
                .paymentStatus(entity.getPaymentStatus())
                .amountPaid(entity.getAmountPaid())
                .amountPending(amountPending)
                .build();
    }

    private RetreatEnrollmentResponseDto toEnrollmentResponseDto(RetreatEnrollmentProjection projection, BigDecimal price) {
        BigDecimal amountPending = price.subtract(projection.getAmountPaid());

        return RetreatEnrollmentResponseDto.builder()
                .enrollmentId(projection.getEnrollmentId())
                .discipleId(projection.getDiscipleId())
                .firstName(projection.getFirstName())
                .lastName(projection.getLastName())
                .phoneCodeNumber(projection.getPhoneCodeNumber())
                .phoneNumber(projection.getPhoneNumber())
                .paymentStatus(com.casadedios.backend.modules.retreat.enums.PaymentStatusEnum.valueOf(projection.getPaymentStatus()))
                .amountPaid(projection.getAmountPaid())
                .amountPending(amountPending)
                .build();
    }

    private RetreatStaffResponseDto toStaffResponseDto(RetreatStaff entity) {
        return RetreatStaffResponseDto.builder()
                .id(entity.getId())
                .discipleId(entity.getDisciple().getId())
                .firstName(entity.getDisciple().getFirstName())
                .lastName(entity.getDisciple().getLastName())
                .phoneCodeNumber(entity.getDisciple().getPhoneCodeNumber())
                .phoneNumber(entity.getDisciple().getPhoneNumber())
                .build();
    }

    private RetreatStaffResponseDto toStaffResponseDto(RetreatStaffProjection projection) {
        return RetreatStaffResponseDto.builder()
                .id(projection.getId())
                .discipleId(projection.getDiscipleId())
                .firstName(projection.getFirstName())
                .lastName(projection.getLastName())
                .phoneCodeNumber(projection.getPhoneCodeNumber())
                .phoneNumber(projection.getPhoneNumber())
                .build();
    }
}

