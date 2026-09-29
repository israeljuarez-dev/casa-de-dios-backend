package com.casadedios.backend.common.response;

import com.casadedios.backend.common.dto.response.ApiResponseBodyDto;
import com.casadedios.backend.common.dto.response.ApiResponseDto;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(
            @NonNull MethodParameter returnType,
            @NonNull  Class<? extends HttpMessageConverter<?>> converterType
    ) {
        ResolvableType type = ResolvableType.forMethodParameter(returnType);

        if (ApiResponseBodyDto.class.isAssignableFrom(type.toClass())) {
            return true;
        }

        if (ResponseEntity.class.isAssignableFrom(type.toClass())) {
            ResolvableType bodyType = type.getGeneric(0);

            return ApiResponseBodyDto.class.isAssignableFrom(
                    bodyType.toClass()
            );
        }

        return false;
    }

    @Override
    public @Nullable Object beforeBodyWrite(
            @Nullable Object body,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response
    ) {
        if (!(body instanceof ApiResponseBodyDto<?>(String message, Object data))) {
            return body;
        }

        int status = getStatus(response);

        return ApiResponseDto.success(status, message, data);
    }

    private int getStatus(ServerHttpResponse response) {
        if (response instanceof ServletServerHttpResponse servletResponse) {
            return servletResponse.getServletResponse().getStatus();
        }

        return 200;
    }
}
