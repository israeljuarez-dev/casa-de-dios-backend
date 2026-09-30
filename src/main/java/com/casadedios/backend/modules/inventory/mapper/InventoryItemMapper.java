package com.casadedios.backend.modules.inventory.mapper;

import com.casadedios.backend.modules.inventory.dto.request.InventoryItemRegisterRequestDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemUpdateRequestDto;
import com.casadedios.backend.modules.inventory.dto.response.InventoryItemDonorResponseDto;
import com.casadedios.backend.modules.inventory.dto.response.InventoryItemResponseDto;
import com.casadedios.backend.modules.inventory.persistence.model.InventoryItem;
import com.casadedios.backend.modules.inventory.persistence.projection.InventoryItemSummaryProjection;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface InventoryItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "donor", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    InventoryItem toEntity(InventoryItemRegisterRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    @Mapping(target = "donor", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(InventoryItemUpdateRequestDto dto, @MappingTarget InventoryItem entity);

    @Mapping(target = "donor", source = "projection")
    InventoryItemResponseDto toResponseDto(InventoryItemSummaryProjection projection);

    default InventoryItemDonorResponseDto toDonorResponseDto(InventoryItemSummaryProjection projection) {
        if (projection.getDonorId() == null) {
            return null;
        }

        return InventoryItemDonorResponseDto.builder()
                .id(projection.getDonorId())
                .firstName(projection.getDonorFirstName())
                .lastName(projection.getDonorLastName())
                .build();
    }
}
