package com.casadedios.backend.auth.controller.documentation;

import com.casadedios.backend.auth.dto.request.AuthUserRegisterRequestDto;
import com.casadedios.backend.auth.dto.response.AuthUserEntityProfileResponseDto;
import com.casadedios.backend.auth.dto.response.AuthUserRegisterResponseDto;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Registro", description = "Registro de usuario")
public interface UserEntityControllerDocumentation {

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea una cuenta de acceso (pastor/pastora). El rol se asigna automáticamente como PASTOR.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuario creado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos inválidos (username, email o contraseña no cumplen el formato requerido)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),

            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Registro",
                                    summary = "Ejemplo de registro de usuario",
                                    value = """
                                    {
                                      "username": "pastor.juan",
                                      "email": "pastor.juan@casadedios.com",
                                      "password": "MiClaveSegura123!"
                                    }
                                    """
                            )
                    )
            )
    )
    @SecurityRequirements
    ResponseEntity<ApiResponseDto<AuthUserRegisterResponseDto>>  register(@RequestBody @Valid AuthUserRegisterRequestDto request);

    @Operation(
            summary = "Obtener perfil del usuario autenticado",
            description = """
                    Devuelve los datos del pastor actualmente autenticado.
                    El usuario se identifica a partir del JWT, no de un parámetro en la URL.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Perfil obtenido exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token inválido o ausente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    ResponseEntity<ApiResponseDto<AuthUserEntityProfileResponseDto>> me(@AuthenticationPrincipal String username);
}
