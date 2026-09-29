package com.casadedios.backend.modules.auth.exception;

import com.casadedios.backend.common.exception.model.ApiErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum AuthErrorEnum implements ApiErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "El usuario solicitado no existe"),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "El nombre de usuario ya está registrado"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "El correo electrónico ya está registrado"),
    PASTOR_GENDER_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "Ya existe un pastor registrado con ese género"),
    ;

    private final HttpStatusCode status;

    private final String message;

    AuthErrorEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}