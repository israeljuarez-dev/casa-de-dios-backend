package com.casadedios.backend.modules.auth.mapper;

import com.casadedios.backend.modules.auth.dto.request.AuthUserRegisterRequestDto;
import com.casadedios.backend.modules.auth.dto.response.AuthUserEntityProfileResponseDto;
import com.casadedios.backend.modules.auth.dto.response.AuthUserRegisterResponseDto;
import com.casadedios.backend.modules.auth.persistence.model.UserEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    UserEntity toEntity(AuthUserRegisterRequestDto dto);

    AuthUserRegisterResponseDto toRegisterResponseDto(UserEntity entity);

    AuthUserEntityProfileResponseDto toProfileResponseDto(UserEntity entity);
}

