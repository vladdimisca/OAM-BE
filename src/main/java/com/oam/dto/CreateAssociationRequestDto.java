package com.oam.dto;

import com.oam.model.validator.IbanConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateAssociationRequestDto(

        @NotBlank(message = "The country is mandatory!")
        String country,

        @NotBlank(message = "The locality is mandatory!")
        String locality,

        @NotBlank(message = "The administrative area is mandatory!")
        String administrativeArea,

        @NotBlank(message = "The zip code is mandatory!")
        @Pattern(regexp = "\\d+", message = "The zip code must contain only digits!")
        String zipCode,

        @NotBlank(message = "The street is mandatory!")
        String street,

        @NotBlank(message = "The number is mandatory!")
        String number,

        @NotBlank(message = "The block is mandatory!")
        String block,

        @NotBlank(message = "The staircase is mandatory!")
        String staircase,


        @NotNull(message = "The latitude is mandatory!")
        Double latitude,

        @NotNull(message = "The longitude is mandatory!")
        Double longitude,

        @IbanConstraint(message = "The iban has an invalid format!")
        String iban
) { }
