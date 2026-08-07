package com.casadedios.backend.common.exception;

import com.casadedios.backend.common.exception.dto.ErrorDto;
import com.casadedios.backend.common.exception.enums.ApiError;
import com.casadedios.backend.common.exception.model.CasaDeDiosException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @NullUnmarked
    protected @Nullable ResponseEntity<@NonNull Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException exception,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        // Field errors: errores sobre campos individuales (@NotBlank, @Size, etc.)
        List<String> fieldReasons = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError ->
                        "%s - %s".formatted(
                                fieldError.getField(),
                                Objects.requireNonNullElse(fieldError.getDefaultMessage(), "Sin descripción")
                        )
                )
                .toList();

        // Global errors: errores de anotaciones a nivel de clase (@ValidPhone, etc.)
        List<String> globalReasons = exception.getBindingResult().getGlobalErrors().stream()
                .map(globalError ->
                        Objects.requireNonNullElse(globalError.getDefaultMessage(), "Sin descripción")
                )
                .toList();

        List<String> reasons = Stream.concat(fieldReasons.stream(), globalReasons.stream()).toList();

        log.warn("Errores de validación en el request: {}", reasons);

        return ResponseEntity
                .status(ApiError.VALIDATION_ERROR.getStatus())
                .body(new ErrorDto(ApiError.VALIDATION_ERROR.getMessage(), reasons));
    }

    @ExceptionHandler(CasaDeDiosException.class)
    public ResponseEntity<ErrorDto> handleCasaDeDiosException(CasaDeDiosException exception) {
        log.warn("Error de negocio: {}", exception.getDescription());
        return ResponseEntity
                .status(exception.getStatus())
                .body(new ErrorDto(exception.getDescription(), exception.getReasons()));
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorDto> handleAuthenticationErrors(RuntimeException exception) {
        log.warn("Intento de autenticación fallido: {}", exception.getMessage());
        return ResponseEntity
                .status(ApiError.INVALID_CREDENTIALS.getStatus())
                .body(new ErrorDto(ApiError.INVALID_CREDENTIALS.getMessage(), List.of()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException exception) {
        List<String> reasons = exception.getConstraintViolations().stream()
                .map(violation -> "%s - %s".formatted(violation.getPropertyPath(), violation.getMessage()))
                .toList();

        log.warn("Errores de validación: {}", reasons);

        return ResponseEntity
                .status(ApiError.VALIDATION_ERROR.getStatus())
                .body(new ErrorDto(ApiError.VALIDATION_ERROR.getMessage(), reasons));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleUnexpectedException(Exception exception) {
        log.error("Error inesperado no controlado", exception);
        return ResponseEntity
                .status(ApiError.INTERNAL_ERROR.getStatus())
                .body(new ErrorDto(ApiError.INTERNAL_ERROR.getMessage(), List.of()));
    }
}
