package com.oam.controller;

import com.oam.dto.CommentResponseDto;
import com.oam.dto.CreateCommentRequestDto;
import com.oam.dto.UpdateCommentRequestDto;
import com.oam.dto.UpdatePostRequestDto;
import com.oam.mapper.CommentMapper;
import com.oam.model.Comment;
import com.oam.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping
    public ResponseEntity<CommentResponseDto> create(@Valid @RequestBody CreateCommentRequestDto createCommentRequestDto) throws Exception {
        Comment comment = commentService.create(commentMapper.mapToEntity(createCommentRequestDto));
        return ResponseEntity.ok(commentMapper.mapToDto(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> update(@PathVariable("id") UUID id,
                                                  @Valid @RequestBody UpdateCommentRequestDto updateCommentRequestDto) {
        Comment comment = commentService.updateById(id, commentMapper.mapToEntity(updateCommentRequestDto));
        return ResponseEntity.ok(commentMapper.mapToDto(comment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getById(@PathVariable("id") UUID id) {
        Comment comment = commentService.getById(id);
        return ResponseEntity.ok(commentMapper.mapToDto(comment));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getAll(@RequestParam(value = "postId", required = false) UUID postId) {
        List<Comment> comments = commentService.getAll(postId);
        return ResponseEntity.ok(comments.stream().map(commentMapper::mapToDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") UUID id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
