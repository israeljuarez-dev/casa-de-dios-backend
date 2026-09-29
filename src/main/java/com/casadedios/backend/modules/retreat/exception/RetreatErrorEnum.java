package com.casadedios.backend.modules.retreat.exception;

import com.casadedios.backend.common.exception.model.ApiErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum RetreatErrorEnum implements ApiErrorCode {
    RETREAT_NOT_FOUND(HttpStatus.NOT_FOUND, "El encuentro solicitado no existe"),
    RETREAT_INVALID_DATE_RANGE(HttpStatus.UNPROCESSABLE_CONTENT, "La fecha de fin debe ser posterior a la fecha de inicio"),
    RETREAT_DATES_MUST_BE_FUTURE(HttpStatus.UNPROCESSABLE_CONTENT, "Las fechas del encuentro deben ser en el futuro"),
    RETREAT_ENROLLMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "El discípulo ya está inscrito en este encuentro"),
    RETREAT_ENROLLMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "El discípulo no está inscrito en este encuentro"),
    RETREAT_STAFF_ALREADY_EXISTS(HttpStatus.CONFLICT, "El discípulo ya está registrado como staff de este encuentro"),
    RETREAT_PAYMENT_EXCEEDS_PRICE(HttpStatus.UNPROCESSABLE_CONTENT, "El monto pagado supera el costo del encuentro"),
    ;

    private final HttpStatusCode status;

    private final String message;

    RetreatErrorEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
