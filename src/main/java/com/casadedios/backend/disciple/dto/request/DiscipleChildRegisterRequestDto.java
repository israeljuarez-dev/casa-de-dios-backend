package com.casadedios.backend.disciple.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DiscipleChildRegisterRequestDto(
        @NotBlank(message = "Los nombres del hijo son obligatorios")
        @Size(max = 150, message = "Los nombres del hijo no pueden superar los 150 caracteres")
        String firstName,

        @NotBlank(message = "Los apellidos del hijo son obligatorios")
        @Size(max = 150, message = "Los apellidos del hijo no pueden superar los 150 caracteres")
        String lastName,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser en tiempo pasado")
        LocalDate birthDate
) {}
