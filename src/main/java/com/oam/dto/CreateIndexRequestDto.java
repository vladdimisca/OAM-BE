package com.oam.dto;

import com.oam.model.InvoiceType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateIndexRequestDto (

        @NotNull(message = "Old index is mandatory!")
        Double oldIndex,

        @NotNull(message = "New index is mandatory!")
        Double newIndex,

        @NotNull(message = "Month is mandatory!")
        Integer month,

        @NotNull(message = "Year is mandatory!")
        Integer year,

        @NotNull(message = "Type is mandatory!")
        InvoiceType type,

        @NotNull(message = "Apartment is mandatory!")
        UUID apartmentId
) {
}
