package com.casadedios.backend.disciple.dto.request;

import com.casadedios.backend.common.enums.GenderEnum;
import com.casadedios.backend.disciple.enums.MaritalStatus;
import com.casadedios.backend.disciple.enums.SpiritualLevel;
import com.casadedios.backend.disciple.validation.phonenumber.PhoneValidatable;
import com.casadedios.backend.disciple.validation.phonenumber.ValidPhone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
@ValidPhone
public record DiscipleUpdateRequestDto(
        @Size(max = 150, message = "Los nombres no deben superar los 150 caracteres")
        String firstName,

        @Size(max = 150, message = "Los apellidos no deben superar los 150 caracteres")
        String lastName,

        GenderEnum gender,

        @Past(message = "La fecha de nacimiento debe ser en el pasado")
        LocalDate birthDate,

        @Size(max = 150, message = "La ocupación no puede superar los 150 caracteres")
        String occupation,

        @Size(max = 10)
        String phoneCodeNumber,

        String phoneNumber,

        @Size(max = 255, message = "La dirección no debe superar los 255 caracteres")
        String address,

        @Size(max = 20, message = "El DNI no puede superar los 20 caracteres")
        String dni,

        MaritalStatus maritalStatus,

        @Size(max = 150, message = "El nombre del cónyuge no debe superar los 150 caracteres")
        String coupleName,

        SpiritualLevel spiritualLevel,

        Boolean isLeader,

        @Valid
        List<DiscipleChildUpdateRequestDto> children

) implements PhoneValidatable {

        @Override
        public String getPhoneCodeNumber() {
                return phoneCodeNumber;
        }

        @Override
        public String getPhoneNumber() {
                return phoneNumber;
        }
}
