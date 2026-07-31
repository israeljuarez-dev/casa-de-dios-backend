package com.casadedios.backend.disciple.dto.request;

import com.casadedios.backend.disciple.enums.MaritalStatus;
import com.casadedios.backend.disciple.enums.SpiritualLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record DiscipleUpdateRequestDto(
        @Size(min = 3, max = 150, message = "Los nombres deben tener entre 3 y 150 caracteres")
        String firstName,

        @Size(min = 3, max = 150, message = "Los apellidos deben tener entre 3 y 150 caracteres")
        String lastName,

        @Past(message = "La fecha de nacimiento debe ser en el pasado")
        LocalDate birthDate,

        @Size(max = 150, message = "La ocupación no puede superar los 150 caracteres")
        String occupation,

        @Size(min = 9, max = 9, message = "El número de teléfono debe tener exactamente 9 números")
        @Pattern(
                regexp = "\\d+",
                message = "El número de teléfono solo puede contener números"
        )
        String phoneNumber,

        @Size(min = 3, max = 255, message = "La dirección debe tener entre 3 y 255 caracteres")
        String address,

        @Size(max = 20, message = "El DNI no puede superar los 20 caracteres")
        String dni,

        MaritalStatus maritalStatus,

        @Size(min = 3, max = 150, message = "El nombre del cónyuge debe tener entre 3 y 150 caracteres")
        String coupleName,

        SpiritualLevel spiritualLevel,

        Boolean isLeader,

        @Valid
        List<DiscipleChildUpdateRequestDto> children
) {}
