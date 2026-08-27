package com.casadedios.backend.cellgroup.validation.leader;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CellGroupLeaderValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCellGroupLeader {
    String message() default "El líder es obligatorio para células regulares";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
