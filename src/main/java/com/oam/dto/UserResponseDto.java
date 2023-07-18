package com.oam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponseDto(

        UUID id,
        String email,
        String callingCode,
        String phoneNumber,
        String firstName,
        String lastName,
        String description,
        String profilePictureURL,
        String role
) { }
