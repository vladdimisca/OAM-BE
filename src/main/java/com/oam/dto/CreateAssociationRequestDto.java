package com.oam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAssociationRequestDto(

        @NotBlank(message = "Country is mandatory!")
        String country,

        @NotBlank(message = "Locality is mandatory!")
        String locality,

        @NotBlank(message = "Administrative area is mandatory!")
        String administrativeArea,

        @NotBlank(message = "Zip code is mandatory!")
        String zipCode,

        @NotBlank(message = "Street is mandatory!")
        String street,

        @NotBlank(message = "Number is mandatory!")
        String number,

        @NotBlank(message = "Block is mandatory!")
        String block,

        @NotBlank(message = "Staircase is mandatory!")
        String staircase,


        @NotNull(message = "Latitude is mandatory!")
        Double latitude,

        @NotNull(message = "Latitude is mandatory!")
        Double longitude,

        @NotBlank(message = "Iban is mandatory!")
        String iban
) { }
