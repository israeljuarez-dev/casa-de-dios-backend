package com.casadedios.backend.disciple.validation.phonenumber;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target(ElementType.TYPE)   // Anotación a nivel de clase
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {
    String message() default "Número de celular inválido para el código de país indicado";

    Class<? extends Payload>[] payload() default {};
}
