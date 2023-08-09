package com.oam.dto;

import java.util.UUID;

public record CreateCommentRequestDto(
        String text,
        UUID postId
) {
}
