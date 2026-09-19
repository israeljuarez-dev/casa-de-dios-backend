package com.casadedios.backend.modules.disciple.persistence.projection;

public interface ParentProjection {
    Long getChildId();    // el target (el hijo)
    Long getParentId();   // el source (el padre/madre)
    String getFirstName();
    String getLastName();
    String getGender();
}
