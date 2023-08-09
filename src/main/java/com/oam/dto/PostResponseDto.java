package com.oam.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostResponseDto(
        UUID id,
        String title,
        String text,
        String summary,
        LocalDateTime createdAt,
        UserResponseDto user,
        AssociationResponseDto association
) {
}
