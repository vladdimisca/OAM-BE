package com.oam.dto;

import com.oam.model.InvoiceMethod;
import com.oam.model.InvoiceType;

public record CreateInvoiceRequestDto (

        Integer month,

        Integer year,

        String name,

        String number,

        Double amount,

        InvoiceType type,

        InvoiceMethod method,

        String associationId
) {}
