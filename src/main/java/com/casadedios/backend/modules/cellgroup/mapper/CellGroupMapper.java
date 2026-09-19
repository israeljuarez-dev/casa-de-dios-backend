package com.casadedios.backend.modules.cellgroup.mapper;

import com.casadedios.backend.modules.cellgroup.dto.request.CellGroupRegisterRequestDto;
import com.casadedios.backend.modules.cellgroup.dto.request.CellGroupUpdateRequestDto;
import com.casadedios.backend.modules.cellgroup.dto.response.CellGroupMemberResponseDto;
import com.casadedios.backend.modules.cellgroup.persistence.model.CellGroup;
import com.casadedios.backend.modules.cellgroup.persistence.model.CellGroupMember;
import com.casadedios.backend.modules.disciple.util.DiscipleDateCalculator;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = DiscipleDateCalculator.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CellGroupMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "leader", ignore = true)
    @Mapping(target = "isPastorCell", source = "isPastorCell", defaultValue = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CellGroup toEntity(CellGroupRegisterRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "leader", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(CellGroupUpdateRequestDto dto, @MappingTarget CellGroup entity);

    @Mapping(target = "memberId", source = "id")
    @Mapping(target = "discipleId", source = "disciple.id")
    @Mapping(target = "firstName", source = "disciple.firstName")
    @Mapping(target = "lastName", source = "disciple.lastName")
    @Mapping(target = "phoneCodeNumber", source = "disciple.phoneCodeNumber")
    @Mapping(target = "phoneNumber", source = "disciple.phoneNumber")
    @Mapping(target = "spiritualLevel", source = "disciple.spiritualLevel")
    @Mapping(target = "birthDate", source = "disciple.birthDate")
    @Mapping(target = "age", source = "disciple.birthDate", qualifiedByName = "calculateAge")
    @Mapping(target = "gender", source = "disciple.gender")
    @Mapping(target = "isCellGroupLeader", source = "disciple.cellGroupLeader")
    @Mapping(target = "isCoreTwelve", source = "coreTwelve")
    @Mapping(target = "isPastorCoreTwelve", source = "pastorCoreTwelve")
    @Mapping(target = "birthdayAlert", source = "disciple.birthDate", qualifiedByName = "calculateBirthdayAlert")
    CellGroupMemberResponseDto toMemberResponseDto(CellGroupMember entity);
}