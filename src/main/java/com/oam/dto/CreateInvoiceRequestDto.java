package com.oam.dto;

import com.oam.model.InvoiceMethod;
import com.oam.model.InvoiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateInvoiceRequestDto (

        @NotNull(message = "Month is mandatory!")
        @Pattern(regexp = "^(1[0-2]|[1-9])$", message = "Month must be between 1 and 12!")
        Integer month,

        @NotNull(message = "Year is mandatory!")
        Integer year,

        @NotBlank(message = "Name is mandatory!")
        String name,

        @NotBlank(message = "Number is mandatory!")
        String number,

        @NotNull(message = "Amount is mandatory!")
        Double amount,

        @NotNull(message = "Type is mandatory!")
        InvoiceType type,

        @NotNull(message = "Method is mandatory!")
        InvoiceMethod method,

        Double pricePerIndexUnit,

        @NotBlank(message = "Association is mandatory!")
        String associationId
) {}
