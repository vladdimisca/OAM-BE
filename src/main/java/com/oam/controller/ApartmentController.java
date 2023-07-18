package com.oam.controller;

import com.oam.dto.ApartmentResponseDto;
import com.oam.dto.CreateApartmentRequestDto;
import com.oam.mapper.ApartmentMapper;
import com.oam.model.Apartment;
import com.oam.service.ApartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;
    private final ApartmentMapper apartmentMapper;

    @PostMapping
    public ResponseEntity<ApartmentResponseDto> create(@Valid @RequestBody CreateApartmentRequestDto createApartmentRequestDto) {
        Apartment apartment = apartmentService.create(apartmentMapper.mapToEntity(createApartmentRequestDto));
        return ResponseEntity
                .created(URI.create("/api/apartments/" + apartment.getId()))
                .body(apartmentMapper.mapToDto(apartment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentResponseDto> getById(@PathVariable("id") UUID id) {
        Apartment apartment = apartmentService.getById(id);
        return ResponseEntity.ok(apartmentMapper.mapToDto(apartment));
    }

    @GetMapping
    public ResponseEntity<List<ApartmentResponseDto>> getAll() {
        List<Apartment> apartments = apartmentService.getAll();
        return ResponseEntity.ok(apartments.stream().map(apartmentMapper::mapToDto).toList());
    }
}
