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
        if (page == null || page < 0) {
            page = 0;
        }
        if (size == null || size < 1) {
            size = 10;
        }
    }
    public Pageable toPageable() {
        if (sortField == null || sortField.isBlank()) {
            return PageRequest.of(page, size);
        }

        Sort.Direction direction =
                "DESC".equalsIgnoreCase(sortDirection)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        return PageRequest.of(
                page,
                size,
                Sort.by(direction, sortField)
        );
    }
}