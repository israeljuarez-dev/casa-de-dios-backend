package com.casadedios.backend.disciple.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DiscipleChildResponseDto(
        Long id,

        String firstName,

        String lastName,

        LocalDate birthDate,

        Integer age
) {}

