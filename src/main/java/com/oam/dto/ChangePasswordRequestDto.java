package com.oam.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequestDto(

        @NotNull(message = "Old password is mandatory!")
        String oldPassword,

        @NotNull(message = "New password is mandatory!")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z]).{6,}",
                message = "Password must contain at least 6 characters including one digit and one lower case letter!")
        String newPassword
) {
}
