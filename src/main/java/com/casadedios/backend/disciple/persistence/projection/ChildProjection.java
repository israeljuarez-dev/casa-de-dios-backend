package com.casadedios.backend.disciple.persistence.projection;

public interface ChildProjection {
    Long getParentId();
    Long getChildId();
    String getFirstName();
    String getLastName();
    String getGender();
    java.time.LocalDate getBirthDate();
}
