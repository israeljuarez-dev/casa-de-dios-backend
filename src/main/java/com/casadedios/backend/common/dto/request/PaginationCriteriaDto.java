package com.casadedios.backend.common.dto.request;

import lombok.Builder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Builder
public record PaginationCriteriaDto(
        Integer page,
        Integer size,
        String sortField,
        String sortDirection
) {
    public PaginationCriteriaDto{
        // Contrato público 1-indexed (page=1 = primera página), acorde a lo que ya asume el frontend.
        // La conversión a 0-indexed (nativo de Spring Data) queda aislada en toPageable().
        if (page == null || page < 1) {
            page = 1;
        }
        if (size == null || size < 1) {
            size = 10;
        }
    }
    public Pageable toPageable() {
        int zeroIndexedPage = page - 1; // único punto de conversión 1-indexed -> 0-indexed

        if (sortField == null || sortField.isBlank()) {
            return PageRequest.of(zeroIndexedPage, size);
        }

        Sort.Direction direction =
                "DESC".equalsIgnoreCase(sortDirection)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        return PageRequest.of(
                zeroIndexedPage,
                size,
                Sort.by(direction, sortField)
        );
    }
}