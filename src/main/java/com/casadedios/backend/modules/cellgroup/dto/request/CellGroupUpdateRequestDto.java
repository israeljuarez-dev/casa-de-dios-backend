package com.casadedios.backend.modules.cellgroup.dto.request;

import com.casadedios.backend.modules.cellgroup.enums.MeetingDay;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record CellGroupUpdateRequestDto(

        @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
        String name,

        Long leaderDiscipleId,

        MeetingDay meetingDay,

        LocalTime meetingTime,

        @Size(max = 255, message = "El lugar no puede superar los 255 caracteres")
        String location,

        Boolean isPastorCell
) {}
