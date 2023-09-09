package com.oam.controller;

import com.oam.dto.*;
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

    @PutMapping("/{id}")
    public ResponseEntity<ApartmentResponseDto> update(@PathVariable("id") UUID id,
                                                       @Valid @RequestBody UpdateApartmentRequestDto updateApartmentRequestDto) {
        Apartment apartment = apartmentService.updateById(id, apartmentMapper.mapToEntity(updateApartmentRequestDto));
        return ResponseEntity.ok(apartmentMapper.mapToDto(apartment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApartmentResponseDto> getById(@PathVariable("id") UUID id) {
        Apartment apartment = apartmentService.getById(id);
        return ResponseEntity.ok(apartmentMapper.mapToDto(apartment));
    }

    @GetMapping
    public ResponseEntity<List<ApartmentResponseDto>> getAll(@RequestParam(value = "associationId", required = false) UUID associationId,
                                                             @RequestParam(value = "asMember", required = false) Boolean asMember) {
        List<Apartment> apartments = apartmentService.getAll(associationId, asMember);
        return ResponseEntity.ok(apartments.stream().map(apartmentMapper::mapToDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") UUID id) {
        apartmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<?> leaveApartmentById(@PathVariable("id") UUID id) {
        apartmentService.leaveApartmentById(id);
        return ResponseEntity.noContent().build();
    }
}
