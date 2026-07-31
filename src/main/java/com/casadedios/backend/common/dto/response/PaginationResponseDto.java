package com.casadedios.backend.common.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PaginationResponseDto<T>(
        List<T> content,
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages
){
    public static <T> PaginationResponseDto<T> of(List<T> content, Page<?> page) {
        return PaginationResponseDto.<T>builder()
                .content(content)
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
