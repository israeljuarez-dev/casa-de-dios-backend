package com.casadedios.backend.auth.dto.request;

import com.casadedios.backend.common.enums.GenderEnum;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record AuthUserRegisterRequestDto(
        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
        String firstName,

        @NotBlank(message = "El apellido no puede estar vacío")
        @Size(max = 150, message = "El apellido no puede superar los 150 caracteres")
        String lastName,

        @NotNull(message = "El género es requerido")
        GenderEnum gender,

        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
        String username,

        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "El correo electrónico no es válido")
        @Size(min = 3, max = 50, message = "El  correo electrónico no puede superar los 150 caracteres")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 255, message = "La contraseña debe tener entre 8 y 255 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,100}$",
                message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial"
        )
        String password
) {}
