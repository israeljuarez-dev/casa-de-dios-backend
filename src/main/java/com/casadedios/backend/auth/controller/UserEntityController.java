package com.casadedios.backend.auth.controller;

import com.casadedios.backend.auth.controller.documentation.UserEntityControllerDocumentation;
import com.casadedios.backend.auth.dto.request.AuthUserRegisterRequestDto;
import com.casadedios.backend.auth.dto.response.AuthUserRegisterResponseDto;
import com.casadedios.backend.auth.service.UserEntityService;
import com.casadedios.backend.common.dto.response.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/auth")
public class UserEntityController implements UserEntityControllerDocumentation {

    private final UserEntityService userEntityService;

    @PostMapping("/register")
    @Override
    public ResponseEntity<ApiResponseDto<AuthUserRegisterResponseDto>> register(@RequestBody @Valid AuthUserRegisterRequestDto request) {
        AuthUserRegisterResponseDto result = userEntityService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(HttpStatus.CREATED.value(), "Usuario registrado exitosamente", result));
    }
}
