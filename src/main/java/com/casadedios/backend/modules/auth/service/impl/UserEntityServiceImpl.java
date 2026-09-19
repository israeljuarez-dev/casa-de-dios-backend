package com.casadedios.backend.modules.auth.service.impl;

import com.casadedios.backend.modules.auth.dto.request.AuthUserRegisterRequestDto;
import com.casadedios.backend.modules.auth.dto.response.AuthUserEntityProfileResponseDto;
import com.casadedios.backend.modules.auth.dto.response.AuthUserRegisterResponseDto;
import com.casadedios.backend.modules.auth.enums.RoleEnum;
import com.casadedios.backend.modules.auth.mapper.UserMapper;
import com.casadedios.backend.modules.auth.persistence.model.UserEntity;
import com.casadedios.backend.modules.auth.persistence.repository.UserEntityRepository;
import com.casadedios.backend.modules.auth.service.UserEntityService;
import com.casadedios.backend.common.enums.GenderEnum;
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
        validateUniqueUsername(authUserRegisterRequestDto.username());
        validateUniqueEmail(authUserRegisterRequestDto.email());
        validatePastorGenderLimit(authUserRegisterRequestDto.gender());

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

    private void validateUniqueUsername(String username) {
        if (userEntityRepository.existsByUsername(username)) {
            log.warn("Intento de registrar usuario con username ya existente: {}", username);
            throw new CasaDeDiosException(ApiError.DUPLICATE_USERNAME);
        }
    }

    private void validateUniqueEmail(String email) {
        if (userEntityRepository.existsByEmail(email)) {
            log.warn("Intento de registrar usuario con email ya existente: {}", email);
            throw new CasaDeDiosException(ApiError.DUPLICATE_EMAIL);
        }
    }

    private void validatePastorGenderLimit(GenderEnum gender) {
        long count = userEntityRepository.countByGender(gender);
        if (count >= 1) {
            log.warn("Intento de registrar un segundo pastor del mismo género: {}", gender);
            throw new CasaDeDiosException(ApiError.PASTOR_GENDER_LIMIT_EXCEEDED);
        }
    }
}
