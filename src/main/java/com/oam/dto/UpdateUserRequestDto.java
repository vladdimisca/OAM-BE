package com.oam.dto;

import jakarta.validation.constraints.*;


public record UpdateUserRequestDto(

        @Email(message = "The email must have a valid format!")
        @NotBlank(message = "The email is mandatory!")
        String email,

        @NotNull(message = "The calling code is mandatory!")
        String callingCode,

        @NotNull(message = "The phone number is mandatory!")
        @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
                message = "The phone number must have a valid format!")
        String phoneNumber,

        @NotBlank(message = "The first name is mandatory!")
        String firstName,

        @NotBlank(message = "The last name is mandatory!")
        String lastName,

        String description
) { }
