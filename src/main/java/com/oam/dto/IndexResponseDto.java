package com.oam.dto;

import com.oam.model.InvoiceType;

import java.util.UUID;

public record IndexResponseDto(
        UUID id,
        Double oldIndex,
        Double newIndex,
        Integer month,
        Integer year,
        InvoiceType type,
        ApartmentResponseDto apartment
) {
}
