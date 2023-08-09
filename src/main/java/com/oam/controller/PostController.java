package com.oam.controller;

import com.oam.dto.CreatePostRequestDto;
import com.oam.dto.PostResponseDto;
import com.oam.mapper.PostMapper;
import com.oam.model.Post;
import com.oam.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    @PostMapping
    public ResponseEntity<PostResponseDto> create(@Valid @RequestBody CreatePostRequestDto createPostRequestDto) throws Exception {
        Post post = postService.create(postMapper.mapToEntity(createPostRequestDto));
        return ResponseEntity.ok(postMapper.mapToDto(post));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getById(@PathVariable("id") UUID id) {
        Post post = postService.getById(id);
        return ResponseEntity.ok(postMapper.mapToDto(post));
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAll() {
        List<Post> posts = postService.getAll();
        return ResponseEntity.ok(posts.stream().map(postMapper::mapToDto).toList());
    }
}
