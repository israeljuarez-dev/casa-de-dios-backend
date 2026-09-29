package com.casadedios.backend.modules.retreat.dto.request;

import com.casadedios.backend.modules.retreat.validation.dates.RetreatDatesValidatable;
import com.casadedios.backend.modules.retreat.validation.dates.ValidRetreatDates;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.Instant;

import java.math.BigDecimal;

@Builder
@ValidRetreatDates
public record RetreatUpdateRequestDto(

        @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
        String name,

        @Size(max = 255, message = "El lugar no puede superar los 255 caracteres")
        String location,

        @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
        BigDecimal price,

        Instant startDate,

        Instant endDate

) implements RetreatDatesValidatable {

        @Override
        public Instant getStartDate() {
                return startDate;
        }

        @Override
        public Instant getEndDate() {
                return endDate;
        }
}