package com.casadedios.backend.disciple.enums;

public enum SpiritualLevel {
    GUEST,
    PRE_RETREAT,
    RETREAT,
    POST_RETREAT,
    LEADERSHIP_SCHOOL_1,
    LEADERSHIP_SCHOOL_2,
    LEADERSHIP_SCHOOL_3,
    LEADERSHIP_SCHOOL_4,
    LEADERSHIP_SCHOOL_5,
    LEADERSHIP_SCHOOL_6,
    LEADER,
    CELL_LEADER,
    LEADERSHIP_SCHOOL_TEACHER;

    /**
     * Indica si este nivel espiritual habilita al discípulo para ser marcado
     * como líder (isLeader = true).
     */
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