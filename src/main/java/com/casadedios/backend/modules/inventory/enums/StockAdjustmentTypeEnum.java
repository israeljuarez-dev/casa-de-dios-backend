package com.casadedios.backend.modules.inventory.enums;

import lombok.Getter;

@Getter
public enum StockAdjustmentTypeEnum {
    INCREASE("Ingreso", 1),
    DECREASE("Salida", -1);

    private final String displayName;

    private final int sign;

    StockAdjustmentTypeEnum(String displayName, int sign) {
        this.displayName = displayName;
        this.sign = sign;
    }

    public int toSignedQuantity(int quantity) {
        return sign * quantity;
    }
}
