package com.oam.service;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.ForbiddenException;
import com.oam.exception.model.NotFoundException;
import com.oam.model.*;
import com.oam.repository.CommentRepository;
import com.oam.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    private final SecurityService securityService;

    public Comment create(Comment comment) {
        Post post = postService.getById(comment.getPost().getId());
        User user = userService.getById(securityService.getUserId());
        if (getAssociationMember(user, post.getAssociation()).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.MUST_BE_ASSOCIATION_MEMBER);
        }
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public Comment getById(UUID id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ErrorMessage.NOT_FOUND, "comment", id));
    }

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    private Optional<AssociationMember> getAssociationMember(User user, Association association) {
        return user.getAssociationMembers().stream()
                .filter(associationMember -> associationMember.getAssociation().equals(association))
                .findFirst();
    }
}
