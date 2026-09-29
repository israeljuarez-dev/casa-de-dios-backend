package com.casadedios.backend.modules.auth.controller.documentation;

import com.casadedios.backend.common.dto.response.ApiResponseBodyDto;
import com.casadedios.backend.modules.auth.dto.request.AuthUserRegisterRequestDto;
import com.casadedios.backend.modules.auth.dto.response.AuthUserEntityProfileResponseDto;
import com.casadedios.backend.modules.auth.dto.response.AuthUserRegisterResponseDto;
import com.casadedios.backend.common.dto.response.ApiResponseDto;
import com.casadedios.backend.common.exception.dto.ErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Registro", description = "Registro de usuario")
public interface UserEntityControllerDocumentation {

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea una cuenta de acceso (pastor/pastora). El rol se asigna automáticamente como PASTOR.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Registro",
                                    summary = "Ejemplo de registro de usuario",
                                    value = """
                                    {
                                      "firstName": "Willy",
                                      "lastName": "Juárez",
                                      "gender": "MALE",
                                      "username": "pastorwilly",
                                      "email": "pastorwilly@iglesia.com",
                                      "password": "miContraseña123!"
                                    }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuario creado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 201,
                                              "message": "Usuario registrado exitosamente",
                                              "success": true,
                                              "data": {
                                                "id": 1,
                                                "username": "pastorwilly",
                                                "email": "pastorwilly@iglesia.com",
                                                "role": "PASTOR"
                                              }
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos inválidos (username, email o contraseña no cumplen el formato requerido)",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Datos inválidos",
                                              "reasons": [
                                                "username - no puede estar vacío",
                                                "email - debe ser un email válido",
                                                "password - debe tener al menos 8 caracteres"
                                              ]
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = """
                                    Nombre de usuario o correo ya registrado, o ya existe un pastor
                                    registrado con ese género (solo puede haber un PASTOR hombre y una PASTOR mujer)
                                    """,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Ya existe un pastor registrado con ese género",
                                              "reasons": []
                                            }
                                            """)
                            )
                    )
            }
    )
    @SecurityRequirements
    ResponseEntity<ApiResponseBodyDto<AuthUserRegisterResponseDto>> register(@RequestBody @Valid AuthUserRegisterRequestDto request);

    @Operation(
            summary = "Obtener perfil del usuario autenticado",
            description = """
                    Devuelve los datos del pastor actualmente autenticado.
                    El usuario se identifica a partir del JWT, no de un parámetro en la URL.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Perfil del usuario autenticado",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 200,
                                              "message": "Perfil del usuario autenticado",
                                              "success": true,
                                              "data": {
                                                "id": 1,
                                                "firstName": "Willy",
                                                "lastName": "Juárez",
                                                "gender": "MALE",
                                                "username": "pastorwilly",
                                                "email": "pastorwilly@iglesia.com",
                                                "role": "PASTOR"
                                              }
                                            }
                                            """)
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
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<ApiResponseBodyDto<AuthUserEntityProfileResponseDto>> me(@AuthenticationPrincipal String username);
}
