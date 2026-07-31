package com.casadedios.backend.auth.service;

import com.casadedios.backend.auth.dto.request.AuthUserRegisterRequestDto;
import com.casadedios.backend.auth.dto.response.AuthUserRegisterResponseDto;

public interface UserEntityService {
    AuthUserRegisterResponseDto create(AuthUserRegisterRequestDto authUserRegisterRequestDto);
}
