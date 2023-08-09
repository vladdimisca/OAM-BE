package com.oam.dto;

import java.util.UUID;

public record InvoiceDistributionResponseDto(
        UUID id,
        Double amount,
        ApartmentResponseDto apartment,
        InvoiceResponseDto invoice,
        SimplifiedPaymentResponseDto payment
) {
}
