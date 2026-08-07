package com.casadedios.backend.disciple.validation.phonenumber;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, PhoneValidatable> {

    private static final String ONLY_DIGITS_REGEX = "\\d+";
    private static final int GENERIC_MIN_LENGTH = 6;
    private static final int GENERIC_MAX_LENGTH = 15;

    @Override
    public boolean isValid(PhoneValidatable dto, ConstraintValidatorContext context) {
        // Si ambos campos son nulos, se considera válido (campos opcionales).
        // La obligatoriedad se maneja con @NotNull en el DTO si se requiere.
        if (dto.getPhoneNumber() == null && dto.getPhoneCodeNumber() == null) {
            return true;
        }

        // Si uno de los dos está presente, el otro también debe estarlo
        if (dto.getPhoneNumber() == null || dto.getPhoneCodeNumber() == null) {
            return buildMessage(
                    context,
                    "Si se registra un número de celular, el código de país también es obligatorio, y viceversa"
            );
        }

        String phoneNumber = dto.getPhoneNumber().trim();
        String phoneCode = dto.getPhoneCodeNumber().trim();

        // Regla base: solo dígitos, sin espacios ni símbolos
        if (!phoneNumber.matches(ONLY_DIGITS_REGEX)) {
            return buildMessage(
                    context,
                    "El número de celular debe contener solo dígitos, sin espacios, guiones ni el símbolo +"
            );
        }

        return PhoneRuleEnum.findByCode(phoneCode)
                .map(rule -> validateKnownCountry(rule, phoneNumber, context))
                .orElseGet(() -> validateGeneric(phoneNumber, context));
    }

    private boolean validateKnownCountry(PhoneRuleEnum  rule, String phoneNumber, ConstraintValidatorContext context) {
        if (rule.isValidNumber(phoneNumber)) {
            return true;
        }
        return buildMessage(
                context,
                "Formato de número inválido para el código de país +" + rule.getCountryCode()
                        + ". Formato esperado: " + rule.getExpectedFormat()
        );
    }

    private boolean validateGeneric(String phoneNumber, ConstraintValidatorContext context) {
        int length = phoneNumber.length();
        if (length >= GENERIC_MIN_LENGTH && length <= GENERIC_MAX_LENGTH) {
            return true;
        }
        return buildMessage(
                context,
                "El número de celular debe tener entre " + GENERIC_MIN_LENGTH
                        + " y " + GENERIC_MAX_LENGTH + " dígitos para el código de país indicado"
        );
    }

    // Reemplaza el mensaje por defecto de la anotación con uno dinámico y
    // deshabilita el mensaje default para evitar duplicados en la respuesta
    private boolean buildMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }
}
