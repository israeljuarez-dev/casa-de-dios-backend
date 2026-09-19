package com.casadedios.backend.modules.cellgroup.dto.response;

import com.casadedios.backend.modules.cellgroup.enums.MeetingDay;
import com.casadedios.backend.common.enums.GenderEnum;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record CellGroupResponseDto(
        Long id,
        String name,
        CellGroupLeaderResponseDto leader,
        MeetingDay meetingDay,
        String meetingDaySpanishName,
        LocalTime meetingTime,
        String location,
        boolean isPastorCell,
        GenderEnum pastorCellGender,
        int memberCount
) {}