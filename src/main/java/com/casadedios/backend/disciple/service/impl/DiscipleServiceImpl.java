package com.casadedios.backend.disciple.service.impl;

import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.common.exception.enums.ApiError;
import com.casadedios.backend.common.exception.model.CasaDeDiosException;
import com.casadedios.backend.disciple.dto.request.*;
import com.casadedios.backend.disciple.dto.response.DiscipleChildResponseDto;
import com.casadedios.backend.disciple.dto.response.DiscipleInviterResponseDto;
import com.casadedios.backend.disciple.dto.response.DiscipleResponseDto;
import com.casadedios.backend.disciple.enums.MaritalStatus;
import com.casadedios.backend.disciple.enums.RelationshipType;
import com.casadedios.backend.disciple.enums.SpiritualLevel;
import com.casadedios.backend.disciple.export.DiscipleExcelExporter;
import com.casadedios.backend.disciple.mapper.DiscipleMapper;
import com.casadedios.backend.disciple.persistence.model.Disciple;
import com.casadedios.backend.disciple.persistence.model.DiscipleRelationship;
import com.casadedios.backend.disciple.persistence.repository.DiscipleRelationshipRepository;
import com.casadedios.backend.disciple.persistence.repository.DiscipleRepository;
import com.casadedios.backend.disciple.persistence.specification.DiscipleSpecification;
import com.casadedios.backend.disciple.service.DiscipleService;
import com.casadedios.backend.disciple.util.DiscipleDateCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscipleServiceImpl implements DiscipleService {

    private final DiscipleRepository discipleRepository;

    private final DiscipleRelationshipRepository relationshipRepository;

    private final DiscipleMapper discipleMapper;

    private final DiscipleDateCalculator discipleDateCalculator;

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<DiscipleResponseDto> findAll(DiscipleSearchCriteriaDto criteria){
        Pageable pageable = criteria.pagination().toPageable();

        Page<Disciple> page = discipleRepository.findAll(DiscipleSpecification.withSearchCriteria(criteria), pageable);

        List<DiscipleResponseDto> content = page.getContent().stream()
                .map(this::toResponseDtoWithRelationships)
                .toList();

        return PaginationResponseDto.of(content, page);
    }

    @Override
    @Transactional(readOnly = true)
    public DiscipleResponseDto findById(Long id) {
        Disciple disciple = getDiscipleOrThrow(id);
        return toResponseDtoWithRelationships(disciple);
    }

    @Override
    @Transactional
    public DiscipleResponseDto create(DiscipleRegisterRequestDto request) {
        validateCoupleNameRequirement(request.maritalStatus(), request.coupleName());

        validateIsLeaderRequirement(request.isLeader(), request.spiritualLevel());

        validateUniqueDni(request.dni());
        validateUniquePhoneNumber(request.phoneNumber());

        Disciple entity = discipleMapper.toEntity(request);

        Disciple savedEntity = discipleRepository.save(entity);

        attachChildren(savedEntity, request.children());
        attachInviter(savedEntity, request.invitedByDiscipleId());

        log.info("Discípulo registrado exitosamente con id {}",  savedEntity.getId());

        return toResponseDtoWithRelationships(savedEntity);
    }

    @Override
    @Transactional
    public DiscipleResponseDto update(Long id, DiscipleUpdateRequestDto request) {
        validateUniqueDniOnUpdate(request.dni(), id);
        validateUniquePhoneNumberOnUpdate(request.phoneNumber(), id);

        Disciple disciple = getDiscipleOrThrow(id);

        discipleMapper.updateEntityFromDto(request, disciple);

        validateCoupleNameRequirement(disciple.getMaritalStatus(), disciple.getCoupleName());

        validateIsLeaderRequirement(disciple.isLeader(), disciple.getSpiritualLevel());

        Disciple updatedEntity = discipleRepository.save(disciple);

        syncChildren(updatedEntity, request.children());


        log.info("Discípulo con id {} actualizado exitosamente", id);

        return toResponseDtoWithRelationships(updatedEntity);
    }

    @Override
    @Transactional
    public void softDeleteById(Long id) {
        Disciple disciple = discipleRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.debug("No existe un discípulo activo con id {}, no se puede eliminar", id);
                    return new CasaDeDiosException(ApiError.DISCIPLE_NOT_FOUND);
                });

        disciple.setActive(false);

        discipleRepository.save(disciple);

        log.warn("Discípulo con id {} marcado como inactivo (soft delete)", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ByteArrayOutputStream exportToExcel(DiscipleSearchCriteriaDto criteria) throws IOException {
        long totalRecords = discipleRepository.count(DiscipleSpecification.withSearchCriteria(criteria));

        Pageable pageable = Pageable.ofSize((int) totalRecords);

        Page<Disciple> page = discipleRepository.findAll(
                DiscipleSpecification.withSearchCriteria(criteria),
                pageable
        );

        List<DiscipleResponseDto> disciplesDto = page.getContent().stream()
                .map(this::toResponseDtoWithRelationships)
                .toList();

        DiscipleExcelExporter exporter = new DiscipleExcelExporter(disciplesDto);
        ByteArrayOutputStream outputStream = exporter.export();

        log.info("Reporte de discípulos exportado exitosamente con {} registros", disciplesDto.size());

        return outputStream;
    }

    @Override
    public String generateExcelFileName() {
        return "Reporte_Discipulos_" +
                LocalDate.now(java.time.ZoneId.systemDefault())
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd")) +
                ".xlsx";
    }

    private Disciple getDiscipleOrThrow(Long id) {
        return discipleRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    log.debug("No existe un discípulo activo con id {}", id);
                    return new CasaDeDiosException(ApiError.DISCIPLE_NOT_FOUND);
                });
    }

    private void validateCoupleNameRequirement(MaritalStatus maritalStatus, String coupleName) {
        boolean requiresCouple = maritalStatus == MaritalStatus.MARRIED;
        boolean coupleNameMissing = coupleName == null || coupleName.isBlank();

        if (requiresCouple && coupleNameMissing) {
            log.warn("Validación fallida: 'coupleName' es obligatorio cuando el estado civil es '{}'.", maritalStatus);
            throw new CasaDeDiosException(
                    ApiError.VALIDATION_ERROR,
                    List.of("coupleName - es obligatorio cuando el estado civil es Casado/a")
            );
        }
    }

    private void validateIsLeaderRequirement(boolean isLeader, SpiritualLevel spiritualLevel) {
        boolean eligible = spiritualLevel.isLeaderEligible();

        if (isLeader && !eligible) {
            log.warn("Validación fallida: 'isLeader' no puede ser true cuando el nivel espiritual es '{}'.", spiritualLevel);
            throw new CasaDeDiosException(
                    ApiError.VALIDATION_ERROR,
                    List.of("isLeader - solo puede ser true cuando el nivel espiritual es LEADER, CELL_LEADER o LEADERSHIP_SCHOOL_TEACHER")
            );
        }

        if (!isLeader && eligible) {
            log.warn("Validación fallida: 'isLeader' debe ser true cuando el nivel espiritual es '{}'.", spiritualLevel);
            throw new CasaDeDiosException(
                    ApiError.VALIDATION_ERROR,
                    List.of("isLeader - debe ser true cuando el nivel espiritual es LEADER, CELL_LEADER o LEADERSHIP_SCHOOL_TEACHER")
            );
        }
    }

    private void validateUniqueDni(String dni) {
        if (dni != null && discipleRepository.existsByDni(dni)) {
            log.warn("Intento de registrar un discípulo con un DNI ya existente.");
            throw new CasaDeDiosException(ApiError.DUPLICATE_DNI);
        }
    }

    private void validateUniquePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && discipleRepository.existsByPhoneNumber(phoneNumber)) {
            log.warn("Intento de registrar un discípulo con un número de teléfono ya existente.");
            throw new CasaDeDiosException(ApiError.DUPLICATE_PHONE_NUMBER);
        }
    }

    private void attachChildren(Disciple parent, List<DiscipleChildRegisterRequestDto> childrenRequest) {
        if (childrenRequest == null) {
            return;
        }

        childrenRequest.forEach(childDto -> {
            Disciple childEntity = Disciple.builder()
                    .firstName(childDto.firstName())
                    .lastName(childDto.lastName())
                    .birthDate(childDto.birthDate())
                    .maritalStatus(MaritalStatus.SINGLE)
                    .build();

            Disciple savedChild = discipleRepository.save(childEntity);

            DiscipleRelationship relationship = DiscipleRelationship.builder()
                    .sourceDisciple(parent)
                    .targetDisciple(savedChild)
                    .relationshipType(RelationshipType.PARENT_CHILD)
                    .build();

            relationshipRepository.save(relationship);
        });
    }

    private void attachInviter(Disciple disciple, Long invitedByDiscipleId) {
        relationshipRepository.deleteByTargetDisciple_IdAndRelationshipType(
                disciple.getId(), RelationshipType.INVITED_BY);

        if (invitedByDiscipleId == null) {
            return;
        }

        Disciple inviter = discipleRepository.findById(invitedByDiscipleId)
                .orElseThrow(() -> {
                    log.warn("No se encontró el discípulo invitador con id '{}'.", invitedByDiscipleId);
                    return new CasaDeDiosException(ApiError.INVITER_NOT_FOUND);
                });

        DiscipleRelationship relationship = DiscipleRelationship.builder()
                .sourceDisciple(inviter)
                .targetDisciple(disciple)
                .relationshipType(RelationshipType.INVITED_BY)
                .build();

        relationshipRepository.save(relationship);
    }

    private DiscipleResponseDto toResponseDtoWithRelationships(Disciple entity) {
        List<DiscipleChildResponseDto> children = relationshipRepository
                .findBySourceDisciple_IdAndRelationshipType(entity.getId(), RelationshipType.PARENT_CHILD)
                .stream()
                .map(relationship -> discipleMapper.toChildResponseDto(relationship.getTargetDisciple()))
                .toList();

        DiscipleInviterResponseDto inviter = relationshipRepository
                .findByTargetDisciple_IdAndRelationshipType(entity.getId(), RelationshipType.INVITED_BY)
                .map(relationship -> {
                    Disciple sourceDisciple = relationship.getSourceDisciple();
                    return new DiscipleInviterResponseDto(
                            sourceDisciple.getId(), sourceDisciple.getFirstName(), sourceDisciple.getLastName());
                })
                .orElse(null);

        return discipleMapper.toResponseDto(entity, children, inviter);
    }

    private void validateUniqueDniOnUpdate(String dni, Long currentId) {
        if (dni != null && discipleRepository.existsByDniAndIdNot(dni, currentId)) {
            log.warn("Intento de actualizar el discípulo {} con un DNI ya usado por otro registro.", currentId);
            throw new CasaDeDiosException(ApiError.DUPLICATE_DNI);
        }
    }

    private void validateUniquePhoneNumberOnUpdate(String phoneNumber, Long currentId) {
        if (phoneNumber != null && discipleRepository.existsByPhoneNumberAndIdNot(phoneNumber, currentId)) {
            log.warn("Intento de actualizar el discípulo {} con un teléfono ya usado por otro registro.", currentId);
            throw new CasaDeDiosException(ApiError.DUPLICATE_PHONE_NUMBER);
        }
    }

    private void syncChildren(Disciple parent, List<DiscipleChildUpdateRequestDto> childrenRequest) {
        if (childrenRequest == null) {
            return;
        }

        List<DiscipleRelationship> existingRelationships = relationshipRepository.findBySourceDisciple_IdAndRelationshipType(
                parent.getId(),
                RelationshipType.PARENT_CHILD
        );

        List<Long> incomingChildIds = childrenRequest.stream()
                .map(DiscipleChildUpdateRequestDto::id)
                .filter(Objects::nonNull)
                .toList();

        existingRelationships.stream()
                .filter(relationship -> !incomingChildIds.contains(relationship.getTargetDisciple().getId()))
                .forEach(relationship -> {
                    Long childId = relationship.getTargetDisciple().getId();
                    relationshipRepository.delete(relationship);
                    discipleRepository.deleteById(childId);
                    log.debug("Hijo con id {} eliminado por no estar en la lista de actualización", childId);
                });

        childrenRequest.forEach(childDto -> {
            if (childDto.id() != null) {
                updateExistingChild(childDto);
            } else {
                createNewChild(parent, childDto);
            }
        });
    }

    private void updateExistingChild(DiscipleChildUpdateRequestDto childDto) {
        Disciple childEntity = discipleRepository.findById(childDto.id())
                .orElseThrow(() -> {
                    log.debug("No existe un hijo con id {} para actualizar", childDto.id());
                    return new CasaDeDiosException(ApiError.DISCIPLE_NOT_FOUND);
                });

        if (childDto.firstName() != null) {
            childEntity.setFirstName(childDto.firstName());
        }

        if (childDto.lastName() != null) {
            childEntity.setLastName(childDto.lastName());
        }

        if (childDto.birthDate() != null) {
            childEntity.setBirthDate(childDto.birthDate());
        }

        ensureChildMaritalStatus(childEntity);

        discipleRepository.save(childEntity);
    }

    private void createNewChild(Disciple parent, DiscipleChildUpdateRequestDto childDto) {
        Disciple childEntity = Disciple.builder()
                .firstName(childDto.firstName())
                .lastName(childDto.lastName())
                .birthDate(childDto.birthDate())
                .maritalStatus(MaritalStatus.SINGLE)
                .spiritualLevel(SpiritualLevel.GUEST)
                .build();

        Disciple savedChild = discipleRepository.save(childEntity);

        DiscipleRelationship relationship = DiscipleRelationship.builder()
                .sourceDisciple(parent)
                .targetDisciple(savedChild)
                .relationshipType(RelationshipType.PARENT_CHILD)
                .build();

        relationshipRepository.save(relationship);
    }

    private void ensureChildMaritalStatus(Disciple child) {
        int age = discipleDateCalculator.calculateAge(child.getBirthDate());

        if (age < 18) {
            child.setMaritalStatus(MaritalStatus.SINGLE);
            child.setCoupleName(null);
        }
    }
}