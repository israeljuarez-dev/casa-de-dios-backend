package com.casadedios.backend.modules.inventory.persistence.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface InventoryItemSummaryProjection {
    Long getId();
    String getName();
    String getDescription();
    BigDecimal getCost();
    int getQuantity();
    String getCategory();
    String getSourceType();
    Long getDonorId();
    String getDonorFirstName();
    String getDonorLastName();
    LocalDate getRegisteredAt();
}
