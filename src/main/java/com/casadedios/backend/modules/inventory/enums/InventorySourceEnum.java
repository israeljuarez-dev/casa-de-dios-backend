package com.casadedios.backend.modules.inventory.enums;

import lombok.Getter;

@Getter
public enum InventorySourceEnum {
    DONATED("Donado"),
    PURCHASED("Comprado");

    private final String displayName;

    InventorySourceEnum(String displayName) {
        this.displayName = displayName;
    }
}
