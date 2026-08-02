package com.casadedios.backend.disciple.enums;

public enum MaritalStatus {
    SINGLE("Soltero/a"),
    MARRIED("Casado/a"),
    DIVORCED("Divorciado/a"),
    WIDOWED("Viudo/a"),
    COHABITING("Conviviente");

    private final String displayName;

    MaritalStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

