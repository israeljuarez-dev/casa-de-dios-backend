package com.casadedios.backend.disciple.controller.documentation;

import com.casadedios.backend.common.dto.response.ApiResponseDto;
import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.common.exception.dto.ErrorDto;
import com.casadedios.backend.disciple.dto.request.DiscipleRegisterRequestDto;
import com.casadedios.backend.disciple.dto.request.DiscipleSearchCriteriaDto;
import com.casadedios.backend.disciple.dto.request.DiscipleUpdateRequestDto;
import com.casadedios.backend.disciple.dto.response.DiscipleResponseDto;
import com.casadedios.backend.disciple.enums.MaritalStatus;
import com.casadedios.backend.disciple.enums.SpiritualLevel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Discípulos", description = "Gestión de discípulos: registro, edición, consulta y eliminación")
public interface DiscipleControllerDocumentation {

    @Operation(
            summary = "Listar discípulos",
            description = "Devuelve un listado paginado de discípulos con filtros dinámicos opcionales.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado obtenido exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    )
            }
    )
    @Parameters({
            @Parameter(
                    name = "firstName",
                    description = "Filtro por nombres del discípulo (búsqueda parcial)",
                    example = "Heinz",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "lastName",
                    description = "Filtro por apellidos del discípulo (búsqueda parcial)",
                    example = "Juárez",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "spiritualLevel",
                    description = "Filtro por nivel espiritual",
                    example = "GUEST",
                    schema = @Schema(implementation = SpiritualLevel.class),
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "maritalStatus",
                    description = "Filtro por estado civil",
                    schema = @Schema(implementation = MaritalStatus.class),
                    example = "SINGLE",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "isLeader",
                    description = "Filtro por si el discípulo es líder",
                    example = "false",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "page",
                    description = "Número de página (0-indexed)",
                    example = "0",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "size",
                    description = "Cantidad de resultados por página",
                    example = "10",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "sortField",
                    description = "Campo por el cual ordenar los resultados",
                    example = "lastName",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "sortDirection",
                    description = "Dirección de orden: ASC o DESC",
                    example = "ASC",
                    in = ParameterIn.QUERY
            )
    })
    ResponseEntity<ApiResponseDto<PaginationResponseDto<DiscipleResponseDto>>> findAll(@ModelAttribute DiscipleSearchCriteriaDto criteria);

    @Operation(
            summary = "Obtener un discípulo por id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Discípulo encontrado",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El discípulo no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    @Parameter(name = "id", description = "Id del discípulo", example = "1", in = ParameterIn.PATH)
    ResponseEntity<ApiResponseDto<DiscipleResponseDto>> findById(@PathVariable @Min(1) Long id);

    @Operation(
            summary = "Registrar un nuevo discípulo",
            description = """
            Crea un discípulo, opcionalmente con sus hijos y su invitador. isLeader debe ser coherente
            con spiritualLevel: solo puede (y debe) ser true cuando spiritualLevel es LEADER, CELL_LEADER
            o LEADERSHIP_SCHOOL_TEACHER. coupleName es obligatorio únicamente cuando maritalStatus es MARRIED.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Discípulo creado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = """
                            Datos inválidos (validaciones de formato, coupleName faltante, 
                            o isLeader inconsistente con spiritualLevel)
                            """,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El discípulo invitador (invitedByDiscipleId) no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "DNI o teléfono ya registrado",
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
                                    name = "Registro de discípulo",
                                    summary = "Ejemplo de registro con hijos e invitador",
                                    value = """
                                            {
                                              "firstName": "Heinz",
                                              "lastName": "Juárez",
                                              "birthDate": "1994-07-27",
                                              "occupation": "Economista",
                                              "phoneNumber": "901112126",
                                              "address": "Rusia",
                                              "dni": "70100033",
                                              "maritalStatus": "SINGLE",
                                              "spiritualLevel": "GUEST",
                                              "isLeader": false,
                                              "children": [
                                                {
                                                  "firstName": "Mateo",
                                                  "lastName": "Juárez",
                                                  "birthDate": "2020-03-15"
                                                }
                                              ],
                                              "invitedByDiscipleId": 1
                                            }
                                            """
                            )
                    )
            )
    )
    ResponseEntity<ApiResponseDto<DiscipleResponseDto>>  create(@RequestBody @Valid DiscipleRegisterRequestDto request);

    @Operation(
            summary = "Actualizar un discípulo existente",
            description = """
            Actualiza parcialmente un discípulo. Los campos no enviados conservan su valor actual.
            Si se envía maritalStatus o isLeader, se revalidan las reglas de coupleName y coherencia
            con spiritualLevel sobre el estado final resultante.
            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Discípulo actualizado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = """
                            Datos inválidos (validaciones de formato, coupleName faltante,
                            o isLeader inconsistente con spiritualLevel)
                            """,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El discípulo no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "DNI o teléfono ya usado por otro registro",
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
                                    name = "Actualización de discípulo",
                                    summary = "Ejemplo de actualización parcial",
                                    value = """
                                            {
                                              "phoneNumber": "901999888",
                                              "spiritualLevel": "CELL_LEADER",
                                              "isLeader": true
                                            }
                                            """
                            )
                    )
            )
    )
    @Parameter(name = "id", description = "Id del discípulo", example = "1", in = ParameterIn.PATH)
    ResponseEntity<ApiResponseDto<DiscipleResponseDto>>  update(
            @PathVariable @Min(1) @Parameter(description = "Id del discípulo a actualizar", example = "1") Long id,
            @RequestBody @Valid DiscipleUpdateRequestDto request
    );

    @Operation(
            summary = "Eliminar un discípulo",
            description = """
                    Realiza un borrado lógico (soft delete): el discípulo deja de ser visible en las consultas,
                    pero no se elimina físicamente de la base de datos. El DNI y el teléfono quedan reservados
                    y no pueden reutilizarse.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Discípulo eliminado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El discípulo no existe o ya fue eliminado previamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    @Parameter(name = "id", description = "Id del discípulo", example = "1", in = ParameterIn.PATH)
    ResponseEntity<ApiResponseDto<Void>> softDeleteById(@PathVariable @Min(1) Long id);

    @Operation(
            summary = "Exportar discípulos a Excel",
            description = "Genera un archivo Excel con los discípulos filtrados según los criterios de búsqueda. " +
                    "Soporta los mismos filtros que el listado (firstName, lastName, spiritualLevel, maritalStatus, isLeader).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Archivo Excel generado exitosamente",
                            content = @Content(
                                    mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validación fallida en los criterios de búsqueda",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error al generar el archivo Excel",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    @Parameters({
            @Parameter(
                    name = "firstName",
                    description = "Filtro por nombres del discípulo",
                    example = "Heinz",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "lastName",
                    description = "Filtro por apellidos del discípulo",
                    example = "Juárez",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "spiritualLevel",
                    description = "Filtro por nivel espiritual",
                    example = "GUEST",
                    schema = @Schema(implementation = SpiritualLevel.class),
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "maritalStatus",
                    description = "Filtro por estado civil",
                    example = "SINGLE",
                    schema = @Schema(implementation = MaritalStatus.class),
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "isLeader",
                    description = "Filtro por si el discípulo es líder de célula",
                    example = "false",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "page",
                    description = "Número de página (0-indexed)",
                    example = "0",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "size",
                    description = "Cantidad de resultados por página",
                    example = "10",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "sortField",
                    description = "Campo por el cual ordenar los resultados",
                    example = "lastName",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "sortDirection",
                    description = "Dirección de orden: ASC o DESC",
                    example = "ASC",
                    in = ParameterIn.QUERY
            )
    })
    ResponseEntity<byte[]> exportToExcel(@ModelAttribute DiscipleSearchCriteriaDto criteria);
}
