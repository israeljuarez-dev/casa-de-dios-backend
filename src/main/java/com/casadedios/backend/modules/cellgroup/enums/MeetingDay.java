package com.casadedios.backend.modules.cellgroup.enums;

import lombok.Getter;

@Getter
public enum MeetingDay {
    MONDAY("Lunes"),
    TUESDAY("Martes"),
    WEDNESDAY("Miércoles"),
    FRIDAY("Viernes"),
    SATURDAY("Sábado");

    private final String daySpanishName;

    MeetingDay(String daySpanishName) {
        this.daySpanishName = daySpanishName;
    }
}
