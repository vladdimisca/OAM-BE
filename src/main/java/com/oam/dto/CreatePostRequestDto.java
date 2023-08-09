package com.oam.dto;

import java.util.UUID;

public record CreatePostRequestDto(
        String title,
        String text,
        UUID associationId
) {
}
