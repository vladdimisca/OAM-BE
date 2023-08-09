package com.oam.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponseDto(
        UUID id,
        String text,
        LocalDateTime createdAt,
        UserResponseDto user,
        PostResponseDto post
) {
}
