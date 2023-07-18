package com.oam.controller;

import com.oam.dto.AssociationResponseDto;
import com.oam.dto.CreateAssociationRequestDto;
import com.oam.mapper.AssociationMapper;
import com.oam.model.Association;
import com.oam.service.AssociationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/associations")
@RequiredArgsConstructor
public class AssociationController {

    private final AssociationService associationService;
    private final AssociationMapper associationMapper;

    @PostMapping
    public ResponseEntity<AssociationResponseDto> create(@Valid @RequestBody CreateAssociationRequestDto createAssociationRequestDto) {
        Association association = associationService.create(associationMapper.mapToEntity(createAssociationRequestDto));
        return ResponseEntity
                .created(URI.create("/api/associations/" + association.getId()))
                .body(associationMapper.mapToDto(association));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssociationResponseDto> getById(@PathVariable("id") UUID id) {
        Association association = associationService.getById(id);
        return ResponseEntity.ok(associationMapper.mapToDto(association));
    }

    @GetMapping
    public ResponseEntity<List<AssociationResponseDto>> getAll() {
        List<Association> associations = associationService.getAll();
        return ResponseEntity.ok(associations.stream().map(associationMapper::mapToDto).toList());
    }
}
