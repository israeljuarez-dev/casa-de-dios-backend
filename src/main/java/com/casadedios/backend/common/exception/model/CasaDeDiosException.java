package com.casadedios.backend.common.exception.model;

import com.casadedios.backend.common.exception.enums.ApiError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class CasaDeDiosException extends RuntimeException{

    // Código de estado a responder
    private HttpStatus status;

    // Título del error
    private String description;

    // Razones del error
    private List<String> reasons;

    public CasaDeDiosException(ApiError error) {
        super(error.getMessage());
        this.status = error.getStatus();
        this.description = error.getMessage();
        this.reasons = List.of();
    }

    public CasaDeDiosException(ApiError error, List<String> reasons) {
        super(error.getMessage());
        this.status = error.getStatus();
        this.description = error.getMessage();
        this.reasons = reasons;
    }
}

