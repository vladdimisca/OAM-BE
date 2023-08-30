package com.oam.dto;

import java.util.List;
import java.util.UUID;

public record AssociationResponseDto(
        UUID id,
        String country,
        String locality,
        String administrativeArea,
        String zipCode,
        String street,
        String number,
        String block,
        String staircase,
        Double latitude,
        Double longitude,
        String iban,
        List<SimplifiedApartmentResponseDto> apartments,
        List<UserResponseDto> members,
        List<UserResponseDto> admins
) { }
