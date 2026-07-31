package com.casadedios.backend.common.exception.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record ErrorDto(
        String message,
        List<String> reasons,
        Instant timestamp
) {
    public ErrorDto(String message, List<String> reasons) {
        this(message, reasons, Instant.now());
    }
}

