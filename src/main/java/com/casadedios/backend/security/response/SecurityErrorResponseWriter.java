package com.casadedios.backend.security.response;

import com.casadedios.backend.common.exception.dto.ErrorDto;
import com.casadedios.backend.common.exception.model.ApiErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.List;

public class SecurityErrorResponseWriter {
    private SecurityErrorResponseWriter() {
    }

    static void write(HttpServletResponse response, ApiErrorCode apiErrorCode, JsonMapper jsonMapper) throws IOException {
        response.setStatus(apiErrorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        ErrorDto body = new ErrorDto(apiErrorCode.getMessage(), List.of());
        response.getWriter().write(jsonMapper.writeValueAsString(body));
    }
}
