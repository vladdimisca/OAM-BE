package com.oam.dto;

import com.oam.model.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PaymentResponseDto(
        UUID id,
        Double amount,
        PaymentStatus status,
        LocalDateTime createdAt,
        UserResponseDto user,
        List<InvoiceDistributionResponseDto> invoiceDistributions
) {
}
