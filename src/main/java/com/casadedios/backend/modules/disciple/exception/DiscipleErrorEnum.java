package com.casadedios.backend.modules.disciple.exception;

import com.casadedios.backend.common.exception.model.ApiErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum DiscipleErrorEnum implements ApiErrorCode {
    DISCIPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "El discípulo solicitado no existe"),
    DUPLICATE_NATIONAL_ID(HttpStatus.CONFLICT, "Ya existe un discípulo registrado con ese DNI"),
    DUPLICATE_DNI(HttpStatus.CONFLICT, "Ya existe un discípulo registrado con ese DNI"),
    DUPLICATE_PHONE_NUMBER(HttpStatus.CONFLICT, "Ya existe un discípulo registrado con ese número de celular"),
    INVITER_NOT_FOUND(HttpStatus.NOT_FOUND, "El discípulo que registraste como invitador no existe"),
    ;

    private final HttpStatusCode status;

    private final String message;

    DiscipleErrorEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}