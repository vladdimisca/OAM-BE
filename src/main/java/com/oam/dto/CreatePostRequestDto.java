package com.oam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePostRequestDto(
        @NotBlank(message = "The title must not be blank!")
        String title,

        @NotBlank(message = "The post content must not be blank!")
        String text,

        @NotNull(message = "The association is mandatory!")
        UUID associationId
) {
}
