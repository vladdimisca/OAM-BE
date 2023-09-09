package com.oam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateApartmentRequestDto(

        @NotBlank(message = "The number is mandatory!")
        String number,

        @NotNull(message = "The number of persons is mandatory!")
        @Positive(message = "The number of persons must be a positive value!")
        Integer numberOfPersons,

        @NotNull(message = "The surface is mandatory!")
        @Positive(message = "The surface must be a positive value!")
        Double surface,

        @NotNull(message = "The association is mandatory!")
        UUID associationId
) {
}
