package com.casadedios.backend.modules.retreat.mapper;

import com.casadedios.backend.modules.retreat.dto.request.RetreatRegisterRequestDto;
import com.casadedios.backend.modules.retreat.dto.request.RetreatUpdateRequestDto;
import com.casadedios.backend.modules.retreat.persistence.model.Retreat;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RetreatMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Retreat toEntity(RetreatRegisterRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(RetreatUpdateRequestDto dto, @MappingTarget Retreat entity);
}
