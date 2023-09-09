package com.oam.dto;

import com.oam.model.InvoiceMethod;
import com.oam.model.InvoiceType;
import jakarta.validation.constraints.*;

public record CreateInvoiceRequestDto (

        @NotNull(message = "The month is mandatory!")
        @Min(value = 1, message = "The month must be between 1 and 12")
        @Max(value = 12, message = "The month must be between 1 and 12")
        Integer month,

        @NotNull(message = "The year is mandatory!")
        @Positive(message = "The year must be a positive value!")
        Integer year,

        @NotBlank(message = "The name is mandatory!")
        String name,

        @NotBlank(message = "The number is mandatory!")
        String number,

        @NotNull(message = "The amount is mandatory!")
        @Positive(message = "The amount must be a positive value!")
        Double amount,

        @NotNull(message = "The type is mandatory!")
        InvoiceType type,

        @NotNull(message = "The method is mandatory!")
        InvoiceMethod method,

        Double pricePerIndexUnit,

        @NotBlank(message = "The association is mandatory!")
        String associationId
) {}
