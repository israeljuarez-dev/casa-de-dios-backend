package com.casadedios.backend.auth.mapper;

import com.casadedios.backend.auth.dto.request.AuthUserRegisterRequestDto;
import com.casadedios.backend.auth.dto.response.AuthUserEntityProfileResponseDto;
import com.casadedios.backend.auth.dto.response.AuthUserRegisterResponseDto;
import com.casadedios.backend.auth.persistence.model.UserEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
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

