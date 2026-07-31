package com.casadedios.backend.disciple.dto.request;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DiscipleChildUpdateRequestDto(
        Long id, // null = hijo nuevo a crear; presente = hijo existente a actualizar

        @Size(min=3, max = 150, message = "Los nombres del hijo deben tener entre 3 y 150 caracteres")
        String firstName,

        @Size(min=3, max = 150, message = "Los apellidos del hijo deben tener entre 3 y 15 150 caracteres")
        String lastName,

        @Past(message = "La fecha de nacimiento debe ser en tiempo pasado")
        LocalDate birthDate
) {}

