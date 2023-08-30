package com.oam.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequestDto(
        @NotBlank(message = "The comment text must not be blank!")
        String text
) {
}
