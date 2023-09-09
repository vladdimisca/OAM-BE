package com.oam.dto;

import com.oam.model.InvoiceType;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record CreateIndexRequestDto (

        @NotNull(message = "The old index is mandatory!")
        @Positive(message = "The old index must be a positive value!")
        Double oldIndex,

        @NotNull(message = "The new index is mandatory!")
        @Positive(message = "The new index must be a positive value!")
        Double newIndex,

        @NotNull(message = "The month is mandatory!")
        @Min(value = 1, message = "The month must be between 1 and 12")
        @Max(value = 12, message = "The month must be between 1 and 12")
        Integer month,

        @NotNull(message = "The year is mandatory!")
        @Positive(message = "The year must be a positive value!")
        Integer year,

        @NotNull(message = "The type is mandatory!")
        InvoiceType type,

        @NotNull(message = "The apartment is mandatory!")
        UUID apartmentId
) {
}
