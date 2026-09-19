package com.casadedios.backend.modules.disciple.enums;

import lombok.Getter;

@Getter
public enum SpiritualLevel {
    GUEST("Invitado"),
    PRE_RETREAT("Pre-encuentro"),
    RETREAT("Encuentro"),
    POST_RETREAT("Post-encuentro"),
    LEADERSHIP_SCHOOL_1("Escuela de Líderes 1"),
    LEADERSHIP_SCHOOL_2("Escuela de Líderes 2"),
    LEADERSHIP_SCHOOL_3("Escuela de Líderes 3"),
    LEADERSHIP_SCHOOL_4("Escuela de Líderes 4"),
    LEADERSHIP_SCHOOL_5("Escuela de Líderes 5"),
    LEADERSHIP_SCHOOL_6("Escuela de Líderes 6"),
    LEADER("Líder");

    private final String displayName;

    SpiritualLevel(String displayName) {
        this.displayName = displayName;
    }

    public boolean isLeaderEligible() {
        return this == LEADER;
    }
}