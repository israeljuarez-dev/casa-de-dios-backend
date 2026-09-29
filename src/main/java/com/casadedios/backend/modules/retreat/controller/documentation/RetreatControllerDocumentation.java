package com.casadedios.backend.modules.retreat.controller.documentation;

import com.casadedios.backend.common.dto.response.ApiResponseBodyDto;
import com.casadedios.backend.modules.retreat.dto.request.*;
import com.casadedios.backend.modules.retreat.dto.response.*;
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

@Tag(name = "Encuentros", description = "Gestión de encuentros, asistentes, pagos y staff")
public interface RetreatControllerDocumentation {

    @Operation(
            summary = "Listar encuentros",
            description = "Devuelve un listado paginado de encuentros con filtros dinámicos opcionales.",
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
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "name",
            description = "Filtro por nombre del encuentro (búsqueda parcial)",
            example = "Encuentro de Varones",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "location",
            description = "Filtro por lugar del encuentro (búsqueda parcial)",
            example = "Casa de Retiro San José",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "startDateFrom",
            description = "Filtro: fecha de inicio desde",
            example = "2026-01-01T00:00:00Z",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "startDateTo",
            description = "Filtro: fecha de inicio hasta",
            example = "2026-12-31T23:59:59Z",
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
            description = "Campo por el cual ordenar",
            example = "startDate",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "sortDirection",
            description = "Dirección de orden: ASC o DESC",
            example = "DESC",
            in = ParameterIn.QUERY
    )
    ResponseEntity<ApiResponseBodyDto<PaginationResponseDto<RetreatResponseDto>>> findAll(@ModelAttribute RetreatSearchCriteriaDto criteria);

    @Operation(
            summary = "Obtener un encuentro por id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Encuentro encontrado",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El encuentro no existe",
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
            description = "Id del encuentro",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<RetreatResponseDto>> findById(@PathVariable("id") @Min(1) Long id);

    @Operation(
            summary = "Registrar un nuevo encuentro",
            description = "Las fechas de inicio y fin deben ser futuras, y la fecha de fin posterior a la de inicio.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Encuentro registrado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos inválidos",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Las fechas son inválidas o no son futuras",
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
                                    name = "Registro de encuentro",
                                    summary = "Ejemplo de registro mínimo",
                                    value = """
                                            {
                                              "name": "Encuentro de Varones",
                                              "location": "Casa de Retiro San José",
                                              "startDate": "2026-11-06T19:00:00Z",
                                              "endDate": "2026-11-08T18:00:00Z",
                                              "price": 150.00
                                            }
                                            """
                            )
                    )
            )
    )
    ResponseEntity<ApiResponseBodyDto<RetreatResponseDto>> create(@RequestBody @Valid RetreatRegisterRequestDto request);

    @Operation(
            summary = "Actualizar un encuentro existente",
            description = "Actualiza parcialmente un encuentro. Los campos no enviados conservan su valor actual.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Encuentro actualizado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El encuentro no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Las fechas son inválidas o no son futuras",
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
            description = "Id del encuentro",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<RetreatResponseDto>> update(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid RetreatUpdateRequestDto request
    );

    @Operation(
            summary = "Eliminar un encuentro",
            description = "Elimina físicamente el encuentro junto con sus inscripciones y staff asociados (CASCADE).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Encuentro eliminado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El encuentro no existe",
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
            description = "Id del encuentro",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<Void>> deleteById(@PathVariable("id") @Min(1) Long id);

    @Operation(
            summary = "Listar asistentes inscritos a un encuentro",
            description = "Soporta filtro de texto libre por nombre/apellido y filtro por estado de pago.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Asistentes obtenidos exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El encuentro no existe",
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
            description = "Id del encuentro",
            example = "1",
            in = ParameterIn.PATH
    )
    @Parameter(
            name = "search",
            description = "Filtro por nombre o apellido del discípulo",
            example = "María",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "paymentStatus",
            description = "Filtro por estado de pago",
            example = "PENDING",
            in = ParameterIn.QUERY
    )
    ResponseEntity<ApiResponseBodyDto<List<RetreatEnrollmentResponseDto>>> findEnrolledDisciples(
            @PathVariable("id") @Min(1) Long id,
            @ModelAttribute RetreatEnrollmentSearchCriteriaDto criteria
    );

    @Operation(
            summary = "Inscribir discípulos a un encuentro",
            description = """ 
                          Inscribe uno o varios discípulos de una sola vez. Si alguno ya está
                          inscrito o no existe, se revierte toda la operación.
                          """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Discípulo(s) inscrito(s) exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El encuentro o alguno de los discípulos no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Algún discípulo ya está inscrito en este encuentro",
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
            description = "Id del encuentro",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<List<RetreatEnrollmentResponseDto>>> enrollDisciples(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid RetreatEnrollmentRequestDto request
    );

    @Operation(
            summary = "Remover asistentes de un encuentro",
            description = "Remueve una o varias inscripciones de una sola vez.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Discípulo(s) removido(s) exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El encuentro no existe",
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
            description = "Id del encuentro",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<Void>> removeEnrolledDisciples(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid RemoveEnrolledDisciplesRequestDto request
    );

    @Operation(
            summary = "Registrar un pago (total o parcial) de un asistente",
            description = """
                          Acumula el monto recibido sobre lo ya pagado. El estado de
                          pago se recalcula automáticamente (PENDING, PARTIAL o PAID).
                          """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Pago registrado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El discípulo no está inscrito en este encuentro",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "El monto pagado supera el costo del encuentro",
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
            description = "Id del encuentro",
            example = "1",
            in = ParameterIn.PATH
    )
    @Parameter(
            name = "discipleId",
            description = "Id del discípulo",
            example = "5",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<RetreatEnrollmentResponseDto>> registerPayment(
            @PathVariable("id") @Min(1) Long id,
            @PathVariable("discipleId") @Min(1) Long discipleId,
            @RequestBody @Valid RegisterPaymentRequestDto request
    );

    @Operation(
            summary = "Obtener un encuentro junto con todos sus asistentes",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Encuentro y asistentes obtenidos exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El encuentro no existe",
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
            description = "Id del encuentro",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<RetreatWithEnrollmentsResponseDto>> findRetreatWithEnrolledDisciples(
            @PathVariable("id") @Min(1) Long id
    ) ;

    @Operation(
            summary = "Listar staff de un encuentro",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Staff obtenido exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El encuentro no existe",
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
            description = "Id del encuentro",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<List<RetreatStaffResponseDto>>> findStaff(@PathVariable("id") @Min(1) Long id) ;

    @Operation(
            summary = "Añadir discípulos como staff de un encuentro",
            description = "Añade uno o varios discípulos de una sola vez. El staff no realiza pagos.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Discípulo(s) añadido(s) exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El encuentro o alguno de los discípulos no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Algún discípulo ya es staff de este encuentro",
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
            description = "Id del encuentro",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<List<RetreatStaffResponseDto>>> addStaff(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid RetreatStaffRequestDto request
    );

    @Operation(
            summary = "Remover discípulos del staff de un encuentro",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Discípulo(s) removido(s) exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El encuentro no existe",
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
            description = "Id del encuentro",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<Void>> removeStaff(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid RemoveStaffRequestDto request
    );

    @Operation(
            summary = "Exportar reporte completo del encuentro a Excel",
            description = "Incluye nombre, fechas, staff y asistentes con su estado de pago.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Archivo Excel generado exitosamente",
                            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El encuentro no existe",
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
            name = "id",
            description = "Id del encuentro",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<byte[]> exportFullReport(@PathVariable("id") @Min(1) Long id) throws IOException;

    @Operation(
            summary = "Exportar solo los asistentes de un encuentro a Excel",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Archivo Excel generado exitosamente",
                            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El encuentro no existe",
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
            name = "id", description = "Id del encuentro", example = "1", in = ParameterIn.PATH)
    ResponseEntity<byte[]> exportAttendees(@PathVariable("id") @Min(1) Long id) throws IOException;

    @Operation(
            summary = "Exportar asistentes con su estado de pago a Excel",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Archivo Excel generado exitosamente",
                            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El encuentro no existe",
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
            name = "id",
            description = "Id del encuentro",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<byte[]> exportAttendeesWithPayments(@PathVariable("id") @Min(1) Long id) throws IOException;

    @Operation(
            summary = "Exportar solo el staff de un encuentro a Excel",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Archivo Excel generado exitosamente",
                            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El encuentro no existe",
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
            name = "id",
            description = "Id del encuentro",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<byte[]> exportStaff(@PathVariable("id") @Min(1) Long id) throws IOException;
}
