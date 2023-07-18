package com.oam.dto;

import jakarta.validation.constraints.*;

public record CreateUserRequestDto(

        @Email(message = "Email must have a valid format!")
        @NotNull(message = "Email is mandatory!")
        String email,

        @NotNull(message = "Calling code is mandatory!")
        String callingCode,

        @NotNull(message = "Phone number is mandatory!")
        @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
                message = "Phone number must have a valid format!")
        String phoneNumber,

        @NotBlank(message = "First name is mandatory!")
        String firstName,

        @NotBlank(message = "Last name is mandatory!")
        String lastName,

        @NotNull(message = "Password is mandatory!")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z]).{6,}",
                message = "Password must contain at least 6 characters including one digit and one lower case letter!")
        String password
) { }
