package com.oam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateApartmentRequestDto(

        @NotBlank(message = "The number is mandatory!")
        String number,

        @NotNull(message = "The number of persons is mandatory!")
        Integer numberOfPersons,

        @NotNull(message = "The surface is mandatory!")
        Double surface,

        @NotNull(message = "Association is mandatory!")
        UUID associationId
) {
}
