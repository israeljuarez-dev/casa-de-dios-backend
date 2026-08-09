package com.casadedios.backend.disciple.dto.response;

import com.casadedios.backend.common.enums.GenderEnum;
import com.casadedios.backend.disciple.enums.MaritalStatus;
import com.casadedios.backend.disciple.enums.SpiritualLevel;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder(toBuilder = true)
public record DiscipleResponseDto(
        Long id,

        String firstName,

        String lastName,

        GenderEnum gender,

        LocalDate birthDate,

        Integer age,

        String occupation,

        String phoneCodeNumber,

        String phoneNumber,

        String address,

        String dni,

        MaritalStatus maritalStatus,

        String coupleName,

        SpiritualLevel spiritualLevel,

        boolean isLeader,

        boolean hasChildren,

        List<DiscipleChildResponseDto> children,

        BirthdayAlertDto birthdayAlert,

        DiscipleInviterResponseDto invitedBy,

        List<DiscipleParentResponseDto> parents
) {}