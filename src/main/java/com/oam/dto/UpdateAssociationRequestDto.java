package com.oam.dto;

import com.oam.model.validator.IbanConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateAssociationRequestDto (

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

        @IbanConstraint(message = "The iban has an invalid format!")
        String iban
) { }
