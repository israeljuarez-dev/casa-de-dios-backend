package com.casadedios.backend.modules.inventory.exception;

import com.casadedios.backend.common.exception.model.ApiErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum InventoryErrorEnum implements ApiErrorCode {
    INVENTORY_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "El ítem de inventario solicitado no existe"),
    INVENTORY_DONOR_REQUIRED(HttpStatus.UNPROCESSABLE_CONTENT, "Los ítems donados requieren especificar el discípulo donante"),
    INVENTORY_INSUFFICIENT_STOCK(HttpStatus.UNPROCESSABLE_CONTENT, "No hay stock suficiente para realizar la salida solicitada"),
    ;

    private final HttpStatusCode status;

    private final String message;

    InventoryErrorEnum(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
