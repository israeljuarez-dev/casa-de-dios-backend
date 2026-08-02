package com.casadedios.backend.disciple.enums;

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
    LEADER("Líder"),
    CELL_LEADER("Líder de Célula"),
    LEADERSHIP_SCHOOL_TEACHER("Profesor de Escuela de Líderes");

    private final String displayName;

    SpiritualLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isLeaderEligible() {
        return switch (this) {
            case LEADER,
                 CELL_LEADER,
                 LEADERSHIP_SCHOOL_TEACHER -> true;
            case GUEST,
                 PRE_RETREAT,
                 RETREAT,
                 POST_RETREAT,
                 LEADERSHIP_SCHOOL_1,
                 LEADERSHIP_SCHOOL_2,
                 LEADERSHIP_SCHOOL_3,
                 LEADERSHIP_SCHOOL_4,
                 LEADERSHIP_SCHOOL_5,
                 LEADERSHIP_SCHOOL_6 -> false;
        };
    }
}