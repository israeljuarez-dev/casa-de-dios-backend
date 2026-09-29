package com.casadedios.backend.modules.retreat.validation.dates;

import java.time.Instant;

public interface RetreatDatesValidatable {
    Instant getStartDate();
    Instant getEndDate();
}