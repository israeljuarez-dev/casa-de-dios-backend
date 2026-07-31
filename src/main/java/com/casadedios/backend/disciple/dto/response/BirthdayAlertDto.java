package com.casadedios.backend.disciple.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BirthdayAlertDto(
        boolean isToday,

        boolean isTomorrow,

        boolean wasYesterday,

        boolean withinCurrentMonth,

        boolean withinCurrentWeek,

        long daysUntilNextBirthday,

        LocalDate nextBirthday,

        String dayOfWeek
) {}

