package com.casadedios.backend.common.exception.enums;

import com.casadedios.backend.common.exception.model.ApiErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum CommonError implements ApiErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Los datos enviados no son válidos"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado, inténtalo más tarde"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Debes iniciar sesión primero."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "La sesión no es válida, vuelve a iniciar sesión"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "La sesión ha expirado, vuelve a iniciar sesión"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "No tienes permisos para realizar esta acción"),
    EXCEL_EXPORT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Error al generar el archivo Excel"),
    ;

    private final HttpStatusCode status;

    private final String message;

    CommonError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
