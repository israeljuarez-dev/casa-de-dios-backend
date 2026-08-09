package com.casadedios.backend.disciple.dto.request;

import com.casadedios.backend.common.enums.GenderEnum;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DiscipleChildUpdateRequestDto(
        Long id, // null = hijo nuevo a crear; presente = hijo existente a actualizar

        @Size(max = 150, message = "Los nombres del hijo no deben superar los 150 caracteres")
        String firstName,

        @Size(max = 150, message = "Los apellidos del hijo no deben superar los 150 caracteres")
        String lastName,

        GenderEnum gender,

        @Past(message = "La fecha de nacimiento debe ser en tiempo pasado")
        LocalDate birthDate
) {}

