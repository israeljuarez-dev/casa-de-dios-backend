package com.casadedios.backend.disciple.persistence.projection;

public interface InviterProjection {
    Long getDiscipleId();   // el target (quien fue invitado)
    Long getInviterId();
    String getFirstName();
    String getLastName();
}
