package com.casadedios.backend.common.dto.response;

import lombok.Builder;

@Builder
public record ApiResponseBodyDto<T>(
        String message,
        T data
) {
}
