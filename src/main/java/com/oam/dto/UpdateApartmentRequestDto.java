package com.oam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateApartmentRequestDto(

        @NotBlank(message = "Street is mandatory!")
        String number,

        @NotNull(message = "Street is mandatory!")
        Integer numberOfPersons,

        @NotNull(message = "Surface is mandatory!")
        Double surface
) {
}
