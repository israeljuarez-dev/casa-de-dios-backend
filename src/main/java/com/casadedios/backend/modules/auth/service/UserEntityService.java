package com.casadedios.backend.modules.auth.service;

import com.casadedios.backend.modules.auth.dto.request.AuthUserRegisterRequestDto;
import com.casadedios.backend.modules.auth.dto.response.AuthUserEntityProfileResponseDto;
import com.casadedios.backend.modules.auth.dto.response.AuthUserRegisterResponseDto;

public interface UserEntityService {
    AuthUserRegisterResponseDto create(AuthUserRegisterRequestDto authUserRegisterRequestDto);

    AuthUserEntityProfileResponseDto findCurrentUser(String username);
}
