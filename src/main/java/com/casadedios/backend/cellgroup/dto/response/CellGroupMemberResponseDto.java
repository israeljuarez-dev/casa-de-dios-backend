package com.casadedios.backend.cellgroup.dto.response;

import com.casadedios.backend.common.enums.GenderEnum;
import com.casadedios.backend.disciple.dto.response.BirthdayAlertDto;
import com.casadedios.backend.disciple.enums.SpiritualLevel;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CellGroupMemberResponseDto(
        Long memberId,
        Long discipleId,
        String firstName,
        String lastName,
        String phoneCodeNumber,
        String phoneNumber,
        SpiritualLevel spiritualLevel,
        LocalDate birthDate,
        Integer age,
        GenderEnum gender,
        boolean isCellGroupLeader,
        boolean isCoreTwelve,
        boolean isPastorCoreTwelve,
        BirthdayAlertDto birthdayAlert
) {}