package com.oam.dto;

import java.util.List;
import java.util.UUID;

public record ApartmentResponseDto(
        UUID id,
        String number,
        Integer numberOfPersons,
        String code,
        Double surface,
        AssociationResponseDto association,
        List<UserResponseDto> admins,
        List<UserResponseDto> members
) { }
