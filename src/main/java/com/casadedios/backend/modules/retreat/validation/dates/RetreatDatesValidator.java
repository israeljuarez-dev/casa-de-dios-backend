package com.casadedios.backend.modules.retreat.validation.dates;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;

public class RetreatDatesValidator implements ConstraintValidator<ValidRetreatDates, RetreatDatesValidatable> {

    @Override
    public boolean isValid(RetreatDatesValidatable dto, ConstraintValidatorContext context) {
        Instant startDate = dto.getStartDate();
        Instant endDate = dto.getEndDate();

        if (startDate == null || endDate == null) {
            return true;
        }

        if (!endDate.isAfter(startDate)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("La fecha de fin debe ser posterior a la fecha de inicio")
                    .addPropertyNode("endDate")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
