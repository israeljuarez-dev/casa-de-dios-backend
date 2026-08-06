package com.casadedios.backend.auth.enums;

public enum GenderEnum {
    MALE("Masculino"),
    FEMALE("Femenino");

    private final String description;

    GenderEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
