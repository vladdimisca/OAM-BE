package com.oam.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oam.dto.CreateInvoiceRequestDto;
import com.oam.dto.InvoiceResponseDto;
import com.oam.dto.StatisticsResponseDto;
import com.oam.exception.ErrorEntity;
import com.oam.exception.ErrorResponse;
import com.oam.mapper.InvoiceMapper;
import com.oam.model.Invoice;
import com.oam.model.Statistics;
import com.oam.service.InvoiceService;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    private final SmartValidator validator;

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestPart("invoice") String createInvoiceRequestDto,
            @RequestPart(value = "document", required = false) MultipartFile document,
            BindingResult result) throws IOException, MethodArgumentNotValidException {
        CreateInvoiceRequestDto invoiceRequestDto = new ObjectMapper().readValue(createInvoiceRequestDto, CreateInvoiceRequestDto.class);
        validator.validate(invoiceRequestDto, result);
        if (result.hasErrors()) {
            return handleBindingResult(result);
        }
        Invoice invoice = invoiceService.create(invoiceMapper.mapToEntity(invoiceRequestDto), document);
        return ResponseEntity
                .created(URI.create("/api/invoices/" + invoice.getId()))
                .body(invoiceMapper.mapToDto(invoice));
    }

    private ResponseEntity<ErrorResponse> handleBindingResult(BindingResult bindingResult) {
        List<ErrorEntity> errors = bindingResult.getFieldErrors().stream()
                .map(e -> new ErrorEntity(-1, e.getDefaultMessage(), e.getField()))
                .toList();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(getMultipleErrorsResponse(errors));
    }

    private ErrorResponse getMultipleErrorsResponse(List<ErrorEntity> errors) {
        return new ErrorResponse(errors);
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

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponseDto> getStatistics() {
        Statistics statistics = invoiceService.getStatistics();
        return ResponseEntity.ok(new StatisticsResponseDto(statistics.getLabels(), statistics.getData()));
    }
}
