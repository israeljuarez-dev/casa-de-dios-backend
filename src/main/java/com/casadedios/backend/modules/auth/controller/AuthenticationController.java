package com.casadedios.backend.modules.auth.controller;

import com.casadedios.backend.common.dto.response.ApiResponseBodyDto;
import com.casadedios.backend.modules.auth.dto.request.AuthLoginRequestDto;
import com.casadedios.backend.modules.auth.dto.response.AuthLoginResponseDto;
import com.casadedios.backend.modules.auth.service.impl.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class AuthenticationController {

    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseBodyDto<AuthLoginResponseDto>> login(
            @RequestBody @Valid AuthLoginRequestDto loginRequest
    ) {
        AuthLoginResponseDto result = userDetailsService.login(loginRequest);
        return ResponseEntity.ok(new ApiResponseBodyDto<>("Inicio de sesión exitoso", result));
    }
}

