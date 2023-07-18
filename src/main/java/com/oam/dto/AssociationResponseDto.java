package com.oam.dto;

import java.util.UUID;

public record AssociationResponseDto(
        UUID id,
        String country,
        String locality,
        String administrativeArea,
        String zipCode,
        String street,
        String number,
        String staircase,
        Double latitude,
        Double longitude
) { }
