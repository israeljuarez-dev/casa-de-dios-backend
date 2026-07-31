package com.casadedios.backend.common.exception.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiError {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "El usuario solicitado no existe"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Los datos enviados no son válidos"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado, inténtalo más tarde"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Debes iniciar sesión primero."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "La sesión no es válida, vuelve a iniciar sesión"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "La sesión ha expirado, vuelve a iniciar sesión"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "No tienes permisos para realizar esta acción"),
    DISCIPLE_NOT_FOUND(HttpStatus.NOT_FOUND, "El discípulo solicitado no existe"),
    DUPLICATE_NATIONAL_ID(HttpStatus.CONFLICT, "Ya existe un discípulo registrado con ese DNI"),
    DUPLICATE_DNI(HttpStatus.CONFLICT, "Ya existe un discípulo registrado con ese DNI"),
    DUPLICATE_PHONE_NUMBER(HttpStatus.CONFLICT, "Ya existe un discípulo registrado con ese número de celular"),
    INVITER_NOT_FOUND(HttpStatus.NOT_FOUND, "El discípulo que registraste como invitador no existe");

    private final HttpStatus status;
    private final String message;

    ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}