package com.casadedios.backend.auth.controller;

import com.casadedios.backend.auth.controller.documentation.AuthenticationControllerDocumentation;
import com.casadedios.backend.auth.dto.request.AuthLoginRequestDto;
import com.casadedios.backend.auth.dto.response.AuthLoginResponseDto;
import com.casadedios.backend.auth.service.impl.UserDetailsServiceImpl;
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
public class AuthenticationController implements AuthenticationControllerDocumentation {

    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    @Override
    public ResponseEntity<ApiResponseDto<AuthLoginResponseDto>> login(@RequestBody @Valid AuthLoginRequestDto loginRequest) {
        AuthLoginResponseDto result = userDetailsService.login(loginRequest);
        return ResponseEntity.ok(ApiResponseDto.success(HttpStatus.OK.value(), "Inicio de sesión exitoso", result));
    }
}

