package com.casadedios.backend.disciple.mapper;

import com.casadedios.backend.disciple.dto.request.DiscipleRegisterRequestDto;
import com.casadedios.backend.disciple.dto.request.DiscipleUpdateRequestDto;
import com.casadedios.backend.disciple.dto.response.DiscipleChildResponseDto;
import com.casadedios.backend.disciple.dto.response.DiscipleInviterResponseDto;
import com.casadedios.backend.disciple.dto.response.DiscipleResponseDto;
import com.casadedios.backend.disciple.enums.MaritalStatus;
import com.casadedios.backend.disciple.persistence.model.Disciple;
import com.casadedios.backend.disciple.util.DiscipleDateCalculator;
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
    @Mapping(target = "leader", source = "isLeader")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(DiscipleUpdateRequestDto dto, @MappingTarget Disciple entity);

    @Mapping(target = "age", source = "birthDate")
    @Mapping(target = "birthdayAlert", source = "birthDate")
    @Mapping(target = "hasChildren", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "invitedBy", ignore = true)
    @Mapping(target = "isLeader", source = "leader")
    DiscipleResponseDto toResponseDto(
            Disciple entity,
            @Context List<DiscipleChildResponseDto> children,
            @Context DiscipleInviterResponseDto invitedBy
    );

    @AfterMapping
    default void attachRelationships(
            @MappingTarget DiscipleResponseDto.DiscipleResponseDtoBuilder builder,
            @Context List<DiscipleChildResponseDto> children,
            @Context DiscipleInviterResponseDto invitedBy
    ) {
        builder.children(children)
                .hasChildren(!children.isEmpty())
                .invitedBy(invitedBy);
    }
    List<DiscipleResponseDto> toResponseDtoList(List<Disciple> entities);

    @Mapping(target = "age", source = "birthDate")
    DiscipleChildResponseDto toChildResponseDto(Disciple entity);

    @AfterMapping
    default void applyDefaults(@MappingTarget Disciple entity) {
        if (entity.getMaritalStatus() == null) {
            entity.setMaritalStatus(MaritalStatus.SINGLE);
        }
    }
}
