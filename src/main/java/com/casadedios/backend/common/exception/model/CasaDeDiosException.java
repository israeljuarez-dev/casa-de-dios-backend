package com.casadedios.backend.common.exception.model;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@Getter
public class CasaDeDiosException extends RuntimeException{

    // Código de estado a responder
    private final HttpStatusCode status;

    // Título del error
    private final String description;

    // Razones del error
    private final List<String> reasons;

    public CasaDeDiosException(ApiErrorCode error) {
        super(error.getMessage());
        this.status = error.getStatus();
        this.description = error.getMessage();
        this.reasons = List.of();
    }

    public CasaDeDiosException(ApiErrorCode error, List<String> reasons) {
        super(error.getMessage());
        this.status = error.getStatus();
        this.description = error.getMessage();
        this.reasons = reasons;
    }
}

