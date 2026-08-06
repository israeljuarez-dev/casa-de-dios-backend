package com.casadedios.backend.auth.controller;

import com.casadedios.backend.auth.controller.documentation.UserEntityControllerDocumentation;
import com.casadedios.backend.auth.dto.request.AuthUserRegisterRequestDto;
import com.casadedios.backend.auth.dto.response.AuthUserEntityProfileResponseDto;
import com.casadedios.backend.auth.dto.response.AuthUserRegisterResponseDto;
import com.casadedios.backend.auth.service.UserEntityService;
import com.casadedios.backend.common.dto.response.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/me")
    @Override
    public ResponseEntity<ApiResponseDto<AuthUserEntityProfileResponseDto>> me(@AuthenticationPrincipal String username) {
        AuthUserEntityProfileResponseDto profile = userEntityService.findCurrentUser(username);
        return ResponseEntity.ok(
                ApiResponseDto.success(HttpStatus.OK.value(), "Perfil del usuario autenticado", profile)
        );
    }
}
