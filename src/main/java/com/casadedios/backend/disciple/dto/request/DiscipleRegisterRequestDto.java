package com.casadedios.backend.disciple.dto.request;
import com.casadedios.backend.disciple.enums.MaritalStatus;
import com.casadedios.backend.disciple.enums.SpiritualLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record DiscipleRegisterRequestDto(
        @NotBlank(message = "Los nombres son obligatorios")
        @Size(min = 3, max = 150, message = "Los nombres deben tener entre 3 y 150 caracteres")
        String firstName,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Size(min = 3, max = 150, message = "Los apellidos deben tener entre 3 y 150 caracteres")
        String lastName,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser en el pasado")
        LocalDate birthDate,

        @Size(max = 150, message = "La ocupación debe tener entre 3 y 150 caracteres")
        String occupation,

        String phoneCodeNumber,

        String phoneNumber,

        @Size(min=3, max = 255, message = "La dirección debe tener entre 3 y 255 caracteres")
        String address,

        @Size(max = 20, message = "El DNI no puede superar los 20 caracteres")
        String dni,

        @NotNull(message = "El estado civil es obligatorio")
        MaritalStatus maritalStatus,

        @Size(min = 3, max = 150, message = "El nombre del cónyuge debe tener entre 3 y 150 caracteres")
        String coupleName,

        @NotNull(message = "El nivel espiritual es obligatorio")
        SpiritualLevel spiritualLevel,

        boolean isLeader,

        @Valid
        List<DiscipleChildRegisterRequestDto> children,

        Long invitedByDiscipleId
) {}