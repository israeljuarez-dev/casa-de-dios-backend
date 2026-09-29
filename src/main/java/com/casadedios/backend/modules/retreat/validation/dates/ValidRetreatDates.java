package com.casadedios.backend.modules.retreat.validation.dates;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RetreatDatesValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRetreatDates {
    String message() default "La fecha de fin debe ser posterior a la fecha de inicio";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}