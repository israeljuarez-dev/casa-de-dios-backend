package com.casadedios.backend.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AuthLoginRequestDto(
        @NotBlank (message = "El usuario o correo electrónico es obligatorio")
        @Size(max = 50, message = "El usuario o correo electrónico no puede superar los 150 caracteres")
        String usernameOrEmail,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(max = 255, message = "La contraseña no puede superar los 100 caracteres")
        String password
) {}
