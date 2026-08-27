package com.casadedios.backend.cellgroup.controller.documentation;

import com.casadedios.backend.cellgroup.dto.request.*;
import com.casadedios.backend.cellgroup.dto.response.CellGroupMemberResponseDto;
import com.casadedios.backend.cellgroup.dto.response.CellGroupResponseDto;
import com.casadedios.backend.cellgroup.enums.MeetingDay;
import com.casadedios.backend.common.dto.response.ApiResponseDto;
import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.common.exception.dto.ErrorDto;
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

import java.util.List;

@Tag(name = "Células", description = "Gestión de células, miembros y Los 12")
public interface CellGroupControllerDocumentation {

    @Operation(
            summary = "Listar células",
            description = "Devuelve un listado paginado de células con filtros dinámicos opcionales.",
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
                    name = "name",
                    description = "Filtro por nombre de la célula (búsqueda parcial)",
                    example = "Bendecidos",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "leaderName",
                    description = "Filtro por nombre o apellido del líder (búsqueda parcial)",
                    example = "Juan", in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "meetingDay",
                    description = "Filtro por día de reunión",
                    example = "MONDAY",
                    schema = @Schema(implementation = MeetingDay.class),
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "page",
                    description = "Número de página (0-indexed)",
                    example = "0", in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "size",
                    description = "Cantidad de resultados por página",
                    example = "10",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "sortField",
                    description = "Campo por el cual ordenar",
                    example = "name",
                    in = ParameterIn.QUERY
            ),
            @Parameter(
                    name = "sortDirection",
                    description = "Dirección de orden: ASC o DESC",
                    example = "ASC",
                    in = ParameterIn.QUERY
            )
    })
    ResponseEntity<ApiResponseDto<PaginationResponseDto<CellGroupResponseDto>>> findAll(@ModelAttribute CellGroupSearchCriteriaDto criteria);

    @Operation(
            summary = "Obtener una célula por id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Célula encontrada",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
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
    @Parameter(name = "id", description = "Id de la célula", example = "1", in = ParameterIn.PATH)
    ResponseEntity<ApiResponseDto<CellGroupResponseDto>> findById(@PathVariable @Min(1) Long id);

    @Operation(
            summary = "Registrar una nueva célula",
            description = "El líder asignado debe tener isLeader = true. El nombre debe ser único.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Célula registrada exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class))),
                    @ApiResponse(responseCode = "404", description = "El discípulo líder no existe", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class))),
                    @ApiResponse(responseCode = "409", description = "Ya existe una célula con ese nombre", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class))),
                    @ApiResponse(responseCode = "422", description = "El discípulo no tiene el nivel de Líder de Célula", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class)))
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Registro de célula",
                                    summary = "Ejemplo de registro mínimo",
                                    value = """
                                            {
                                              "name": "Los Bendecidos",
                                              "leaderDiscipleId": 1,
                                              "meetingDay": "MONDAY",
                                              "meetingTime": "19:00",
                                              "location": "Av. Siempre Viva 123"
                                            }
                                            """
                            )
                    )
            )
    )
    ResponseEntity<ApiResponseDto<CellGroupResponseDto>> create(@RequestBody @Valid CellGroupRegisterRequestDto request);

    @Operation(
            summary = "Actualizar una célula existente",
            description = "Actualiza parcialmente una célula. Los campos no enviados conservan su valor actual.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Célula actualizada exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "La célula no existe", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class))),
                    @ApiResponse(responseCode = "409", description = "Ya existe una célula con ese nombre", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class))),
                    @ApiResponse(responseCode = "422", description = "El nuevo líder no tiene el nivel de Líder de Célula", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class)))
            }
    )
    @Parameter(name = "id", description = "Id de la célula", example = "1", in = ParameterIn.PATH)
    ResponseEntity<ApiResponseDto<CellGroupResponseDto>> update(@PathVariable @Min(1) Long id, @RequestBody @Valid CellGroupUpdateRequestDto request);

    @Operation(
            summary = "Eliminar una célula",
            description = "Elimina físicamente la célula y sus miembros asociados (CASCADE).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Célula eliminada exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "La célula no existe", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class)))
            }
    )
    @Parameter(name = "id", description = "Id de la célula", example = "1", in = ParameterIn.PATH)
    ResponseEntity<ApiResponseDto<Void>> deleteById(@PathVariable @Min(1) Long id);

    @Operation(summary = "Listar miembros de una célula", responses = {
            @ApiResponse(responseCode = "200", description = "Miembros obtenidos exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "La célula no existe", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class)))
    })
    @Parameter(name = "id", description = "Id de la célula", example = "1", in = ParameterIn.PATH)
    public ResponseEntity<ApiResponseDto<List<CellGroupMemberResponseDto>>> findMembers(
            @PathVariable @Min(1) Long id,
            @ModelAttribute CellGroupMemberSearchCriteriaDto criteria
    );

    @Operation(summary = "Añadir un discípulo a una célula", responses = {
            @ApiResponse(responseCode = "201", description = "Discípulo añadido exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "La célula o el discípulo no existe", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "409", description = "El discípulo ya es miembro de esta célula", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class)))
    })
    @Parameter(name = "id", description = "Id de la célula", example = "1", in = ParameterIn.PATH)
    ResponseEntity<ApiResponseDto<CellGroupMemberResponseDto>> addMember(@PathVariable @Min(1) Long id, @RequestBody @Valid CellGroupMemberRequestDto request);

    @Operation(summary = "Remover un discípulo de una célula", responses = {
            @ApiResponse(responseCode = "200", description = "Discípulo removido exitosamente"),
            @ApiResponse(responseCode = "404", description = "La célula no existe o el discípulo no es miembro", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class)))
    })
    @Parameters({
            @Parameter(name = "id", description = "Id de la célula", example = "1", in = ParameterIn.PATH),
            @Parameter(name = "discipleId", description = "Id del discípulo", example = "5", in = ParameterIn.PATH)
    })
    ResponseEntity<ApiResponseDto<Void>> removeMember(@PathVariable @Min(1) Long id, @PathVariable @Min(1) Long discipleId);

    @Operation(summary = "Marcar discípulo como Los 12 del líder", responses = {
            @ApiResponse(responseCode = "200", description = "Discípulo marcado exitosamente"),
            @ApiResponse(responseCode = "404", description = "La célula no existe o el discípulo no es miembro", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class)))
    })
    @Parameters({
            @Parameter(name = "id", description = "Id de la célula", example = "1", in = ParameterIn.PATH),
            @Parameter(name = "discipleId", description = "Id del discípulo", example = "5", in = ParameterIn.PATH)
    })
    ResponseEntity<ApiResponseDto<CellGroupMemberResponseDto>> markAsCoreTwelve(@PathVariable @Min(1) Long id, @PathVariable @Min(1) Long discipleId);

    @Operation(summary = "Desmarcar discípulo de Los 12 del líder", responses = {
            @ApiResponse(responseCode = "200", description = "Discípulo desmarcado exitosamente"),
            @ApiResponse(responseCode = "404", description = "La célula no existe o el discípulo no es miembro", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class)))
    })
    @Parameters({
            @Parameter(name = "id", description = "Id de la célula", example = "1", in = ParameterIn.PATH),
            @Parameter(name = "discipleId", description = "Id del discípulo", example = "5", in = ParameterIn.PATH)
    })
    ResponseEntity<ApiResponseDto<Void>> unmarkAsCoreTwelve(@PathVariable @Min(1) Long id, @PathVariable @Min(1) Long discipleId);

    @Operation(summary = "Marcar discípulo como Los 12 del pastor", responses = {
            @ApiResponse(responseCode = "200", description = "Discípulo marcado exitosamente"),
            @ApiResponse(responseCode = "404", description = "La célula no existe o el discípulo no es miembro", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "409", description = "Ya se alcanzó el límite de 12 discípulos del pastor para esta célula", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class)))
    })
    @Parameters({
            @Parameter(name = "id", description = "Id de la célula", example = "1", in = ParameterIn.PATH),
            @Parameter(name = "discipleId", description = "Id del discípulo", example = "5", in = ParameterIn.PATH)
    })
    ResponseEntity<ApiResponseDto<CellGroupMemberResponseDto>> markAsPastorCoreTwelve(@PathVariable @Min(1) Long id, @PathVariable @Min(1) Long discipleId);

    @Operation(summary = "Desmarcar discípulo de Los 12 del pastor", responses = {
            @ApiResponse(responseCode = "200", description = "Discípulo desmarcado exitosamente"),
            @ApiResponse(responseCode = "404", description = "La célula no existe o el discípulo no es miembro", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class)))
    })
    @Parameters({
            @Parameter(name = "id", description = "Id de la célula", example = "1", in = ParameterIn.PATH),
            @Parameter(name = "discipleId", description = "Id del discípulo", example = "5", in = ParameterIn.PATH)
    })
    ResponseEntity<ApiResponseDto<Void>> unmarkAsPastorCoreTwelve(@PathVariable @Min(1) Long id, @PathVariable @Min(1) Long discipleId);

    @Operation(
            summary = "Listar Los 12 del pastor",
            description = "Retorna todos los discípulos marcados como Los 12 del pastor o la pastora, de todas las células.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Los 12 del pastor obtenidos exitosamente", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiResponseDto.class)))
            }
    )
    ResponseEntity<ApiResponseDto<List<CellGroupMemberResponseDto>>> findPastorCoreTwelve();

    @Operation(
            summary = "Exportar células a Excel",
            description = "Genera un archivo Excel con las células filtradas según los criterios de búsqueda. Soporta los mismos filtros que el listado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Archivo Excel generado exitosamente", content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")),
                    @ApiResponse(responseCode = "500", description = "Error al generar el archivo Excel", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDto.class)))
            }
    )
    @Parameters({
            @Parameter(name = "name", description = "Filtro por nombre de la célula", example = "Bendecidos", in = ParameterIn.QUERY),
            @Parameter(name = "leaderName", description = "Filtro por nombre o apellido del líder", example = "Juan", in = ParameterIn.QUERY),
            @Parameter(name = "meetingDay", description = "Filtro por día de reunión", example = "MONDAY", schema = @Schema(implementation = MeetingDay.class), in = ParameterIn.QUERY)
    })
    ResponseEntity<byte[]> exportToExcel(@ModelAttribute CellGroupSearchCriteriaDto criteria);
}