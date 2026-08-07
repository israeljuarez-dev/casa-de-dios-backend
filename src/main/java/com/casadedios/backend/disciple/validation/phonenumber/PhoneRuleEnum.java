package com.casadedios.backend.disciple.validation.phonenumber;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum PhoneRuleEnum {
    PERU("51", 9, new String[]{"9"}, "9 dígitos empezando con 9 (ej: 987654321)"),

    VENEZUELA("58", 10, new String[]{"4"}, "10 dígitos empezando con 4 (ej: 4141234567)"),

    COLOMBIA("57", 10, new String[]{"3"}, "10 dígitos empezando con 3 (ej: 3101234567)"),

    BOLIVIA("591", 8, new String[]{"6", "7"}, "8 dígitos empezando con 6 o 7 (ej: 67123456)"),

    ECUADOR("593", 9, new String[]{"9"}, "9 dígitos empezando con 9 (ej: 987654321)"),

    CHILE("56", 9, new String[]{"9"}, "9 dígitos empezando con 9 (ej: 912345678)"),

    SPAIN("34", 9, new String[]{"6", "7"}, "9 dígitos empezando con 6 o 7 (ej: 612345678)"),

    ITALY("39", 10, new String[]{"3"}, "10 dígitos empezando con 3 (ej: 3201234567)"),

    SWEDEN("46", 9, new String[]{"7"}, "9 dígitos empezando con 7 (ej: 701234567)"),

    RUSSIA("7", 10, new String[]{"9"}, "10 dígitos empezando con 9 (ej: 9031234567)");

    private final String countryCode;
    private final int expectedLength;
    private final String[] validPrefixes;
    private final String expectedFormat;

    // Índice de lookup: código de país -> regla
    private static final Map<String, PhoneRuleEnum> BY_CODE = Arrays.stream(values())
            .collect(Collectors.toMap(PhoneRuleEnum::getCountryCode, Function.identity()));

    PhoneRuleEnum(String countryCode, int expectedLength, String[] validPrefixes, String expectedFormat) {
        this.countryCode = countryCode;
        this.expectedLength = expectedLength;
        this.validPrefixes = validPrefixes;
        this.expectedFormat = expectedFormat;
    }

    public static Optional<PhoneRuleEnum> findByCode(String code) {
        return Optional.ofNullable(BY_CODE.get(code));
    }

    public boolean isValidNumber(String phoneNumber) {
        if (phoneNumber.length() != expectedLength) {
            return false;
        }

        return Arrays.stream(validPrefixes).anyMatch(phoneNumber::startsWith);
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getExpectedFormat() {
        return expectedFormat;
    }
}
