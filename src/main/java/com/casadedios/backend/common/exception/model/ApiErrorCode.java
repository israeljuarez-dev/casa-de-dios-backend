package com.casadedios.backend.common.exception.model;

import org.springframework.http.HttpStatusCode;

public interface ApiErrorCode {
    HttpStatusCode getStatus();
    String getMessage();
}
