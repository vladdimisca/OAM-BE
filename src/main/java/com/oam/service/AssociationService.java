package com.oam.service;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.NotFoundException;
import com.oam.model.Association;
import com.oam.model.AssociationMember;
import com.oam.model.AssociationRole;
import com.oam.model.User;
import com.oam.repository.AssociationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssociationService {

    private final AssociationRepository associationRepository;
    private final UserService userService;
    private final SecurityService securityService;

    public Association create(Association association) {
        User user = userService.getById(securityService.getUserId());
        AssociationMember associationMember = createAssociationMember(user, association, AssociationRole.ADMIN);
        association.getAssociationMembers().add(associationMember);

        return associationRepository.save(association);
    }

    public Association getById(UUID id) {
        return associationRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ErrorMessage.NOT_FOUND, "association", id));
    }

    public List<Association> getAll() {
        return associationRepository.findAll();
    }

    private AssociationMember createAssociationMember(User user, Association association, AssociationRole role) {
        AssociationMember associationMember = new AssociationMember();
        associationMember.setRole(role);
        associationMember.setMember(user);
        associationMember.setAssociation(association);
        return associationMember;
    }
}
