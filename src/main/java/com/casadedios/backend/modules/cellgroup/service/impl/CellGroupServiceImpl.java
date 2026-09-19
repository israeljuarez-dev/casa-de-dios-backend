package com.casadedios.backend.modules.cellgroup.service.impl;

import com.casadedios.backend.cellgroup.dto.request.*;
import com.casadedios.backend.modules.cellgroup.dto.request.*;
import com.casadedios.backend.modules.cellgroup.dto.response.CellGroupLeaderResponseDto;
import com.casadedios.backend.modules.cellgroup.dto.response.CellGroupMemberResponseDto;
import com.casadedios.backend.modules.cellgroup.dto.response.CellGroupResponseDto;
import com.casadedios.backend.modules.cellgroup.enums.MeetingDay;
import com.casadedios.backend.modules.cellgroup.export.CellGroupExcelExporter;
import com.casadedios.backend.modules.cellgroup.mapper.CellGroupMapper;
import com.casadedios.backend.modules.cellgroup.persistence.model.CellGroup;
import com.casadedios.backend.modules.cellgroup.persistence.model.CellGroupMember;
import com.casadedios.backend.modules.cellgroup.persistence.projection.CellGroupMemberProjection;
import com.casadedios.backend.modules.cellgroup.persistence.projection.CellGroupSummaryProjection;
import com.casadedios.backend.modules.cellgroup.persistence.repository.CellGroupMemberRepository;
import com.casadedios.backend.modules.cellgroup.persistence.repository.CellGroupRepository;
import com.casadedios.backend.modules.cellgroup.service.CellGroupService;
import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.common.enums.GenderEnum;
import com.casadedios.backend.common.exception.enums.ApiError;
import com.casadedios.backend.common.exception.model.CasaDeDiosException;
import com.casadedios.backend.modules.disciple.enums.SpiritualLevel;
import com.casadedios.backend.modules.disciple.persistence.model.Disciple;
import com.casadedios.backend.modules.disciple.persistence.repository.DiscipleRepository;
import com.casadedios.backend.modules.disciple.util.DiscipleDateCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CellGroupServiceImpl implements CellGroupService {

    private static final int PASTOR_CORE_TWELVE_LIMIT = 12;

    private final CellGroupRepository cellGroupRepository;

    private final CellGroupMemberRepository cellGroupMemberRepository;

    private final DiscipleRepository discipleRepository;

    private final CellGroupMapper cellGroupMapper;

    private final DiscipleDateCalculator discipleDateCalculator;

    private final Clock clock;

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<CellGroupResponseDto> findAll(CellGroupSearchCriteriaDto criteria) {
        Pageable pageable = criteria.pagination().toPageable();

        String meetingDayParam = criteria.meetingDay() != null
                ? criteria.meetingDay().name()
                : null;

        Page<CellGroupSummaryProjection> page = cellGroupRepository.findAllWithLeaderAndCount(
                blankToNull(criteria.name()),
                blankToNull(criteria.leaderName()),
                meetingDayParam,
                criteria.isPastorCell(),
                pageable
        );

        List<CellGroupResponseDto> content = page.getContent().stream()
                .map(this::toResponseDtoFromProjection)
                .toList();

        return PaginationResponseDto.of(content, page);
    }

    @Override
    @Transactional(readOnly = true)
    public CellGroupResponseDto findById(Long id) {
        CellGroup cellGroup = getCellGroupOrThrow(id);
        long memberCount = cellGroupMemberRepository.countByCellGroup_Id(id);
        return toResponseDtoFromEntity(cellGroup, (int) memberCount);
    }

    @Override
    @Transactional
    public CellGroupResponseDto create(CellGroupRegisterRequestDto request) {
        validateUniqueName(request.name());

        if (Boolean.TRUE.equals(request.isPastorCell())) {
            validateOnlyTwoPastorCellsPermitted();

            if (request.pastorCellGender() == null) {
                throw new CasaDeDiosException(ApiError.PASTOR_CELL_GENDER_REQUIRED);
            }

            validateNoDuplicatePastorCellForGender(request.pastorCellGender());

            CellGroup cellGroup = cellGroupMapper.toEntity(request);
            cellGroup.setLeader(null);

            CellGroup saved = cellGroupRepository.save(cellGroup);
            return toResponseDtoFromEntity(saved, 0);
        }

        Disciple leader = getDiscipleOrThrow(request.leaderDiscipleId());
        validateLeaderSpiritualLevel(leader);
        promoteToLeaderIfNeeded(leader);

        CellGroup cellGroup = cellGroupMapper.toEntity(request);
        cellGroup.setLeader(leader);

        CellGroup saved = cellGroupRepository.save(cellGroup);

        log.info("Célula '{}' registrada exitosamente con id {}", saved.getName(), saved.getId());

        return toResponseDtoFromEntity(saved, 0);
    }

    @Override
    @Transactional
    public CellGroupResponseDto update(Long id, CellGroupUpdateRequestDto request) {
        CellGroup cellGroup = getCellGroupOrThrow(id);

        validateUniqueNameOnUpdate(request.name(), id);

        cellGroupMapper.updateEntityFromDto(request, cellGroup);

        if (request.leaderDiscipleId() != null) {
            Disciple newLeader = getDiscipleOrThrow(request.leaderDiscipleId());
            validateLeaderSpiritualLevel(newLeader);
            promoteToLeaderIfNeeded(newLeader);
            cellGroup.setLeader(newLeader);
        }

        CellGroup updated = cellGroupRepository.save(cellGroup);

        log.info("Célula con nombre '{}' actualizada exitosamente", updated.getName());

        long memberCount = cellGroupMemberRepository.countByCellGroup_Id(id);

        return toResponseDtoFromEntity(updated, (int) memberCount);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!cellGroupRepository.existsById(id)) {
            log.debug("No existe célula con id {} para eliminar", id);
            throw new CasaDeDiosException(ApiError.CELL_GROUP_NOT_FOUND);
        }

        cellGroupRepository.deleteById(id);

        log.warn("Célula con id {} eliminada", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CellGroupMemberResponseDto> findMembers(Long cellGroupId, CellGroupMemberSearchCriteriaDto criteria) {
        validateCellGroupExists(cellGroupId);

        String spiritualLevelParam = criteria.spiritualLevel() != null
                ? criteria.spiritualLevel().name()
                : null;
        String genderParam = criteria.gender() != null
                ? criteria.gender().name()
                : null;

        return cellGroupMemberRepository.findMembersByCellGroupId(
                        cellGroupId,
                        blankToNull(criteria.search()),
                        spiritualLevelParam,
                        genderParam
                ).stream()
                .map(this::toMemberResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public CellGroupMemberResponseDto addMember(Long cellGroupId, CellGroupMemberRequestDto request) {
        CellGroup cellGroup = getCellGroupOrThrow(cellGroupId);
        Disciple disciple = getDiscipleOrThrow(request.discipleId());

        validateLeaderIsNotAddingHimselfAsMember(cellGroup, disciple);
        validateDiscipleIsNotAlreadyMemberOfAnotherCell(request.discipleId());

        if (cellGroup.isPastorCell()) {
            validateMemberEligibilityForPastorCell(cellGroup, disciple);
        }

        if (cellGroupMemberRepository.existsByCellGroup_IdAndDisciple_Id(cellGroupId, request.discipleId())) {
            throw new CasaDeDiosException(ApiError.CELL_GROUP_MEMBER_ALREADY_EXISTS);
        }

        CellGroupMember member = CellGroupMember.builder()
                .cellGroup(cellGroup)
                .disciple(disciple)
                .joinedAt(LocalDate.now(clock))
                .build();

        CellGroupMember saved = cellGroupMemberRepository.save(member);

        if (cellGroup.isPastorCell()) {
            saved.setPastorCoreTwelve(true);
            saved.setCoreTwelve(true);
            saved = cellGroupMemberRepository.save(saved);
            log.info("Discípulo {} marcado automáticamente como Los 12 del pastor", disciple.getId());
        }

        log.info("Discípulo {} añadido a célula {}", disciple.getId(), cellGroupId);

        return cellGroupMapper.toMemberResponseDto(saved);
    }

    @Override
    @Transactional
    public void removeMember(Long cellGroupId, Long discipleId) {
        CellGroupMember member = getMemberOrThrow(cellGroupId, discipleId);
        cellGroupMemberRepository.delete(member);

        log.info("Discípulo {} removido de célula {}", discipleId, cellGroupId);
    }

    @Override
    @Transactional
    public CellGroupMemberResponseDto markAsCoreTwelve(Long cellGroupId, Long discipleId) {
        CellGroupMember member = getMemberOrThrow(cellGroupId, discipleId);
        member.setCoreTwelve(true);
        CellGroupMember saved = cellGroupMemberRepository.save(member);

        log.info("Discípulo {} marcado como Los 12 en célula {}", discipleId, cellGroupId);

        return cellGroupMapper.toMemberResponseDto(saved);
    }

    @Override
    @Transactional
    public void unmarkAsCoreTwelve(Long cellGroupId, Long discipleId) {
        CellGroupMember member = getMemberOrThrow(cellGroupId, discipleId);
        member.setCoreTwelve(false);
        // Si era Los 12 del pastor también, se quita ambos flags
        member.setPastorCoreTwelve(false);
        cellGroupMemberRepository.save(member);

        log.info("Discípulo {} removido de Los 12 en célula {}", discipleId, cellGroupId);
    }

    @Override
    @Transactional
    public CellGroupMemberResponseDto markAsPastorCoreTwelve(Long cellGroupId, Long discipleId) {
        CellGroupMember member = getMemberOrThrow(cellGroupId, discipleId);

        // Solo líderes de célula pueden ser parte de Los 12 del pastor
        if (!member.getDisciple().isCellGroupLeader()) {
            throw new CasaDeDiosException(ApiError.PASTOR_CORE_TWELVE_MEMBER_MUST_BE_LEADER);
        }

        // Validar límite de 12 en toda la iglesia
        long currentCount = cellGroupMemberRepository
                .countByCellGroup_IdAndIsPastorCoreTwelveTrue(cellGroupId);

        if (!member.isPastorCoreTwelve() && currentCount >= PASTOR_CORE_TWELVE_LIMIT) {
            throw new CasaDeDiosException(ApiError.PASTOR_CORE_TWELVE_LIMIT_EXCEEDED);
        }

        member.setCoreTwelve(true);          // Los 12 del pastor implica también ser Los 12 del líder
        member.setPastorCoreTwelve(true);
        CellGroupMember saved = cellGroupMemberRepository.save(member);

        log.info("Discípulo {} marcado como Los 12 del pastor en célula {}", discipleId, cellGroupId);

        return cellGroupMapper.toMemberResponseDto(saved);
    }

    @Override
    @Transactional
    public void unmarkAsPastorCoreTwelve(Long cellGroupId, Long discipleId) {
        CellGroupMember member = getMemberOrThrow(cellGroupId, discipleId);
        member.setPastorCoreTwelve(false);
        cellGroupMemberRepository.save(member);

        log.info("Discípulo {} removido de Los 12 del pastor en célula {}", discipleId, cellGroupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CellGroupMemberResponseDto> findPastorCoreTwelve() {
        return cellGroupMemberRepository.findPastorCoreTwelve().stream()
                .map(this::toMemberResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ByteArrayOutputStream exportToExcel(CellGroupSearchCriteriaDto criteria) throws IOException {
        String meetingDayParam = criteria.meetingDay() != null
                ? criteria.meetingDay().name()
                : null;

        List<CellGroupSummaryProjection> projections = cellGroupRepository.findAllWithLeaderAndCount(
                blankToNull(criteria.name()),
                blankToNull(criteria.leaderName()),
                meetingDayParam,
                criteria.isPastorCell(),
                Pageable.unpaged()
        ).getContent();

        List<CellGroupResponseDto> cellGroupsDto = projections.stream()
                .map(this::toResponseDtoFromProjection)
                .toList();

        CellGroupExcelExporter exporter = new CellGroupExcelExporter(cellGroupsDto);
        ByteArrayOutputStream outputStream = exporter.export();

        log.info("Reporte de células exportado exitosamente con {} registros", cellGroupsDto.size());

        return outputStream;
    }

    @Override
    public String generateExcelFileName() {
        return "Reporte_Celulas_"
                + LocalDate.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                + ".xlsx";
    }

    // --- Métodos privados ---

    private CellGroup getCellGroupOrThrow(Long id) {
        return cellGroupRepository.findById(id)
                .orElseThrow(() -> {
                    log.debug("No existe célula con id {}", id);
                    return new CasaDeDiosException(ApiError.CELL_GROUP_NOT_FOUND);
                });
    }

    private Disciple getDiscipleOrThrow(Long id) {
        return discipleRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.debug("No existe discípulo activo con id {}", id);
                    return new CasaDeDiosException(ApiError.DISCIPLE_NOT_FOUND);
                });
    }

    private void validateLeaderSpiritualLevel(Disciple disciple) {
        if (disciple.getSpiritualLevel() != SpiritualLevel.LEADER) {
            log.warn("Discípulo {} no tiene nivel LEADER, no puede liderar una célula", disciple.getId());
            throw new CasaDeDiosException(ApiError.DISCIPLE_NOT_A_LEADER);
        }
    }

    private void promoteToLeaderIfNeeded(Disciple disciple) {
        if (!disciple.isCellGroupLeader()) {
            disciple.setCellGroupLeader(true);
            discipleRepository.save(disciple);
            log.info("Discípulo {} promovido a líder de célula automáticamente", disciple.getId());
        }
    }

    private void validateNoDuplicatePastorCellForGender(GenderEnum gender) {
        if (cellGroupRepository.existsByIsPastorCellTrueAndPastorCellGender(gender)) {
            log.warn("Intento de crear una segunda célula principal para el género {}", gender);
            throw new CasaDeDiosException(ApiError.PASTOR_CELL_GENDER_ALREADY_EXISTS);
        }
    }

    private void validateCellGroupExists(Long cellGroupId) {
        if (!cellGroupRepository.existsById(cellGroupId)) {
            throw new CasaDeDiosException(ApiError.CELL_GROUP_NOT_FOUND);
        }
    }

    private void validateUniqueName(String name) {
        if (name != null && !name.isBlank() && cellGroupRepository.existsByName(name)) {
            log.warn("Intento de registrar una célula con un nombre ya existente.");
            throw new CasaDeDiosException(ApiError.DUPLICATE_CELL_GROUP_NAME);
        }
    }

    private void validateUniqueNameOnUpdate(String name, Long currentId) {
        if (name != null && !name.isBlank() && cellGroupRepository.existsByNameAndIdNot(name, currentId)) {
            log.warn("Intento de actualizar la célula {} con un nombre ya usado por otra célula.", currentId);
            throw new CasaDeDiosException(ApiError.DUPLICATE_CELL_GROUP_NAME);
        }
    }

    private void validateLeaderIsNotAddingHimselfAsMember(CellGroup cellGroup, Disciple disciple) {
        if (cellGroup.getLeader() != null && cellGroup.getLeader().getId().equals(disciple.getId())) {
            log.warn("Discípulo {} intenta añadirse a su propia célula {} como miembro", disciple.getId(), cellGroup.getId());
            throw new CasaDeDiosException(ApiError.LEADER_CANNOT_BE_OWN_CELL_MEMBER);
        }
    }

    private void validateDiscipleIsNotAlreadyMemberOfAnotherCell(Long discipleId) {
        if (cellGroupMemberRepository.existsByDisciple_Id(discipleId)) {
            log.warn("Discípulo {} ya pertenece a otra célula", discipleId);
            throw new CasaDeDiosException(ApiError.DISCIPLE_ALREADY_IN_ANOTHER_CELL);
        }
    }

    private void validateOnlyTwoPastorCellsPermitted() {
        if (cellGroupRepository.countByIsPastorCellTrue() >= 2) {
            log.warn("Intento de crear una tercera célula principal del pastor");
            throw new CasaDeDiosException(ApiError.PASTOR_CELL_LIMIT_EXCEEDED);
        }
    }

    private void validateMemberEligibilityForPastorCell(CellGroup cellGroup, Disciple disciple) {
        // Solo los discípulos leader pueden formar parte de la célula principal
        if (disciple.getSpiritualLevel() != SpiritualLevel.LEADER) {
            throw new CasaDeDiosException(ApiError.PASTOR_CELL_MEMBER_MUST_BE_LEADER);
        }

        // Solo los discípulos del género del pastor se pueden asignar a su célula prícinpal
        GenderEnum cellGender = cellGroup.getPastorCellGender();
        if (disciple.getGender() != cellGender) {
            throw new CasaDeDiosException(ApiError.PASTOR_CELL_GENDER_MISMATCH);
        }

        // Verifica que sean máximo 12 miembros
        long currentCount = cellGroupMemberRepository
                .countByCellGroup_IdAndDisciple_Gender(cellGroup.getId(), cellGender);

        if (currentCount >= 12) {
            throw new CasaDeDiosException(ApiError.PASTOR_CELL_MEMBER_LIMIT_EXCEEDED);
        }
    }

    private CellGroupMember getMemberOrThrow(Long cellGroupId, Long discipleId) {
        validateCellGroupExists(cellGroupId);
        return cellGroupMemberRepository.findByCellGroup_IdAndDisciple_Id(cellGroupId, discipleId)
                .orElseThrow(() -> {
                    log.debug("Discípulo {} no es miembro de célula {}", discipleId, cellGroupId);
                    return new CasaDeDiosException(ApiError.CELL_GROUP_MEMBER_NOT_FOUND);
                });
    }

    private CellGroupResponseDto toResponseDtoFromEntity(CellGroup entity, int memberCount) {
        CellGroupLeaderResponseDto leader = entity.getLeader() != null
                ? CellGroupLeaderResponseDto.builder()
                .id(entity.getLeader().getId())
                .firstName(entity.getLeader().getFirstName())
                .lastName(entity.getLeader().getLastName())
                .build()
                : null;

        MeetingDay meetingDay = entity.getMeetingDay();

        return CellGroupResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .leader(leader)
                .meetingDay(meetingDay)
                .meetingDaySpanishName(meetingDay != null ? meetingDay.getDaySpanishName() : null)
                .meetingTime(entity.getMeetingTime())
                .location(entity.getLocation())
                .isPastorCell(entity.isPastorCell())
                .pastorCellGender(entity.getPastorCellGender())
                .memberCount(memberCount)
                .build();
    }

    private CellGroupMemberResponseDto toMemberResponseDto(CellGroupMemberProjection projection) {
        LocalDate birthDate = projection.getBirthDate();

        return CellGroupMemberResponseDto.builder()
                .memberId(projection.getMemberId())
                .discipleId(projection.getDiscipleId())
                .firstName(projection.getFirstName())
                .lastName(projection.getLastName())
                .phoneCodeNumber(projection.getPhoneCodeNumber())
                .phoneNumber(projection.getPhoneNumber())
                .spiritualLevel(SpiritualLevel.valueOf(projection.getSpiritualLevel()))
                .birthDate(birthDate)
                .age(discipleDateCalculator.calculateAge(birthDate))
                .gender(GenderEnum.valueOf(projection.getGender()))
                .isCellGroupLeader(projection.getIsCellGroupLeader())
                .isCoreTwelve(projection.getIsCoreTwelve())
                .isPastorCoreTwelve(projection.getIsPastorCoreTwelve())
                .birthdayAlert(discipleDateCalculator.calculateBirthdayAlert(birthDate))
                .build();
    }

    private CellGroupResponseDto toResponseDtoFromProjection(CellGroupSummaryProjection projection) {
        MeetingDay meetingDay = projection.getMeetingDay() != null
                ? MeetingDay.valueOf(projection.getMeetingDay())
                : null;

        CellGroupLeaderResponseDto leader = projection.getLeaderId() != null
                ? CellGroupLeaderResponseDto.builder()
                .id(projection.getLeaderId())
                .firstName(projection.getLeaderFirstName())
                .lastName(projection.getLeaderLastName())
                .build()
                : null;

        return CellGroupResponseDto.builder()
                .id(projection.getId())
                .name(projection.getName())
                .leader(leader)
                .meetingDay(meetingDay)
                .meetingDaySpanishName(meetingDay != null ? meetingDay.getDaySpanishName() : null)
                .meetingTime(projection.getMeetingTime())
                .location(projection.getLocation())
                .isPastorCell(projection.getIsPastorCell() != null && projection.getIsPastorCell())
                .pastorCellGender(projection.getPastorCellGender() != null
                        ? GenderEnum.valueOf(projection.getPastorCellGender())
                        : null)
                .memberCount(projection.getMemberCount())
                .build();
    }

    private String blankToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isBlank() ? null : trimmed;
    }
}
