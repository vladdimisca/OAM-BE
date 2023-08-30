package com.oam.dto;

import jakarta.validation.constraints.*;

public record CreateUserRequestDto(

        @NotBlank(message = "Email is mandatory!")
        @Email(message = "Email must have a valid format!")
        String email,

        @NotBlank(message = "Calling code is mandatory!")
        String callingCode,

        @NotBlank(message = "Phone number is mandatory!")
        @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
                message = "Phone number must have a valid format!")
        String phoneNumber,

        @NotBlank(message = "First name is mandatory!")
        String firstName,

        @NotBlank(message = "Last name is mandatory!")
        String lastName,

        @NotBlank(message = "Password is mandatory!")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z]).{6,}",
                message = "Password must contain at least 6 characters including one digit and one lower case letter!")
        String password
) { }
