package com.oam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oam.dto.CreateInvoiceRequestDto;
import com.oam.dto.InvoiceResponseDto;
import com.oam.mapper.InvoiceMapper;
import com.oam.model.Invoice;
import com.oam.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceMapper invoiceMapper;

    @PostMapping
    public ResponseEntity<InvoiceResponseDto> create(
            @Valid @RequestPart("invoice") String createInvoiceRequestDto,
            @RequestPart(value = "document", required = false) MultipartFile document) throws IOException {
        CreateInvoiceRequestDto invoiceRequestDto = new ObjectMapper().readValue(createInvoiceRequestDto, CreateInvoiceRequestDto.class);
        Invoice invoice = invoiceService.create(invoiceMapper.mapToEntity(invoiceRequestDto), document);
        return ResponseEntity
                .created(URI.create("/api/invoices/" + invoice.getId()))
                .body(invoiceMapper.mapToDto(invoice));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> getById(@PathVariable("id") UUID id) {
        Invoice invoice = invoiceService.getById(id);
        return ResponseEntity.ok(invoiceMapper.mapToDto(invoice));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceResponseDto>> getAll() {
        List<Invoice> invoices = invoiceService.getAll();
        return ResponseEntity.ok(invoices.stream().map(invoiceMapper::mapToDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") UUID id) {
        invoiceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
