package com.casadedios.backend.modules.disciple.persistence.projection;

import java.time.LocalDate;

public interface ChildProjection {
    Long getParentId();
    Long getChildId();
    String getFirstName();
    String getLastName();
    String getGender();
    LocalDate getBirthDate();
}
