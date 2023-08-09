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

    public Post getById(UUID id) {
        return postRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ErrorMessage.NOT_FOUND, "post", id));
    }

    public List<Post> getAll() {
        return postRepository.findAll();
    }

    private Optional<AssociationMember> getAssociationMember(User user, Association association) {
        return user.getAssociationMembers().stream()
                .filter(associationMember -> associationMember.getAssociation().equals(association))
                .findFirst();
    }
}
