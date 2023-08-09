package com.oam.controller;

import com.oam.dto.PaymentIntentResponseDto;
import com.oam.dto.PaymentResponseDto;
import com.oam.mapper.PaymentMapper;
import com.oam.model.Payment;
import com.oam.service.PaymentService;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @PostMapping
    public ResponseEntity<PaymentIntentResponseDto> create(@RequestBody Set<UUID> invoiceDistributionIds) throws Exception {
        PaymentIntent paymentIntent = paymentService.create(invoiceDistributionIds);
        return ResponseEntity.ok(new PaymentIntentResponseDto(paymentIntent.getClientSecret()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getById(@PathVariable("id") UUID id) {
        Payment payment = paymentService.getById(id);
        return ResponseEntity.ok(paymentMapper.mapToDto(payment));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getAll() {
        List<Payment> payments = paymentService.getAll();
        return ResponseEntity.ok(payments.stream().map(paymentMapper::mapToDto).toList());
    }
}
