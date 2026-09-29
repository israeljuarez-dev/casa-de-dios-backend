package com.casadedios.backend.modules.retreat.persistence.projection;

import java.math.BigDecimal;

public interface RetreatEnrollmentProjection {
    Long getEnrollmentId();
    Long getDiscipleId();
    String getFirstName();
    String getLastName();
    String getPhoneCodeNumber();
    String getPhoneNumber();
    String getPaymentStatus();
    BigDecimal getAmountPaid();
}
