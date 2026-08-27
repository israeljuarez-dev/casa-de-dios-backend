package com.casadedios.backend.cellgroup.dto.request;

import com.casadedios.backend.cellgroup.enums.MeetingDay;
import com.casadedios.backend.common.dto.request.PaginationCriteriaDto;
import lombok.Builder;

@Builder
public record CellGroupSearchCriteriaDto(
        String name,
        String leaderName,
        MeetingDay meetingDay,
        Boolean isPastorCell,
        Integer page,
        Integer size,
        String sortField,
        String sortDirection
) {
    public PaginationCriteriaDto pagination() {
        return PaginationCriteriaDto.builder()
                .page(page)
                .size(size)
                .sortField(sortField)
                .sortDirection(sortDirection)
                .build();
    }
}