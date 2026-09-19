package com.casadedios.backend.modules.disciple.mapper;

import com.casadedios.backend.common.enums.GenderEnum;
import com.casadedios.backend.modules.disciple.dto.request.DiscipleChildRegisterRequestDto;
import com.casadedios.backend.modules.disciple.dto.request.DiscipleChildUpdateRequestDto;
import com.casadedios.backend.modules.disciple.dto.request.DiscipleRegisterRequestDto;
import com.casadedios.backend.modules.disciple.dto.request.DiscipleUpdateRequestDto;
import com.casadedios.backend.modules.disciple.dto.response.DiscipleChildResponseDto;
import com.casadedios.backend.modules.disciple.dto.response.DiscipleInviterResponseDto;
import com.casadedios.backend.modules.disciple.dto.response.DiscipleParentResponseDto;
import com.casadedios.backend.modules.disciple.dto.response.DiscipleResponseDto;
import com.casadedios.backend.modules.disciple.enums.MaritalStatus;
import com.casadedios.backend.modules.disciple.persistence.model.Disciple;
import com.casadedios.backend.modules.disciple.persistence.projection.ChildProjection;
import com.casadedios.backend.modules.disciple.persistence.projection.InviterProjection;
import com.casadedios.backend.modules.disciple.persistence.projection.ParentProjection;
import com.casadedios.backend.modules.disciple.util.DiscipleDateCalculator;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = DiscipleDateCalculator.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DiscipleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Disciple toEntity(DiscipleRegisterRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cellGroupLeader", source = "isCellGroupLeader")
    @Mapping(target = "teacher",         source = "isTeacher")
    @Mapping(target = "occupation",      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "address",         nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "phoneCodeNumber", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "phoneNumber",     nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "dni",             nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "coupleName",      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void updateEntityFromDto(DiscipleUpdateRequestDto dto, @MappingTarget Disciple entity);

    @Mapping(target = "age",             source = "birthDate", qualifiedByName = "calculateAge")
    @Mapping(target = "birthdayAlert",   source = "birthDate", qualifiedByName = "calculateBirthdayAlert")
    @Mapping(target = "isCellGroupLeader", source = "cellGroupLeader")
    @Mapping(target = "isTeacher", source = "teacher")
    @Mapping(target = "hasChildren", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "invitedBy", ignore = true)
    @Mapping(target = "parents", ignore = true)
    @Mapping(target = "isCellGroupMember", ignore = true)
    DiscipleResponseDto toResponseDto(Disciple entity);

    List<DiscipleResponseDto> toResponseDtoList(List<Disciple> entities);

    @Mapping(target = "age", source = "birthDate", qualifiedByName = "calculateAge")
    DiscipleChildResponseDto toChildResponseDto(Disciple entity);

    @Mapping(target = "id",     source = "childId")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "age",    source = "birthDate", qualifiedByName = "calculateAge")
    DiscipleChildResponseDto fromChildProjection(ChildProjection proj);

    @Mapping(target = "id", source = "inviterId")
    DiscipleInviterResponseDto fromInviterProjection(InviterProjection proj);

    @Mapping(target = "id",     source = "parentId")
    @Mapping(target = "gender", source = "gender")
    DiscipleParentResponseDto fromParentProjection(ParentProjection proj);

    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "active",         ignore = true)
    @Mapping(target = "createdAt",      ignore = true)
    @Mapping(target = "updatedAt",      ignore = true)
    @Mapping(target = "occupation",     ignore = true)
    @Mapping(target = "phoneCodeNumber", ignore = true)
    @Mapping(target = "phoneNumber",    ignore = true)
    @Mapping(target = "address",        ignore = true)
    @Mapping(target = "dni",            ignore = true)
    @Mapping(target = "coupleName",     ignore = true)
    @Mapping(target = "isCellGroupLeader", ignore = true)
    @Mapping(target = "isTeacher",      ignore = true)
    @Mapping(target = "maritalStatus",  constant = "SINGLE")
    @Mapping(target = "spiritualLevel", constant = "GUEST")
    Disciple childRegisterToEntity(DiscipleChildRegisterRequestDto dto);

    @Mapping(target = "id",              ignore = true)
    @Mapping(target = "active",          ignore = true)
    @Mapping(target = "createdAt",       ignore = true)
    @Mapping(target = "updatedAt",       ignore = true)
    @Mapping(target = "occupation",      ignore = true)
    @Mapping(target = "phoneCodeNumber", ignore = true)
    @Mapping(target = "phoneNumber",     ignore = true)
    @Mapping(target = "address",         ignore = true)
    @Mapping(target = "dni",             ignore = true)
    @Mapping(target = "coupleName",      ignore = true)
    @Mapping(target = "isCellGroupLeader", ignore = true)
    @Mapping(target = "isTeacher",       ignore = true)
    @Mapping(target = "maritalStatus",   constant = "SINGLE")
    @Mapping(target = "spiritualLevel",  constant = "GUEST")
    Disciple childUpdateToEntity(DiscipleChildUpdateRequestDto dto);

    @AfterMapping
    default void applyDefaults(@MappingTarget Disciple entity) {
        if (entity.getMaritalStatus() == null) {
            entity.setMaritalStatus(MaritalStatus.SINGLE);
        }
    }

    default GenderEnum toGenderEnum(String gender) {
        return GenderEnum.valueOf(gender);
    }
}
