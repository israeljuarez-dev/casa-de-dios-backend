package com.casadedios.backend.cellgroup.persistence.projection;

public interface CellGroupSummaryProjection {
    Long getId();
    String getName();
    Long getLeaderId();
    String getLeaderFirstName();
    String getLeaderLastName();
    String getMeetingDay();
    java.time.LocalTime getMeetingTime();
    String getLocation();
    Boolean getIsPastorCell();
    String getPastorCellGender();
    int getMemberCount();
}
