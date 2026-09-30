package com.casadedios.backend.modules.inventory.controller.documentation;

import com.casadedios.backend.common.dto.response.ApiResponseBodyDto;
import com.casadedios.backend.common.dto.response.ApiResponseDto;
import com.casadedios.backend.common.dto.response.PaginationResponseDto;
import com.casadedios.backend.common.exception.dto.ErrorDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemRegisterRequestDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemSearchCriteriaDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemStockAdjustmentRequestDto;
import com.casadedios.backend.modules.inventory.dto.request.InventoryItemUpdateRequestDto;
import com.casadedios.backend.modules.inventory.dto.response.InventoryItemResponseDto;
import com.casadedios.backend.modules.inventory.enums.InventorySourceEnum;
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

@Tag(name = "Inventario", description = "Gestión de los bienes y utensilios de la iglesia, incluyendo el control de stock")
public interface InventoryItemControllerDocumentation {

    @Operation(
            summary = "Listar ítems de inventario",
            description = "Devuelve un listado paginado de ítems de inventario con filtros dinámicos opcionales.",
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
            description = "Filtro por nombre del ítem (búsqueda parcial)",
            example = "Silla",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "category",
            description = "Filtro por categoría (búsqueda parcial)",
            example = "Salón principal",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "sourceType",
            description = "Filtro por origen del ítem",
            example = "DONATED",
            schema = @Schema(implementation = InventorySourceEnum.class),
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "donorName",
            description = "Filtro por nombre o apellido del discípulo donante (búsqueda parcial)",
            example = "Juan Pérez",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "registeredFrom",
            description = "Filtro: fecha de registro desde (inclusive)",
            example = "2026-01-01",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "registeredTo",
            description = "Filtro: fecha de registro hasta (inclusive)",
            example = "2026-12-31",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "page",
            description = "Número de página (1-indexed)",
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
            description = "Campo por el cual ordenar: name, category, quantity, cost, sourceType, registeredAt. Por defecto: name",
            example = "registeredAt",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "sortDirection",
            description = "Dirección de orden: ASC o DESC",
            example = "DESC",
            in = ParameterIn.QUERY
    )
    ResponseEntity<ApiResponseBodyDto<PaginationResponseDto<InventoryItemResponseDto>>> findAll(
            @ModelAttribute InventoryItemSearchCriteriaDto criteria
    );

    @Operation(
            summary = "Obtener un ítem de inventario por id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ítem de inventario encontrado",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El ítem de inventario no existe",
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
            description = "Id del ítem de inventario",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<InventoryItemResponseDto>> findById(@PathVariable("id") @Min(1) Long id);

    @Operation(
            summary = "Registrar un nuevo ítem de inventario",
            description = """
                          La cantidad enviada es el stock inicial. El costo es unitario. Si el origen es DONATED,
                          donorDiscipleId es obligatorio; si es PURCHASED, se ignora. Si no se envía registeredAt,
                          se usa la fecha actual.
                          """,
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Ítem de inventario registrado exitosamente",
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
                            responseCode = "404",
                            description = "El discípulo donante no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Un ítem donado requiere especificar el donante",
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
                            examples = {
                                    @ExampleObject(
                                            name = "Ítem comprado",
                                            summary = "Ítem comprado por la iglesia",
                                            value = """
                                                    {
                                                      "name": "Silla plástica",
                                                      "description": "Silla plástica apilable color blanco",
                                                      "cost": 25.50,
                                                      "quantity": 150,
                                                      "category": "Salón principal",
                                                      "sourceType": "PURCHASED",
                                                      "registeredAt": "2026-09-30"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "Ítem donado",
                                            summary = "Ítem donado por un discípulo",
                                            value = """
                                                    {
                                                      "name": "Olla industrial",
                                                      "category": "Cocina",
                                                      "quantity": 2,
                                                      "sourceType": "DONATED",
                                                      "donorDiscipleId": 3
                                                    }
                                                    """
                                    )
                            }
                    )
            )
    )
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<ApiResponseBodyDto<InventoryItemResponseDto>> create(@RequestBody @Valid InventoryItemRegisterRequestDto request);

    @Operation(
            summary = "Actualizar un ítem de inventario existente",
            description = """
                          Actualiza parcialmente un ítem. Los campos no enviados conservan su valor actual.
                          La cantidad (stock) no se modifica aquí: use PATCH /inventory-items/{id}/stock.
                          Al cambiar el origen a PURCHASED se desvincula el donante; al cambiarlo a DONATED
                          se requiere un donante.
                          """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ítem de inventario actualizado exitosamente",
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
                            responseCode = "404",
                            description = "El ítem de inventario o el discípulo donante no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Un ítem donado requiere especificar el donante",
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
            description = "Id del ítem de inventario",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<InventoryItemResponseDto>> update(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid InventoryItemUpdateRequestDto request
    );

    @Operation(
            summary = "Eliminar un ítem de inventario",
            description = "Elimina físicamente el ítem de inventario.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ítem de inventario eliminado exitosamente",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El ítem de inventario no existe",
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
            description = "Id del ítem de inventario",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<Void>> deleteById(@PathVariable("id") @Min(1) Long id);

    @Operation(
            summary = "Ajustar el stock de un ítem de inventario",
            description = """
                          Registra un ingreso (INCREASE) o una salida (DECREASE) de unidades sobre el stock actual.
                          La operación es atómica: una salida nunca puede dejar el stock en negativo.
                          """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Stock ajustado exitosamente",
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
                            responseCode = "404",
                            description = "El ítem de inventario no existe",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "No hay stock suficiente para la salida solicitada",
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
                                    name = "Salida de stock",
                                    summary = "Retirar 5 unidades del stock",
                                    value = """
                                            {
                                              "type": "DECREASE",
                                              "quantity": 5
                                            }
                                            """
                            )
                    )
            )
    )
    @SecurityRequirement(name = "bearerAuth")
    @Parameter(
            name = "id",
            description = "Id del ítem de inventario",
            example = "1",
            in = ParameterIn.PATH
    )
    ResponseEntity<ApiResponseBodyDto<InventoryItemResponseDto>> adjustStock(
            @PathVariable("id") @Min(1) Long id,
            @RequestBody @Valid InventoryItemStockAdjustmentRequestDto request
    );

    @Operation(
            summary = "Exportar ítems de inventario a Excel",
            description = "Exporta todos los ítems que coinciden con los filtros aplicados, sin paginación.",
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
            description = "Filtro por nombre del ítem (búsqueda parcial)",
            example = "Silla",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "category",
            description = "Filtro por categoría (búsqueda parcial)",
            example = "Salón principal",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "sourceType",
            description = "Filtro por origen del ítem",
            example = "DONATED",
            schema = @Schema(implementation = InventorySourceEnum.class),
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "donorName",
            description = "Filtro por nombre o apellido del discípulo donante (búsqueda parcial)",
            example = "Juan Pérez",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "registeredFrom",
            description = "Filtro: fecha de registro desde (inclusive)",
            example = "2026-01-01",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "registeredTo",
            description = "Filtro: fecha de registro hasta (inclusive)",
            example = "2026-12-31",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "sortField",
            description = "Campo por el cual ordenar: name, category, quantity, cost, sourceType, registeredAt. Por defecto: name",
            example = "registeredAt",
            in = ParameterIn.QUERY
    )
    @Parameter(
            name = "sortDirection",
            description = "Dirección de orden: ASC o DESC",
            example = "DESC",
            in = ParameterIn.QUERY
    )
    ResponseEntity<byte[]> exportToExcel(@ModelAttribute InventoryItemSearchCriteriaDto criteria) throws IOException;
}
