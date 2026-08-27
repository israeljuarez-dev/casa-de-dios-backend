package com.casadedios.backend.disciple.service.impl;

import com.casadedios.backend.cellgroup.persistence.repository.CellGroupMemberRepository;
import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.common.enums.GenderEnum;
import com.casadedios.backend.common.exception.enums.ApiError;
import com.casadedios.backend.common.exception.model.CasaDeDiosException;
import com.casadedios.backend.disciple.dto.request.*;
import com.casadedios.backend.disciple.dto.response.DiscipleChildResponseDto;
import com.casadedios.backend.disciple.dto.response.DiscipleInviterResponseDto;
import com.casadedios.backend.disciple.dto.response.DiscipleParentResponseDto;
import com.casadedios.backend.disciple.dto.response.DiscipleResponseDto;
import com.casadedios.backend.disciple.enums.MaritalStatus;
import com.casadedios.backend.disciple.enums.RelationshipType;
import com.casadedios.backend.disciple.enums.SpiritualLevel;
import com.casadedios.backend.disciple.export.DiscipleExcelExporter;
import com.casadedios.backend.disciple.mapper.DiscipleMapper;
import com.casadedios.backend.disciple.persistence.model.Disciple;
import com.casadedios.backend.disciple.persistence.model.DiscipleRelationship;
import com.casadedios.backend.disciple.persistence.projection.ChildProjection;
import com.casadedios.backend.disciple.persistence.projection.ChildRelationshipProjection;
import com.casadedios.backend.disciple.persistence.projection.InviterProjection;
import com.casadedios.backend.disciple.persistence.projection.ParentProjection;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscipleServiceImpl implements DiscipleService {

    private final DiscipleRepository discipleRepository;

    private final DiscipleRelationshipRepository discipleRelationshipRepository;

    private final DiscipleMapper discipleMapper;

    private final DiscipleDateCalculator discipleDateCalculator;

    private final CellGroupMemberRepository cellGroupMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<DiscipleResponseDto> findAll(DiscipleSearchCriteriaDto criteria){
        Pageable pageable = criteria.pagination().toPageable();

        Page<Disciple> page = discipleRepository.findAll(DiscipleSpecification.withSearchCriteria(criteria), pageable);

        List<DiscipleResponseDto> content = mapPageWithRelationships(page.getContent());

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

        validateIsLeaderRequirement(request.isCellGroupLeader(), request.spiritualLevel());

        boolean isTeacher = Boolean.TRUE.equals(request.isTeacher());
        validateIsTeacherRequirement(isTeacher, request.spiritualLevel());

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

        validateIsLeaderRequirement(disciple.isCellGroupLeader(), disciple.getSpiritualLevel());

        validateIsTeacherRequirement(disciple.isTeacher(), disciple.getSpiritualLevel());

        Disciple updatedEntity = discipleRepository.save(disciple);

        syncChildren(updatedEntity, request.children());


        log.info("Discípulo con id {} actualizado exitosamente", id);

        return toResponseDtoWithRelationships(updatedEntity);
    }

    @Override
    @Transactional
    public void softDeleteById(Long id) {
        if (!discipleRepository.existsById(id)) {
            log.debug("No existe un discípulo activo con id {}, no se puede eliminar", id);
            throw new CasaDeDiosException(ApiError.DISCIPLE_NOT_FOUND);
        }

        discipleRepository.softDeleteById(id);

        log.warn("Discípulo con id {} marcado como inactivo (soft delete)", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ByteArrayOutputStream exportToExcel(DiscipleSearchCriteriaDto criteria) throws IOException {
        List<Disciple> disciples = discipleRepository.findAll(
                DiscipleSpecification.withSearchCriteria(criteria),
                Pageable.unpaged()  // trae todos sin paginación, sin query de count extra
        ).getContent();

        List<DiscipleResponseDto> disciplesDto = mapPageWithRelationships(disciples);

        DiscipleExcelExporter exporter = new DiscipleExcelExporter(disciplesDto);
        ByteArrayOutputStream outputStream = exporter.export();

        log.info("Reporte de discípulos exportado exitosamente con {} registros", disciplesDto.size());

        return outputStream;
    }

    @Override
    public String generateExcelFileName() {
        return "Reporte_Discipulos_"
                + LocalDate.now(java.time.ZoneId.systemDefault()).format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                + ".xlsx";
    }

    private List<DiscipleResponseDto> mapPageWithRelationships(List<Disciple> disciples) {
        if (disciples.isEmpty()) {
            return List.of();
        }

        List<Long> discipleIds = disciples.stream()
                .map(Disciple::getId)
                .toList();

        Map<Long, List<DiscipleChildResponseDto>> childrenByParentId = fetchChildrenMap(discipleIds);
        Map<Long, DiscipleInviterResponseDto> inviterByDiscipleId = fetchInvitersMap(discipleIds);
        Map<Long, List<DiscipleParentResponseDto>> parentsByChildId = fetchParentsMap(discipleIds);
        Set<Long> cellGroupMemberIds = fetchCellGroupMemberIds(discipleIds);

        return disciples.stream()
                .map(disciple -> {
                    DiscipleResponseDto dto = discipleMapper.toResponseDto(disciple);
                    return dto.toBuilder()
                            .children(childrenByParentId.getOrDefault(disciple.getId(), List.of()))
                            .hasChildren(!childrenByParentId.getOrDefault(disciple.getId(), List.of()).isEmpty())
                            .invitedBy(inviterByDiscipleId.get(disciple.getId()))
                            .parents(parentsByChildId.getOrDefault(disciple.getId(), List.of()))
                            .isCellGroupMember(cellGroupMemberIds.contains(disciple.getId()))
                            .build();
                })
                .toList();
    }

    private Set<Long> fetchCellGroupMemberIds(List<Long> discipleIds) {
        return new HashSet<>(cellGroupMemberRepository.findDiscipleIdsByDiscipleIdIn(discipleIds));
    }

    /*
    private Map<Long, List<DiscipleChildResponseDto>> fetchChildrenMap(List<Long> discipleIds) {
        return discipleRelationshipRepository.findChildrenBySourceIds(discipleIds, RelationshipType.PARENT_CHILD.name()).stream()
                .collect(Collectors.groupingBy(
                        ChildProjection::getParentId,
                        Collectors.mapping(
                                proj -> new DiscipleChildResponseDto(
                                        proj.getChildId(),
                                        proj.getFirstName(),
                                        proj.getLastName(),
                                        GenderEnum.valueOf(proj.getGender()),
                                        proj.getBirthDate(),
                                        discipleDateCalculator.calculateAge(proj.getBirthDate())
                                ),
                                Collectors.toList()
                        )
                ));
    }*/

    private Map<Long, List<DiscipleChildResponseDto>> fetchChildrenMap(List<Long> discipleIds) {
        return discipleRelationshipRepository
                .findChildrenBySourceIds(discipleIds, RelationshipType.PARENT_CHILD.name())
                .stream()
                .collect(Collectors.groupingBy(
                        ChildProjection::getParentId,
                        Collectors.mapping(
                                discipleMapper::fromChildProjection,
                                Collectors.toList()
                        )
                ));
    }

    /*
    private Map<Long, List<DiscipleParentResponseDto>> fetchParentsMap(List<Long> discipleIds) {
        return discipleRelationshipRepository
                .findParentsByChildIds(discipleIds, RelationshipType.PARENT_CHILD.name())
                .stream()
                .collect(Collectors.groupingBy(
                        ParentProjection::getChildId,
                        Collectors.mapping(
                                proj -> new DiscipleParentResponseDto(
                                        proj.getParentId(),
                                        proj.getFirstName(),
                                        proj.getLastName(),
                                        GenderEnum.valueOf(proj.getGender())
                                ),
                                Collectors.toList()
                        )
                ));
    }*/

    private Map<Long, List<DiscipleParentResponseDto>> fetchParentsMap(List<Long> discipleIds) {
        return discipleRelationshipRepository
                .findParentsByChildIds(discipleIds, RelationshipType.PARENT_CHILD.name())
                .stream()
                .collect(Collectors.groupingBy(
                        ParentProjection::getChildId,
                        Collectors.mapping(
                                discipleMapper::fromParentProjection,
                                Collectors.toList()
                        )
                ));
    }

    /*
    private Map<Long, DiscipleInviterResponseDto> fetchInvitersMap(List<Long> discipleIds) {
        return discipleRelationshipRepository.findInvitersByTargetIds(discipleIds, RelationshipType.INVITED_BY.name()).stream()
                .collect(Collectors.toMap(
                        InviterProjection::getDiscipleId,
                        proj -> new DiscipleInviterResponseDto(
                                proj.getInviterId(),
                                proj.getFirstName(),
                                proj.getLastName()
                        )
                ));
    }*/

    private Map<Long, DiscipleInviterResponseDto> fetchInvitersMap(List<Long> discipleIds) {
        return discipleRelationshipRepository
                .findInvitersByTargetIds(discipleIds, RelationshipType.INVITED_BY.name())
                .stream()
                .collect(Collectors.toMap(
                        InviterProjection::getDiscipleId,
                        discipleMapper::fromInviterProjection
                ));
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

    private void validateIsLeaderRequirement(boolean isCellGroupLeader, SpiritualLevel spiritualLevel) {
        boolean eligible = spiritualLevel.isLeaderEligible();

        if (isCellGroupLeader && !eligible) {
            log.warn("Validación fallida: 'isCellGroupLeader' no puede ser true cuando el nivel espiritual es '{}'.", spiritualLevel);
            throw new CasaDeDiosException(
                    ApiError.VALIDATION_ERROR,
                    List.of("isCellGroupLeader - solo puede ser true cuando el nivel espiritual es LEADER")
            );
        }

        if (!isCellGroupLeader && eligible) {
            log.warn("Validación fallida: 'isCellGroupLeader' debe ser true cuando el nivel espiritual es '{}'.", spiritualLevel);
            throw new CasaDeDiosException(
                    ApiError.VALIDATION_ERROR,
                    List.of("isCellGroupLeader - debe ser true cuando el nivel espiritual es LEADER")
            );
        }
    }

    private void validateIsTeacherRequirement(boolean isTeacher, SpiritualLevel spiritualLevel) {
        if (isTeacher && spiritualLevel != SpiritualLevel.LEADER) {
            log.warn("Validación fallida: 'isTeacher' no puede ser true cuando el nivel espiritual es '{}'.", spiritualLevel);
            throw new CasaDeDiosException(
                    ApiError.VALIDATION_ERROR,
                    List.of("isTeacher - solo puede ser true cuando el nivel espiritual es LEADER")
            );
        }
    }

    private void validateUniqueDni(String dni) {
        if (dni != null && !dni.isBlank() && discipleRepository.existsByDniAndActiveTrue(dni)) {
            log.warn("Intento de registrar un discípulo con un DNI ya existente.");
            throw new CasaDeDiosException(ApiError.DUPLICATE_DNI);
        }
    }

    private void validateUniquePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isBlank() && discipleRepository.existsByPhoneNumberAndActiveTrue(phoneNumber)) {
            log.warn("Intento de registrar un discípulo con un número de teléfono ya existente.");
            throw new CasaDeDiosException(ApiError.DUPLICATE_PHONE_NUMBER);
        }
    }

    /*
    private void attachChildren(Disciple parent, List<DiscipleChildRegisterRequestDto> childrenRequest) {
        if (childrenRequest == null || childrenRequest.isEmpty()) {
            return;
        }

        // Construye todos los hijos primero
        List<Disciple> children = childrenRequest.stream()
                .map(childDto -> Disciple.builder()
                        .firstName(childDto.firstName())
                        .lastName(childDto.lastName())
                        .gender(childDto.gender())
                        .birthDate(childDto.birthDate())
                        .maritalStatus(MaritalStatus.SINGLE)
                        .spiritualLevel(SpiritualLevel.GUEST)
                        .build())
                .toList();

        // Un solo batch INSERT en lugar de N inserts
        List<Disciple> savedChildren = discipleRepository.saveAll(children);

        // Construye todas las relaciones
        List<DiscipleRelationship> relationships = savedChildren.stream()
                .map(child -> DiscipleRelationship.builder()
                        .sourceDisciple(parent)
                        .targetDisciple(child)
                        .relationshipType(RelationshipType.PARENT_CHILD)
                        .build())
                .toList();

        // Un solo batch INSERT de relaciones
        discipleRelationshipRepository.saveAll(relationships);
    }*/

    private void attachChildren(Disciple parent, List<DiscipleChildRegisterRequestDto> childrenRequest) {
        if (childrenRequest == null || childrenRequest.isEmpty()) {
            return;
        }

        List<Disciple> children = childrenRequest.stream()
                .map(discipleMapper::childRegisterToEntity)
                .toList();

        List<Disciple> savedChildren = discipleRepository.saveAll(children);

        List<DiscipleRelationship> relationships = savedChildren.stream()
                .map(child -> DiscipleRelationship.builder()
                        .sourceDisciple(parent)
                        .targetDisciple(child)
                        .relationshipType(RelationshipType.PARENT_CHILD)
                        .build())
                .toList();

        discipleRelationshipRepository.saveAll(relationships);
    }

    private void attachInviter(Disciple disciple, Long invitedByDiscipleId) {
        discipleRelationshipRepository.deleteByTargetDisciple_IdAndRelationshipType(
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

        discipleRelationshipRepository.save(relationship);
    }

    /*
    private DiscipleResponseDto toResponseDtoWithRelationships(Disciple entity) {
        List<Long> id = List.of(entity.getId());

        List<DiscipleChildResponseDto> children = discipleRelationshipRepository
                .findChildrenBySourceIds(id, RelationshipType.PARENT_CHILD.name())
                .stream()
                .map(proj -> new DiscipleChildResponseDto(
                        proj.getChildId(),
                        proj.getFirstName(),
                        proj.getLastName(),
                        GenderEnum.valueOf(proj.getGender()),
                        proj.getBirthDate(),
                        discipleDateCalculator.calculateAge(proj.getBirthDate())
                ))
                .toList();

        DiscipleInviterResponseDto inviter = discipleRelationshipRepository
                .findInvitersByTargetIds(id, RelationshipType.INVITED_BY.name())
                .stream()
                .findFirst()
                .map(proj -> new DiscipleInviterResponseDto(
                        proj.getInviterId(),
                        proj.getFirstName(),
                        proj.getLastName()
                ))
                .orElse(null);

        List<DiscipleParentResponseDto> parents = discipleRelationshipRepository
                .findParentsByChildIds(id, RelationshipType.PARENT_CHILD.name())
                .stream()
                .map(proj -> new DiscipleParentResponseDto(
                        proj.getParentId(),
                        proj.getFirstName(),
                        proj.getLastName(),
                        GenderEnum.valueOf(proj.getGender())
                ))
                .toList();

        boolean isCellGroupMember = cellGroupMemberRepository.existsByDisciple_Id(entity.getId());

        DiscipleResponseDto dto = discipleMapper.toResponseDto(entity);
        return dto.toBuilder()
                .children(children)
                .hasChildren(!children.isEmpty())
                .invitedBy(inviter)
                .parents(parents)
                .isCellGroupMember(isCellGroupMember)
                .build();
    }*/

    private DiscipleResponseDto toResponseDtoWithRelationships(Disciple entity) {
        List<Long> ids = List.of(entity.getId());

        List<DiscipleChildResponseDto> children = discipleRelationshipRepository
                .findChildrenBySourceIds(ids, RelationshipType.PARENT_CHILD.name())
                .stream()
                .map(discipleMapper::fromChildProjection)
                .toList();

        DiscipleInviterResponseDto inviter = discipleRelationshipRepository
                .findInvitersByTargetIds(ids, RelationshipType.INVITED_BY.name())
                .stream()
                .findFirst()
                .map(discipleMapper::fromInviterProjection)
                .orElse(null);

        List<DiscipleParentResponseDto> parents = discipleRelationshipRepository
                .findParentsByChildIds(ids, RelationshipType.PARENT_CHILD.name())
                .stream()
                .map(discipleMapper::fromParentProjection)
                .toList();

        boolean isCellGroupMember = cellGroupMemberRepository.existsByDisciple_Id(entity.getId());

        DiscipleResponseDto dto = discipleMapper.toResponseDto(entity);
        return dto.toBuilder()
                .children(children)
                .hasChildren(!children.isEmpty())
                .invitedBy(inviter)
                .parents(parents)
                .isCellGroupMember(isCellGroupMember)
                .build();
    }

    private void validateUniqueDniOnUpdate(String dni, Long currentId) {
        if (dni != null && !dni.isBlank() && discipleRepository.existsByDniAndIdNotAndActiveTrue(dni, currentId)) {
            log.warn("Intento de actualizar el discípulo {} con un DNI ya usado por otro registro.", currentId);
            throw new CasaDeDiosException(ApiError.DUPLICATE_DNI);
        }
    }

    private void validateUniquePhoneNumberOnUpdate(String phoneNumber, Long currentId) {
        if (phoneNumber != null && !phoneNumber.isBlank() && discipleRepository.existsByPhoneNumberAndIdNotAndActiveTrue(phoneNumber, currentId)) {
            log.warn("Intento de actualizar el discípulo {} con un teléfono ya usado por otro registro.", currentId);
            throw new CasaDeDiosException(ApiError.DUPLICATE_PHONE_NUMBER);
        }
    }

    private void syncChildren(Disciple parent, List<DiscipleChildUpdateRequestDto> childrenRequest) {
        if (childrenRequest == null) {
            return;
        }

        List<ChildRelationshipProjection> existingRelationships = discipleRelationshipRepository
                .findChildRelationshipsByParentId(parent.getId(), RelationshipType.PARENT_CHILD.name());

        List<Long> incomingChildIds = childrenRequest.stream()
                .map(DiscipleChildUpdateRequestDto::id)
                .filter(Objects::nonNull)
                .toList();

        List<ChildRelationshipProjection> toDelete = existingRelationships.stream()
                .filter(rel -> !incomingChildIds.contains(rel.getChildId()))
                .toList();

        if (!toDelete.isEmpty()) {
            List<Long> relationshipIdsToDelete = toDelete.stream()
                    .map(ChildRelationshipProjection::getRelationshipId)
                    .toList();

            List<Long> childIdsToDelete = toDelete.stream()
                    .map(ChildRelationshipProjection::getChildId)
                    .toList();

            // Un solo DELETE para todas las relaciones
            discipleRelationshipRepository.deleteAllById(relationshipIdsToDelete);

            // Un solo DELETE para todos los hijos
            discipleRepository.deleteAllById(childIdsToDelete);

            childIdsToDelete.forEach(childId ->
                    log.debug("Hijo con id {} eliminado por no estar en la lista de actualización", childId));
        }

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

        if (childDto.gender() != null) {           // AÑADIDO: actualizar género si viene en el request
            childEntity.setGender(childDto.gender());
        }

        if (childDto.birthDate() != null) {
            childEntity.setBirthDate(childDto.birthDate());
        }

        ensureChildMaritalStatus(childEntity);

        discipleRepository.save(childEntity);
    }

    /*
    private void createNewChild(Disciple parent, DiscipleChildUpdateRequestDto childDto) {
        Disciple childEntity = Disciple.builder()
                .firstName(childDto.firstName())
                .lastName(childDto.lastName())
                .gender(childDto.gender())
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

        discipleRelationshipRepository.save(relationship);
    }*/

    private void createNewChild(Disciple parent, DiscipleChildUpdateRequestDto childDto) {
        Disciple savedChild = discipleRepository.save(discipleMapper.childUpdateToEntity(childDto));

        DiscipleRelationship relationship = DiscipleRelationship.builder()
                .sourceDisciple(parent)
                .targetDisciple(savedChild)
                .relationshipType(RelationshipType.PARENT_CHILD)
                .build();

        discipleRelationshipRepository.save(relationship);
    }

    private void ensureChildMaritalStatus(Disciple child) {
        int age = discipleDateCalculator.calculateAge(child.getBirthDate());

        if (age < 18) {
            child.setMaritalStatus(MaritalStatus.SINGLE);
            child.setCoupleName(null);
        }
    }
}