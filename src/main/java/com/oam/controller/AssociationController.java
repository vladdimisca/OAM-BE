package com.oam.controller;

import com.oam.dto.AssociationResponseDto;
import com.oam.dto.CreateAssociationRequestDto;
import com.oam.dto.UpdateAssociationRequestDto;
import com.oam.mapper.AssociationMapper;
import com.oam.model.Association;
import com.oam.model.AssociationRole;
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

    @PutMapping("/{id}")
    public ResponseEntity<AssociationResponseDto> update(@PathVariable("id") UUID id,
                                                         @Valid @RequestBody UpdateAssociationRequestDto updateAssociationRequestDto) {
        Association association = associationService.updateById(id, associationMapper.mapToEntity(updateAssociationRequestDto));
        return ResponseEntity.ok(associationMapper.mapToDto(association));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssociationResponseDto> getById(@PathVariable("id") UUID id) {
        Association association = associationService.getById(id);
        return ResponseEntity.ok(associationMapper.mapToDto(association));
    }

    @GetMapping
    public ResponseEntity<List<AssociationResponseDto>> getAll(@RequestParam(value = "role", required = false) AssociationRole role) {
        List<Association> associations = associationService.getAll(role);
        return ResponseEntity.ok(associations.stream().map(associationMapper::mapToDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") UUID id) {
        associationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestParam("code") String code) {
        associationService.join(code);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/removeMember/{userId}")
    public ResponseEntity<?> removeUserFromAssociation(@PathVariable("id") UUID id,
                                                       @PathVariable("userId") UUID userId) {
        associationService.removeMemberFromAssociation(userId, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/addAdmin/{email}")
    public ResponseEntity<?> assAdminMember(@PathVariable("id") UUID id,
                                            @PathVariable("email") String email) {
        associationService.addAdminMember(email, id);
        return ResponseEntity.noContent().build();
    }
}
