package com.casadedios.backend.cellgroup.validation.leader;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CellGroupLeaderValidator implements ConstraintValidator<ValidCellGroupLeader, CellGroupLeaderValidatable> {

    @Override
    public boolean isValid(CellGroupLeaderValidatable dto, ConstraintValidatorContext context) {
        boolean isPastorCell = Boolean.TRUE.equals(dto.getIsPastorCell());

        if (isPastorCell) {
            return true;
        }

        if (dto.getLeaderDiscipleId() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El ID del líder es obligatorio para células regulares")
                    .addPropertyNode("leaderDiscipleId")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
