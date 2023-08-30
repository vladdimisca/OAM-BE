package com.oam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCommentRequestDto(
        @NotBlank(message = "The comment text must not be blank!")
        String text,

        @NotNull(message = "Post is mandatory!")
        UUID postId
) {
}
