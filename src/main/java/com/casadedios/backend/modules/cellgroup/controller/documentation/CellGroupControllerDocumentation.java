package com.casadedios.backend.modules.cellgroup.controller.documentation;

import com.casadedios.backend.common.dto.response.ApiResponseBodyDto;
import com.casadedios.backend.modules.cellgroup.dto.request.*;
import com.casadedios.backend.modules.cellgroup.dto.response.CellGroupMemberResponseDto;
import com.casadedios.backend.modules.cellgroup.dto.response.CellGroupResponseDto;
import com.casadedios.backend.modules.cellgroup.enums.MeetingDay;
import com.casadedios.backend.common.dto.response.ApiResponseDto;
import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.common.exception.dto.ErrorDto;
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
import java.util.List;

@Tag(name = "Células", description = "Gestión de células, miembros y Los 12")
public interface CellGroupControllerDocumentation {

    @Operation(
            summary = "Listar células",
            description = """
                    Devuelve un listado paginado de células con filtros dinámicos opcionales.
                    """,
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
                                                    "name": "Los Bendecidos",
                                                    "leader": { "id": 3, "firstName": "Juan", "lastName": "Pérez" },
                                                    "meetingDay": "MONDAY",
                                                    "meetingDaySpanishName": "Lunes",
                                                    "meetingTime": "19:00",
                                                    "location": "Av. Siempre Viva 123",
                                                    "isPastorCell": false,
                                                    "pastorCellGender": null,
                                                    "memberCount": 8
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
            name = "name",
            description = "Filtro por nombre de la célula (búsqueda parcial)",
            example = "Bendecidos",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "leaderName",
            description = "Filtro por nombre o apellido del líder (búsqueda parcial)",
            example = "Juan",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "meetingDay",
            description = "Filtro por día de reunión",
            example = "MONDAY",
            schema = @Schema(implementation = MeetingDay.class),
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "isPastorCell",
            description = "Filtra únicamente células principales (true) o regulares (false)",
            example = "false",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "page",
            description = "Número de página (1-indexed: page=1 es la primera página)",
            example = "1",
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
            description = "Campo por el cual ordenar",
            example = "name",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "sortDirection",
            description = "Dirección de orden: ASC o DESC",
            example = "ASC",
            in = ParameterIn.QUERY
    )
    ResponseEntity<ApiResponseBodyDto<PaginationResponseDto<CellGroupResponseDto>>> findAll(@ModelAttribute CellGroupSearchCriteriaDto criteria);

    @Operation(
            summary = "Obtener una célula por id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Célula encontrada",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 200,
                                              "message": "Célula encontrada",
                                              "success": true,
                                              "data": {
                                                "id": 1,
                                                "name": "Los Bendecidos",
                                                "leader": { "id": 3, "firstName": "Juan", "lastName": "Pérez" },
                                                "meetingDay": "MONDAY",
                                                "meetingDaySpanishName": "Lunes",
                                                "meetingTime": "19:00",
                                                "location": "Av. Siempre Viva 123",
                                                "isPastorCell": false,
                                                "pastorCellGender": null,
                                                "memberCount": 8
                                              }
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "La célula no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "message": "La célula solicitada no existe",
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
            description = "Id de la célula",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<CellGroupResponseDto>> findById(@PathVariable("id") @Min(1) Long id);

    @Operation(
            summary = "Registrar una nueva célula",
            description = """
                    Registra una célula regular o una célula principal (del pastor o la pastora).
                    leaderDiscipleId es obligatorio cuando isPastorCell = false, y debe apuntar a un
                    discípulo con spiritualLevel = LEADER. pastorCellGender es obligatorio cuando
                    isPastorCell = true. Las células principales no llevan líder discípulo asignado.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(
                                            name = "Célula regular",
                                            value = """
                                                    {
                                                      "name": "Los Bendecidos",
                                                      "leaderDiscipleId": 3,
                                                      "meetingDay": "MONDAY",
                                                      "meetingTime": "19:00",
                                                      "location": "Av. Siempre Viva 123",
                                                      "isPastorCell": false
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Célula principal del pastor",
                                            value = """
                                                    {
                                                      "name": "Célula del Pastor",
                                                      "meetingDay": "MONDAY",
                                                      "meetingTime": "19:00",
                                                      "location": "Iglesia Central",
                                                      "isPastorCell": true,
                                                      "pastorCellGender": "MALE"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Célula registrada exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 201,
                                              "message": "Célula registrada exitosamente",
                                              "success": true,
                                              "data": {
                                                "id": 1,
                                                "name": "Los Bendecidos",
                                                "leader": { "id": 3, "firstName": "Juan", "lastName": "Pérez" },
                                                "meetingDay": "MONDAY",
                                                "meetingDaySpanishName": "Lunes",
                                                "meetingTime": "19:00",
                                                "location": "Av. Siempre Viva 123",
                                                "isPastorCell": false,
                                                "pastorCellGender": null,
                                                "memberCount": 0
                                              }
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "El ID del líder es obligatorio para células regulares",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El discípulo solicitado como líder no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = """
                                    Ya existe una célula con ese nombre, o ya existen las dos células
                                    principales del pastor y la pastora, o ya existe una célula principal
                                    registrada para ese género
                                    """,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = """
                                    El discípulo debe tener nivel espiritual LEADER para dirigir una célula,
                                    o las células principales requieren especificar pastorCellGender
                                    """,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<ApiResponseBodyDto<CellGroupResponseDto>> create(@RequestBody @Valid CellGroupRegisterRequestDto request);

    @Operation(
            summary = "Actualizar una célula existente",
            description = """
                    Actualiza parcialmente una célula. Los campos no enviados conservan su valor actual.
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    {
                                      "name": "Los Bendecidos Renovados",
                                      "leaderDiscipleId": 5,
                                      "meetingDay": "WEDNESDAY",
                                      "meetingTime": "20:00",
                                      "location": "Jr. Los Pinos 456"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Célula actualizada exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 200,
                                              "message": "Célula actualizada exitosamente",
                                              "success": true,
                                              "data": {
                                                "id": 1,
                                                "name": "Los Bendecidos Renovados",
                                                "leader": { "id": 5, "firstName": "Pedro", "lastName": "Gómez" },
                                                "meetingDay": "WEDNESDAY",
                                                "meetingDaySpanishName": "Miércoles",
                                                "meetingTime": "20:00",
                                                "location": "Jr. Los Pinos 456",
                                                "isPastorCell": false,
                                                "pastorCellGender": null,
                                                "memberCount": 8
                                              }
                                            }
                                            """)
                            )),
                    @ApiResponse(
                            responseCode = "404",
                            description = "La célula no existe, o el nuevo líder no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Ya existe una célula registrada con ese nombre",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "El nuevo líder no tiene el nivel espiritual LEADER",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "id",
            description = "Id de la célula",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<CellGroupResponseDto>> update(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid CellGroupUpdateRequestDto request
    );

    @Operation(
            summary = "Eliminar una célula",
            description = """
                    Elimina físicamente la célula y sus miembros asociados (CASCADE).
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Célula eliminada exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 200,
                                              "message": "Célula eliminada exitosamente",
                                              "success": true,
                                              "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "La célula no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "id",
            description = "Id de la célula",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<Void>> deleteById(@PathVariable("id") @Min(1) Long id);

    @Operation(
            summary = "Listar miembros de una célula",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Miembros obtenidos exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 200,
                                              "message": "Miembros obtenidos exitosamente",
                                              "success": true,
                                              "data": [
                                                {
                                                  "memberId": 10,
                                                  "discipleId": 5,
                                                  "firstName": "Pedro",
                                                  "lastName": "Gómez",
                                                  "phoneCodeNumber": "51",
                                                  "phoneNumber": "987654321",
                                                  "spiritualLevel": "GUEST",
                                                  "birthDate": "1998-04-10",
                                                  "age": 27,
                                                  "gender": "MALE",
                                                  "isCellGroupLeader": false,
                                                  "isCoreTwelve": false,
                                                  "isPastorCoreTwelve": false,
                                                  "birthdayAlert": {
                                                    "isToday": false,
                                                    "isTomorrow": false,
                                                    "wasYesterday": false,
                                                    "withinCurrentMonth": false,
                                                    "withinCurrentWeek": false,
                                                    "daysUntilNextBirthday": 200,
                                                    "nextBirthday": "2027-04-10",
                                                    "dayOfWeek": "sábado"
                                                  }
                                                }
                                              ]
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "La célula no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "id",
            description = "Id de la célula",
            example = "1",
            in = ParameterIn.PATH
    )
    @Parameter(
            name = "search",
            description = "Búsqueda parcial por nombre o apellido del miembro",
            example = "Pedro",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "spiritualLevel",
            description = "Filtro exacto por nivel espiritual",
            example = "GUEST",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "gender",
            description = "Filtro exacto por género",
            example = "MALE",
            in = ParameterIn.QUERY
    )
    ResponseEntity<ApiResponseBodyDto<List<CellGroupMemberResponseDto>>> findMembers(
            @PathVariable("id") @Min(1) Long id,
            @ModelAttribute CellGroupMemberSearchCriteriaDto criteria
    );

    @Operation(
            summary = "Añadir un discípulo a una célula",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    {
                                      "discipleId": 5
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Discípulo añadido exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 201,
                                              "message": "Discípulo añadido a la célula",
                                              "success": true,
                                              "data": {
                                                "memberId": 10,
                                                "discipleId": 5,
                                                "firstName": "Pedro",
                                                "lastName": "Gómez",
                                                "phoneCodeNumber": "51",
                                                "phoneNumber": "987654321",
                                                "spiritualLevel": "GUEST",
                                                "birthDate": "1998-04-10",
                                                "age": 27,
                                                "gender": "MALE",
                                                "isCellGroupLeader": false,
                                                "isCoreTwelve": false,
                                                "isPastorCoreTwelve": false,
                                                "birthdayAlert": {
                                                  "isToday": false,
                                                  "isTomorrow": false,
                                                  "wasYesterday": false,
                                                  "withinCurrentMonth": false,
                                                  "withinCurrentWeek": false,
                                                  "daysUntilNextBirthday": 200,
                                                  "nextBirthday": "2027-04-10",
                                                  "dayOfWeek": "sábado"
                                                }
                                              }
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "La célula o el discípulo no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = """
                                    El discípulo ya es miembro de esta célula, ya pertenece a otra célula,
                                    o la célula principal ya alcanzó el límite de 12 miembros
                                    """,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = """
                                    El líder no puede ser miembro de su propia célula, o el discípulo
                                    incumple el nivel espiritual o el género requerido por la célula principal
                                    """,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "id",
            description = "Id de la célula",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<CellGroupMemberResponseDto>> addMember(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid CellGroupMemberRequestDto request
    );

    @Operation(
            summary = "Remover un discípulo de una célula",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Discípulo removido exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 200,
                                              "message": "Discípulo removido de la célula",
                                              "success": true,
                                              "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "La célula no existe, o el discípulo no es miembro de esta célula",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "id",
            description = "Id de la célula",
            example = "1",
            in = ParameterIn.PATH
    )
    @Parameter(
            name = "discipleId",
            description = "Id del discípulo",
            example = "5",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<Void>> removeMember(
            @PathVariable("id") @Min(1) Long id,
            @PathVariable("discipleId") @Min(1) Long discipleId
    );

    @Operation(
            summary = "Marcar discípulo como Los 12 del líder",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Discípulo marcado como Los 12",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 200,
                                              "message": "Discípulo marcado como Los 12",
                                              "success": true,
                                              "data": {
                                                "memberId": 10,
                                                "discipleId": 5,
                                                "firstName": "Pedro",
                                                "lastName": "Gómez",
                                                "phoneCodeNumber": "51",
                                                "phoneNumber": "987654321",
                                                "spiritualLevel": "GUEST",
                                                "birthDate": "1998-04-10",
                                                "age": 27,
                                                "gender": "MALE",
                                                "isCellGroupLeader": false,
                                                "isCoreTwelve": true,
                                                "isPastorCoreTwelve": false,
                                                "birthdayAlert": {
                                                  "isToday": false,
                                                  "isTomorrow": false,
                                                  "wasYesterday": false,
                                                  "withinCurrentMonth": false,
                                                  "withinCurrentWeek": false,
                                                  "daysUntilNextBirthday": 200,
                                                  "nextBirthday": "2027-04-10",
                                                  "dayOfWeek": "sábado"
                                                }
                                              }
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "La célula no existe, o el discípulo no es miembro de esta célula",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "id",
            description = "Id de la célula",
            example = "1",
            in = ParameterIn.PATH
    )
    @Parameter(
            name = "discipleId",
            description = "Id del discípulo",
            example = "5",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<CellGroupMemberResponseDto>> markAsCoreTwelve(
            @PathVariable("id") @Min(1) Long id,
            @PathVariable("discipleId") @Min(1) Long discipleId
    );

    @Operation(
            summary = "Desmarcar discípulo de Los 12 del líder",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Discípulo desmarcado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 200,
                                              "message": "Discípulo removido de Los 12",
                                              "success": true,
                                              "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "La célula no existe, o el discípulo no es miembro de esta célula",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "id",
            description = "Id de la célula",
            example = "1",
            in = ParameterIn.PATH
    )
    @Parameter(
            name = "discipleId",
            description = "Id del discípulo",
            example = "5",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<Void>> unmarkAsCoreTwelve(
            @PathVariable("id") @Min(1) Long id,
            @PathVariable("discipleId") @Min(1) Long discipleId
    );

    @Operation(
            summary = "Marcar discípulo como Los 12 del pastor",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Discípulo marcado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 200,
                                              "message": "Discípulo marcado como Los 12 del pastor",
                                              "success": true,
                                              "data": {
                                                "memberId": 10,
                                                "discipleId": 5,
                                                "firstName": "Pedro",
                                                "lastName": "Gómez",
                                                "phoneCodeNumber": "51",
                                                "phoneNumber": "987654321",
                                                "spiritualLevel": "LEADER",
                                                "birthDate": "1998-04-10",
                                                "age": 27,
                                                "gender": "MALE",
                                                "isCellGroupLeader": true,
                                                "isCoreTwelve": true,
                                                "isPastorCoreTwelve": true,
                                                "birthdayAlert": {
                                                  "isToday": false,
                                                  "isTomorrow": false,
                                                  "wasYesterday": false,
                                                  "withinCurrentMonth": false,
                                                  "withinCurrentWeek": false,
                                                  "daysUntilNextBirthday": 200,
                                                  "nextBirthday": "2027-04-10",
                                                  "dayOfWeek": "sábado"
                                                }
                                              }
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "La célula no existe, o el discípulo no es miembro de esta célula",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Ya se alcanzó el límite de 12 discípulos del pastor en toda la iglesia",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Solo los líderes de célula pueden formar parte de Los 12 del pastor",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "id",
            description = "Id de la célula",
            example = "1",
            in = ParameterIn.PATH
    )
    @Parameter(
            name = "discipleId",
            description = "Id del discípulo",
            example = "5",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<CellGroupMemberResponseDto>> markAsPastorCoreTwelve(
            @PathVariable("id") @Min(1) Long id,
            @PathVariable("discipleId") @Min(1) Long discipleId
    );

    @Operation(
            summary = "Desmarcar discípulo de Los 12 del pastor",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Discípulo desmarcado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "status": 200,
                                              "message": "Discípulo removido de Los 12 del pastor",
                                              "success": true,
                                              "data": null
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "La célula no existe, o el discípulo no es miembro de esta célula",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "id",
            description = "Id de la célula",
            example = "1",
            in = ParameterIn.PATH
    )
    @Parameter(
            name = "discipleId",
            description = "Id del discípulo",
            example = "5",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<Void>> unmarkAsPastorCoreTwelve(
            @PathVariable("id") @Min(1) Long id,
            @PathVariable("discipleId") @Min(1) Long discipleId
    );

    @Operation(
            summary = "Listar Los 12 del pastor",
            description = """
                    Retorna todos los discípulos marcados como Los 12 del pastor o la pastora,
                    de todas las células.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Los 12 del pastor obtenidos exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    )
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<ApiResponseBodyDto<List<CellGroupMemberResponseDto>>> findPastorCoreTwelve();

    @Operation(
            summary = "Exportar células a Excel",
            description = """
                    Genera un archivo Excel con las células filtradas según los criterios de búsqueda.
                    Soporta los mismos filtros que el listado.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Archivo Excel generado exitosamente",
                            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
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
            name = "name",
            description = "Filtro por nombre de la célula",
            example = "Bendecidos",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "leaderName",
            description = "Filtro por nombre o apellido del líder",
            example = "Juan",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "meetingDay",
            description = "Filtro por día de reunión",
            example = "MONDAY",
            schema = @Schema(implementation = MeetingDay.class),
            in = ParameterIn.QUERY
    )
    ResponseEntity<byte[]> exportToExcel(@ModelAttribute CellGroupSearchCriteriaDto criteria) throws IOException;
}