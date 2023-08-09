package com.oam.dto;

import java.util.List;
import java.util.UUID;

public record SimplifiedApartmentResponseDto(
        UUID id,
        String number,
        List<UserResponseDto> members
) { }
