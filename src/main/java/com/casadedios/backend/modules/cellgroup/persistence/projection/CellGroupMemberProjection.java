package com.casadedios.backend.modules.cellgroup.persistence.projection;

import java.time.LocalDate;

public interface CellGroupMemberProjection {
    Long getMemberId();
    Long getDiscipleId();
    String getFirstName();
    String getLastName();
    String getPhoneCodeNumber();
    String getPhoneNumber();
    String getSpiritualLevel();
    LocalDate getBirthDate();
    String getGender();
    boolean getIsCellGroupLeader();
    boolean getIsCoreTwelve();
    boolean getIsPastorCoreTwelve();
}
