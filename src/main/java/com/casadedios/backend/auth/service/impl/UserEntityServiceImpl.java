package com.casadedios.backend.auth.service.impl;

import com.casadedios.backend.auth.dto.request.AuthUserRegisterRequestDto;
import com.casadedios.backend.auth.dto.response.AuthUserEntityProfileResponseDto;
import com.casadedios.backend.auth.dto.response.AuthUserRegisterResponseDto;
import com.casadedios.backend.auth.enums.RoleEnum;
import com.casadedios.backend.auth.mapper.UserMapper;
import com.casadedios.backend.auth.persistence.model.UserEntity;
import com.casadedios.backend.auth.persistence.repository.UserEntityRepository;
import com.casadedios.backend.auth.service.UserEntityService;
import com.casadedios.backend.common.exception.enums.ApiError;
import com.casadedios.backend.common.exception.model.CasaDeDiosException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserEntityServiceImpl implements UserEntityService {

    private final UserEntityRepository userEntityRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthUserRegisterResponseDto create(AuthUserRegisterRequestDto authUserRegisterRequestDto) {
        UserEntity user = userMapper.toEntity(authUserRegisterRequestDto);

        user.setPasswordHash(passwordEncoder.encode(authUserRegisterRequestDto.password()));
        user.setRole(RoleEnum.PASTOR);

        UserEntity savedUser = userEntityRepository.save(user);

        log.info("Usuario creado exitosamente con rol {}", savedUser.getRole());

        return userMapper.toRegisterResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthUserEntityProfileResponseDto findCurrentUser(String username) {
        // username viene del subject del JWT, ya validado por el filtro
        UserEntity user = userEntityRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> {
                    log.debug("Usuario autenticado no encontrado en BD: {}", username);
                    return new CasaDeDiosException(ApiError.USER_NOT_FOUND);
                });

        return userMapper.toProfileResponseDto(user);
    }
}
