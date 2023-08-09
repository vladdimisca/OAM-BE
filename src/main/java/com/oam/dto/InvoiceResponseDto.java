package com.oam.dto;

import com.oam.model.InvoiceMethod;
import com.oam.model.InvoiceType;

import java.util.UUID;

public record InvoiceResponseDto (

        UUID id,
        String documentUrl,
        Integer month,
        Integer year,
        String name,
        String number,
        Double amount,
        InvoiceType type,
        InvoiceMethod method,
        Double pricePerIndexUnit,
        AssociationResponseDto association
) { }
