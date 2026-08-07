package com.casadedios.backend.disciple.dto.response;

import com.casadedios.backend.common.enums.GenderEnum;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DiscipleChildResponseDto(
        Long id,

        String firstName,

        String lastName,

        GenderEnum gender,

        LocalDate birthDate,

        Integer age
) {}

