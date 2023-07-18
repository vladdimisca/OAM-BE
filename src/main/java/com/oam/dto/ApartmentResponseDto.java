package com.oam.dto;

import java.util.UUID;

public record ApartmentResponseDto(
        UUID id,
        String number,
        Integer numberOfPersons,
        String code,
        UUID associationId
) { }
