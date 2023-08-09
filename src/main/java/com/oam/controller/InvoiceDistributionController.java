package com.oam.controller;

import com.oam.dto.InvoiceDistributionResponseDto;
import com.oam.mapper.InvoiceDistributionMapper;
import com.oam.model.InvoiceDistribution;
import com.oam.service.InvoiceDistributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoiceDistributions")
@RequiredArgsConstructor
public class InvoiceDistributionController {

    private final InvoiceDistributionService invoiceDistributionService;
    private final InvoiceDistributionMapper invoiceDistributionMapper;

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDistributionResponseDto> getById(@PathVariable("id") UUID id) {
        InvoiceDistribution invoiceDistribution = invoiceDistributionService.getById(id);
        return ResponseEntity.ok(invoiceDistributionMapper.mapToDto(invoiceDistribution));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDistributionResponseDto>> getAll() {
        List<InvoiceDistribution> invoiceDistributions = invoiceDistributionService.getAll();
        return ResponseEntity.ok(invoiceDistributions.stream().map(invoiceDistributionMapper::mapToDto).toList());
    }
}
