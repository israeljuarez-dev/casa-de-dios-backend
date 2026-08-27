package com.casadedios.backend.cellgroup.dto.request;

import com.casadedios.backend.cellgroup.enums.MeetingDay;
import com.casadedios.backend.cellgroup.validation.leader.CellGroupLeaderValidatable;
import com.casadedios.backend.cellgroup.validation.leader.ValidCellGroupLeader;
import com.casadedios.backend.common.enums.GenderEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalTime;

@Builder
@ValidCellGroupLeader
public record CellGroupRegisterRequestDto(

        @NotBlank(message = "El nombre de la célula es obligatorio")
        @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
        String name,

        Long leaderDiscipleId,

        MeetingDay meetingDay,

        LocalTime meetingTime,

        @Size(max = 255, message = "El lugar no puede superar los 255 caracteres")
        String location,

        Boolean isPastorCell,

        GenderEnum pastorCellGender

) implements CellGroupLeaderValidatable {

        @Override
        public Boolean getIsPastorCell() {
                return isPastorCell;
        }

        @Override
        public Long getLeaderDiscipleId() {
                return leaderDiscipleId;
        }
}
