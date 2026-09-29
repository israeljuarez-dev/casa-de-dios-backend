package com.casadedios.backend.modules.retreat.persistence.projection;

import java.time.Instant;

public interface RetreatSummaryProjection {
    Long getId();
    String getName();
    String getLocation();
    Instant getStartDate();
    Instant getEndDate();
    int getEnrolledCount();
    int getStaffCount();
}
