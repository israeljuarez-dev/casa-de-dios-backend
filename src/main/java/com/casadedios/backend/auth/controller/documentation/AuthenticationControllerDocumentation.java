package com.casadedios.backend.auth.controller.documentation;

import com.casadedios.backend.auth.dto.request.AuthLoginRequestDto;
import com.casadedios.backend.auth.dto.response.AuthLoginResponseDto;
import com.casadedios.backend.common.dto.response.ApiResponseDto;
import com.casadedios.backend.common.exception.dto.ErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Autenticación", description = "Inicio de sesión y emisión de tokens de acceso")
public interface AuthenticationControllerDocumentation {

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario con sus credenciales y devuelve un token JWT.",
            security = {},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Inicio de sesión exitoso",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation =  ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales inválidas",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de la solicitud inválidos",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Login",
                                    summary = "Ejemplo de inicio de sesión",
                                    value = """
                                    {
                                      "usernameOrEmail": "pastor.juan",
                                      "password": "MiClaveSegura123!"
                                    }
                                    """
                            )
                    )
            )
    )
    @SecurityRequirements
    ResponseEntity<ApiResponseDto<AuthLoginResponseDto>> login(@RequestBody @Valid AuthLoginRequestDto loginRequest);
}
