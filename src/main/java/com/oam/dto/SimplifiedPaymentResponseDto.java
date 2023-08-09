package com.oam.dto;

import com.oam.model.PaymentStatus;

import java.util.UUID;

public record SimplifiedPaymentResponseDto(
        UUID id,
        PaymentStatus status
) { }
