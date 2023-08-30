package com.oam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdatePostRequestDto(
        @NotBlank(message = "Title must not be blank!")
        String title,

        @NotBlank(message = "The post content must not be blank!")
        String text
) {
}
