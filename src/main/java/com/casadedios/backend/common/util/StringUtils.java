package com.casadedios.backend.common.util;

public final class StringUtils {

    private StringUtils() {}

    public static String blankToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isBlank() ? null : trimmed;
    }
}
