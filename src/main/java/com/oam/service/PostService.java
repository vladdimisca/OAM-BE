package com.oam.service;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.ForbiddenException;
import com.oam.exception.model.NotFoundException;
import com.oam.model.Association;
import com.oam.model.AssociationMember;
import com.oam.model.Post;
import com.oam.model.User;
import com.oam.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AssociationService associationService;
    private final UserService userService;
    private final SecurityService securityService;
    private final ChatGptService chatGptService;

    public Post create(Post post) {
        Association association = associationService.getById(post.getAssociation().getId());
        User user = userService.getById(securityService.getUserId());
        if (getAssociationMember(user, association).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.MUST_BE_ASSOCIATION_MEMBER);
        }
        post.setAssociation(association);
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setSummary(chatGptService.getSummary(post.getText()));
        return postRepository.save(post);
    }

    public Post updateById(UUID id, Post post) {
        User user = userService.getById(securityService.getUserId());
        Post existingPost = getById(id);
        if (!existingPost.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN);
        }
        if (!existingPost.getText().equals(post.getText())) {
            existingPost.setSummary(chatGptService.getSummary(post.getText()));
        }
        existingPost.setText(post.getText());
        existingPost.setTitle(post.getTitle());
        return postRepository.save(existingPost);
    }

    public Post getById(UUID id) {
        User user = userService.getById(securityService.getUserId());
        Post post = postRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ErrorMessage.NOT_FOUND, "post", id));
        if (getAssociationMember(user, post.getAssociation()).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN);
        }
        return post;
    }

    public List<Post> getAll() {
        User user = userService.getById(securityService.getUserId());
        return postRepository.findAllByUserId(user.getId());
    }

    public void deleteById(UUID id) {
        User user = userService.getById(securityService.getUserId());
        Post post = getById(id);
        if (!post.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN);
        }
        postRepository.delete(post);
    }

    private Optional<AssociationMember> getAssociationMember(User user, Association association) {
        return user.getAssociationMembers().stream()
                .filter(associationMember -> associationMember.getAssociation().equals(association))
                .findFirst();
    }
}
