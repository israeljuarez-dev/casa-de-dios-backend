package com.casadedios.backend.common.enums;

import lombok.Getter;

@Getter
public enum GenderEnum {
    MALE("Masculino"),
    FEMALE("Femenino");

    private final String description;

    GenderEnum(String description) {
        this.description = description;
    }

}
