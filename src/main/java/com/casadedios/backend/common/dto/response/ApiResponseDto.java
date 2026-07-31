package com.casadedios.backend.common.dto.response;

import lombok.Builder;

@Builder
public record ApiResponseDto<T>(
        int status,
        String message,
        T data,
        boolean success
) {
    public static <T> ApiResponseDto<T> success(int status, String message, T data) {
        return ApiResponseDto.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .success(true)
                .build();
    }
}
