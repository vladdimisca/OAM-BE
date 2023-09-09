package com.oam.dto;

import jakarta.validation.constraints.*;

public record CreateUserRequestDto(

        @NotBlank(message = "The email is mandatory!")
        @Email(message = "The email must have a valid format!")
        String email,

        @NotBlank(message = "The calling code is mandatory!")
        String callingCode,

        @NotBlank(message = "The phone number is mandatory!")
        @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
                message = "The phone number must have a valid format!")
        String phoneNumber,

        @NotBlank(message = "The first name is mandatory!")
        String firstName,

        @NotBlank(message = "The last name is mandatory!")
        String lastName,

        @NotBlank(message = "The password is mandatory!")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z]).{6,}",
                message = "The password must contain at least 6 characters including one digit and one lower case letter!")
        String password
) { }
