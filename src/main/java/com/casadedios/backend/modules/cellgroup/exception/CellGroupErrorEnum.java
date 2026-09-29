package com.casadedios.backend.modules.cellgroup.exception;

import com.casadedios.backend.common.exception.model.ApiErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum CellGroupErrorEnum implements ApiErrorCode {
    CELL_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "La célula solicitada no existe"),
    CELL_GROUP_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "El discípulo no es miembro de esta célula"),
    CELL_GROUP_MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "El discípulo ya es miembro de esta célula"),
    DISCIPLE_NOT_A_LEADER(HttpStatus.UNPROCESSABLE_CONTENT, "El discípulo debe tener nivel espiritual LEADER para dirigir una célula"),
    PASTOR_CORE_TWELVE_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "Ya se alcanzó el límite de 12 discípulos del pastor en toda la iglesia"),
    PASTOR_CORE_TWELVE_MEMBER_MUST_BE_LEADER(HttpStatus.UNPROCESSABLE_CONTENT, "Solo los líderes de célula pueden formar parte de Los 12 del pastor"),
    DUPLICATE_CELL_GROUP_NAME(HttpStatus.CONFLICT, "Ya existe una célula registrada con ese nombre"),
    LEADER_CANNOT_BE_OWN_CELL_MEMBER(HttpStatus.UNPROCESSABLE_CONTENT, "El líder de la célula no puede ser miembro de su propia célula"),
    DISCIPLE_ALREADY_IN_ANOTHER_CELL(HttpStatus.CONFLICT, "El discípulo ya pertenece a otra célula"),
    PASTOR_CORE_TWELVE_CANNOT_BE_CELL_MEMBER(HttpStatus.CONFLICT, "Un discípulo de Los 12 del pastor no puede ser miembro regular de una célula"), // sin uso detectado
    PASTOR_GENDER_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "Ya existe un pastor registrado con ese género"),
    PASTOR_CELL_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "Ya existen las dos células principales del pastor y la pastora"),
    PASTOR_CELL_GENDER_MISMATCH(HttpStatus.UNPROCESSABLE_CONTENT, "La célula del pastor solo acepta hombres y la de la pastora solo mujeres"),
    PASTOR_CELL_MEMBER_MUST_BE_LEADER(HttpStatus.UNPROCESSABLE_CONTENT, "Solo discípulos con nivel LEADER pueden ser miembros de la célula principal"),
    PASTOR_CELL_MEMBER_LIMIT_EXCEEDED(HttpStatus.CONFLICT, "La célula principal ya alcanzó el límite de 12 miembros"),
    LEADER_REQUIRED_FOR_REGULAR_CELL(HttpStatus.UNPROCESSABLE_CONTENT, "Las células regulares requieren un líder asignado"),
    PASTOR_CELL_GENDER_REQUIRED(HttpStatus.UNPROCESSABLE_CONTENT, "Las células principales requieren especificar el género (MALE para el pastor, FEMALE para la pastora)"),
    PASTOR_CELL_GENDER_ALREADY_EXISTS(HttpStatus.CONFLICT, "Ya existe una célula principal registrada para ese género"),
    ;

    private final HttpStatusCode status;

    private final String message;

    CellGroupErrorEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}