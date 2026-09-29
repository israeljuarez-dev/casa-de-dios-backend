package com.casadedios.backend.modules.retreat.persistence.projection;

public interface RetreatStaffProjection {
    Long getId();
    Long getDiscipleId();
    String getFirstName();
    String getLastName();
    String getPhoneCodeNumber();
    String getPhoneNumber();
}