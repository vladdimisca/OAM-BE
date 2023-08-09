package com.oam.controller;

import com.oam.dto.CreateIndexRequestDto;
import com.oam.dto.IndexResponseDto;
import com.oam.mapper.IndexMapper;
import com.oam.model.Index;
import com.oam.service.IndexService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/indexes")
@RequiredArgsConstructor
public class IndexController {

    private final IndexMapper indexMapper;
    private final IndexService indexService;

    @PostMapping
    public ResponseEntity<IndexResponseDto> create(@Valid @RequestBody CreateIndexRequestDto createIndexRequestDto) {
        Index index = indexService.create(indexMapper.mapToEntity(createIndexRequestDto));
        return ResponseEntity
                .created(URI.create("/api/indexes/" + index.getId()))
                .body(indexMapper.mapToDto(index));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndexResponseDto> getById(@PathVariable("id") UUID id) {
        Index index = indexService.getById(id);
        return ResponseEntity.ok(indexMapper.mapToDto(index));
    }

    @GetMapping
    public ResponseEntity<List<IndexResponseDto>> getAll() {
        List<Index> indexes = indexService.getAll();
        return ResponseEntity.ok(indexes.stream().map(indexMapper::mapToDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") UUID id) {
        indexService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
