package com.casadedios.backend.modules.disciple.controller.documentation;

import com.casadedios.backend.common.dto.response.ApiResponseBodyDto;
import com.casadedios.backend.common.dto.response.ApiResponseDto;
import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.common.exception.dto.ErrorDto;
import com.casadedios.backend.modules.disciple.dto.request.DiscipleRegisterRequestDto;
import com.casadedios.backend.modules.disciple.dto.request.DiscipleSearchCriteriaDto;
import com.casadedios.backend.modules.disciple.dto.request.DiscipleUpdateRequestDto;
import com.casadedios.backend.modules.disciple.dto.response.DiscipleResponseDto;
import com.casadedios.backend.modules.disciple.enums.MaritalStatus;
import com.casadedios.backend.modules.disciple.enums.SpiritualLevel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

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
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 200,
                                              "message": "Listado obtenido exitosamente",
                                              "success": true,
                                              "data": {
                                                "content": [
                                                  {
                                                    "id": 1,
                                                    "firstName": "Heinz",
                                                    "lastName": "Juárez",
                                                    "gender": "MALE",
                                                    "birthDate": "1994-07-27",
                                                    "age": 32,
                                                    "occupation": "Economista",
                                                    "phoneCodeNumber": "51",
                                                    "phoneNumber": "901112126",
                                                    "address": "Rusia",
                                                    "dni": "70100033",
                                                    "maritalStatus": "SINGLE",
                                                    "coupleName": null,
                                                    "spiritualLevel": "GUEST",
                                                    "isCellGroupLeader": false,
                                                    "isTeacher": false,
                                                    "isCellGroupMember": false,
                                                    "hasChildren": false,
                                                    "children": [],
                                                    "birthdayAlert": {
                                                      "isToday": false,
                                                      "isTomorrow": false,
                                                      "wasYesterday": false,
                                                      "withinCurrentMonth": false,
                                                      "withinCurrentWeek": false,
                                                      "daysUntilNextBirthday": 120,
                                                      "nextBirthday": "2027-07-27",
                                                      "dayOfWeek": "martes"
                                                    },
                                                    "invitedBy": null,
                                                    "parents": []
                                                  }
                                                ],
                                                "pageSize": 10,
                                                "totalElements": 1,
                                                "totalPages": 1
                                              }
                                            }
                                            """)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "firstName",
            description = "Filtro por nombres del discípulo (búsqueda parcial)",
            example = "Heinz",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "lastName",
            description = "Filtro por apellidos del discípulo (búsqueda parcial)",
            example = "Juárez",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "spiritualLevel",
            description = "Filtro por nivel espiritual",
            example = "GUEST",
            schema = @Schema(implementation = SpiritualLevel.class),
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "maritalStatus",
            description = "Filtro por estado civil",
            schema = @Schema(implementation = MaritalStatus.class),
            example = "SINGLE",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "isLeader",
            description = "Filtro por si el discípulo es líder",
            example = "false",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "page",
            description = "Número de página (0-indexed)",
            example = "0",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "size",
            description = "Cantidad de resultados por página",
            example = "10",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "sortField",
            description = "Campo por el cual ordenar los resultados",
            example = "lastName",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "sortDirection",
            description = "Dirección de orden: ASC o DESC",
            example = "ASC",
            in = ParameterIn.QUERY
    )
    ResponseEntity<ApiResponseBodyDto<PaginationResponseDto<DiscipleResponseDto>>> findAll(@ModelAttribute DiscipleSearchCriteriaDto criteria) ;

    @Operation(
            summary = "Obtener un discípulo por id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Discípulo encontrado",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 200,
                                              "message": "Discípulo encontrado",
                                              "success": true,
                                              "data": {
                                                "id": 1,
                                                "firstName": "Heinz",
                                                "lastName": "Juárez",
                                                "gender": "MALE",
                                                "birthDate": "1994-07-27",
                                                "age": 32,
                                                "occupation": "Economista",
                                                "phoneCodeNumber": "51",
                                                "phoneNumber": "901112126",
                                                "address": "Rusia",
                                                "dni": "70100033",
                                                "maritalStatus": "SINGLE",
                                                "coupleName": null,
                                                "spiritualLevel": "GUEST",
                                                "isCellGroupLeader": false,
                                                "isTeacher": false,
                                                "isCellGroupMember": false,
                                                "hasChildren": true,
                                                "children": [
                                                  {
                                                    "id": 8,
                                                    "firstName": "Mateo",
                                                    "lastName": "Juárez",
                                                    "gender": "MALE",
                                                    "birthDate": "2020-03-15",
                                                    "age": 6
                                                  }
                                                ],
                                                "birthdayAlert": {
                                                  "isToday": false,
                                                  "isTomorrow": false,
                                                  "wasYesterday": false,
                                                  "withinCurrentMonth": false,
                                                  "withinCurrentWeek": false,
                                                  "daysUntilNextBirthday": 120,
                                                  "nextBirthday": "2027-07-27",
                                                  "dayOfWeek": "martes"
                                                },
                                                "invitedBy": {
                                                  "id": 1,
                                                  "firstName": "Juan",
                                                  "lastName": "Pérez"
                                                },
                                                "parents": []
                                              }
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El discípulo no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "El discípulo solicitado no existe",
                                              "reasons": []
                                            }
                                            """)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "id",
            description = "Id del discípulo",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<DiscipleResponseDto>> findById(@PathVariable("id") @Min(1) Long id);

    @Operation(
            summary = "Registrar un nuevo discípulo",
            description = """
                    Crea un discípulo, opcionalmente con sus hijos y su invitador. isLeader debe ser coherente
                    con spiritualLevel: solo puede (y debe) ser true cuando spiritualLevel es LEADER, CELL_LEADER
                    o LEADERSHIP_SCHOOL_TEACHER. coupleName es obligatorio únicamente cuando maritalStatus es MARRIED.
                    """,
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
                                              "gender": "MALE",
                                              "birthDate": "1994-07-27",
                                              "occupation": "Economista",
                                              "phoneCodeNumber": "51",
                                              "phoneNumber": "901112126",
                                              "address": "Rusia",
                                              "dni": "70100033",
                                              "maritalStatus": "SINGLE",
                                              "spiritualLevel": "GUEST",
                                              "isCellGroupLeader": false,
                                              "children": [
                                                {
                                                  "firstName": "Mateo",
                                                  "lastName": "Juárez",
                                                  "gender": "MALE",
                                                  "birthDate": "2020-03-15"
                                                }
                                              ],
                                              "invitedByDiscipleId": 1
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Discípulo creado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 201,
                                              "message": "Discípulo registrado exitosamente",
                                              "success": true,
                                              "data": {
                                                "id": 1,
                                                "firstName": "Heinz",
                                                "lastName": "Juárez",
                                                "gender": "MALE",
                                                "birthDate": "1994-07-27",
                                                "age": 32,
                                                "occupation": "Economista",
                                                "phoneCodeNumber": "51",
                                                "phoneNumber": "901112126",
                                                "address": "Rusia",
                                                "dni": "70100033",
                                                "maritalStatus": "SINGLE",
                                                "coupleName": null,
                                                "spiritualLevel": "GUEST",
                                                "isCellGroupLeader": false,
                                                "isTeacher": false,
                                                "isCellGroupMember": false,
                                                "hasChildren": true,
                                                "children": [
                                                  {
                                                    "id": 8,
                                                    "firstName": "Mateo",
                                                    "lastName": "Juárez",
                                                    "gender": "MALE",
                                                    "birthDate": "2020-03-15",
                                                    "age": 6
                                                  }
                                                ],
                                                "birthdayAlert": {
                                                  "isToday": false,
                                                  "isTomorrow": false,
                                                  "wasYesterday": false,
                                                  "withinCurrentMonth": false,
                                                  "withinCurrentWeek": false,
                                                  "daysUntilNextBirthday": 120,
                                                  "nextBirthday": "2027-07-27",
                                                  "dayOfWeek": "martes"
                                                },
                                                "invitedBy": {
                                                  "id": 1,
                                                  "firstName": "Juan",
                                                  "lastName": "Pérez"
                                                },
                                                "parents": []
                                              }
                                            }
                                            """)
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
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Datos inválidos",
                                              "reasons": [
                                                "firstName - Los nombres son obligatorios",
                                                "maritalStatus - El estado civil es obligatorio"
                                              ]
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El discípulo invitador (invitedByDiscipleId) no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "El discípulo invitador no existe",
                                              "reasons": []
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "DNI o teléfono ya registrado",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Ya existe un discípulo registrado con ese DNI",
                                              "reasons": []
                                            }
                                            """)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<ApiResponseBodyDto<DiscipleResponseDto>> create(@RequestBody @Valid DiscipleRegisterRequestDto request);

    @Operation(
            summary = "Actualizar un discípulo existente",
            description = """
                    Actualiza parcialmente un discípulo. Los campos no enviados conservan su valor actual.
                    Si se envía maritalStatus o isLeader, se revalidan las reglas de coupleName y coherencia
                    con spiritualLevel sobre el estado final resultante.
                    """,
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
                                              "isCellGroupLeader": true
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Discípulo actualizado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 200,
                                              "message": "Discípulo actualizado exitosamente",
                                              "success": true,
                                              "data": {
                                                "id": 1,
                                                "firstName": "Heinz",
                                                "lastName": "Juárez",
                                                "gender": "MALE",
                                                "birthDate": "1994-07-27",
                                                "age": 32,
                                                "occupation": "Economista",
                                                "phoneCodeNumber": "51",
                                                "phoneNumber": "901999888",
                                                "address": "Rusia",
                                                "dni": "70100033",
                                                "maritalStatus": "SINGLE",
                                                "coupleName": null,
                                                "spiritualLevel": "CELL_LEADER",
                                                "isCellGroupLeader": true,
                                                "isTeacher": false,
                                                "isCellGroupMember": false,
                                                "hasChildren": true,
                                                "children": [],
                                                "birthdayAlert": {
                                                  "isToday": false,
                                                  "isTomorrow": false,
                                                  "wasYesterday": false,
                                                  "withinCurrentMonth": false,
                                                  "withinCurrentWeek": false,
                                                  "daysUntilNextBirthday": 120,
                                                  "nextBirthday": "2027-07-27",
                                                  "dayOfWeek": "martes"
                                                },
                                                "invitedBy": null,
                                                "parents": []
                                              }
                                            }
                                            """)
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
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Datos inválidos",
                                              "reasons": [
                                                "coupleName - es obligatorio cuando el estado civil es MARRIED"
                                              ]
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El discípulo no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "El discípulo solicitado no existe",
                                              "reasons": []
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "DNI o teléfono ya usado por otro registro",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "Ya existe un discípulo registrado con ese teléfono",
                                              "reasons": []
                                            }
                                            """)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "id",
            description = "Id del discípulo",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<DiscipleResponseDto>> update(
            @PathVariable("id") @Min(1) Long id,
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
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 200,
                                              "message": "Discípulo eliminado exitosamente",
                                              "success": true,
                                              "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El discípulo no existe o ya fue eliminado previamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "El discípulo solicitado no existe",
                                              "reasons": []
                                            }
                                            """)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "id",
            description = "Id del discípulo",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<Void>> softDeleteById(@PathVariable("id") @Min(1) Long id);

    @Operation(
            summary = "Exportar discípulos a Excel",
            description = """
                    Genera un archivo Excel con los discípulos filtrados según los criterios de búsqueda.
                    Soporta los mismos filtros que el listado (firstName, lastName, spiritualLevel, maritalStatus, isLeader).
                    """,
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
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "firstName",
            description = "Filtro por nombres del discípulo",
            example = "Heinz",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "lastName",
            description = "Filtro por apellidos del discípulo",
            example = "Juárez",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "spiritualLevel",
            description = "Filtro por nivel espiritual",
            example = "GUEST",
            schema = @Schema(implementation = SpiritualLevel.class),
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "maritalStatus",
            description = "Filtro por estado civil",
            example = "SINGLE",
            schema = @Schema(implementation = MaritalStatus.class),
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "isCellGroupLeader",
            description = "Filtro por si el discípulo es líder de célula",
            example = "false",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "page", description = "Número de página (0-indexed)",
            example = "0", in = ParameterIn.QUERY
    )
    @Parameter(
            name = "size",
            description = "Cantidad de resultados por página",
            example = "10",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "sortField",
            description = "Campo por el cual ordenar los resultados",
            example = "lastName",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "sortDirection",
            description = "Dirección de orden: ASC o DESC",
            example = "ASC",
            in = ParameterIn.QUERY
    )
    ResponseEntity<byte[]> exportToExcel(@ModelAttribute DiscipleSearchCriteriaDto criteria) throws IOException;
}
