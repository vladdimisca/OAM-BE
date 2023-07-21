package com.oam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateAssociationRequestDto (

        @NotBlank(message = "Country is mandatory!")
        String country,

        @NotBlank(message = "Locality is mandatory!")
        String locality,

        @NotBlank(message = "Administrative area is mandatory!")
        String administrativeArea,

        @NotBlank(message = "Zip code is mandatory!")
        @Pattern(regexp = "\\d+")
        String zipCode,

        @NotBlank(message = "Street is mandatory!")
        String street,

        @NotBlank(message = "Number is mandatory!")
        String number,

        @NotBlank(message = "Staircase is mandatory!")
        String staircase
) { }
