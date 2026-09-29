package com.casadedios.backend.security.response;

import com.casadedios.backend.common.exception.enums.CommonError;
import com.casadedios.backend.common.exception.model.ApiErrorCode;
import com.casadedios.backend.security.jwt.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint{

    private final JsonMapper jsonMapper;

    @Override
    public void commence(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull AuthenticationException authException
    ) throws IOException {
        Object authError = request.getAttribute(JwtAuthenticationFilter.AUTH_ERROR_ATTRIBUTE);

        ApiErrorCode apiErrorCode = switch (String.valueOf(authError)) {
            case "EXPIRED" -> CommonError.EXPIRED_TOKEN;
            case "INVALID" -> CommonError.INVALID_TOKEN;
            default -> CommonError.UNAUTHENTICATED;
        };

        SecurityErrorResponseWriter.write(response, apiErrorCode, jsonMapper);
    }
}
