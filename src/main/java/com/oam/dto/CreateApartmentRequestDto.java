package com.oam.dto;

import java.util.UUID;

public record CreateApartmentRequestDto(
        String number,
        Integer numberOfPersons,

        Double surface,
        UUID associationId
) {
}
