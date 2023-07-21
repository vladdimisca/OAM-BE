package com.oam.service;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.ForbiddenException;
import com.oam.exception.model.NotFoundException;
import com.oam.model.*;
import com.oam.repository.AssociationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.oam.model.AssociationRole.ADMIN;
import static com.oam.model.AssociationRole.MEMBER;

@Service
@RequiredArgsConstructor
public class AssociationService {

    private final AssociationRepository associationRepository;
    private final UserService userService;
    private final SecurityService securityService;

    public Association create(Association association) {
        User user = userService.getById(securityService.getUserId());
        AssociationMember associationMember = createAssociationMember(user, association, ADMIN);
        association.getAssociationMembers().add(associationMember);

        return associationRepository.save(association);
    }

    public Association getById(UUID id) {
        return associationRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ErrorMessage.NOT_FOUND, "association", id));
    }

    public Association updateById(UUID id, Association association) {
        User user = userService.getById(securityService.getUserId());
        Association existingAssociation = getById(id);
        checkUserIsAssociationMemberWithAdminRights(user, existingAssociation);
        copyValues(association, existingAssociation);

        return associationRepository.save(existingAssociation);
    }

    public List<Association> getAll(AssociationRole role) {
        return associationRepository.findAllByUserIdAndRole(securityService.getUserId(), role);
    }

    @Transactional
    public void deleteById(UUID id) {
        User user = userService.getById(securityService.getUserId());
        Association association = getById(id);
        checkUserIsAssociationMemberWithAdminRights(user, association);

        associationRepository.deleteAssociationMembersByFamilyId(association.getId());
        association.getAssociationMembers().clear();

        associationRepository.delete(association);
    }

    public void join(String code) {
        Association correspondingAssociation = associationRepository.findAssociationByApartmentCode(code).orElseThrow();
        User user = userService.getById(securityService.getUserId());
        if (getAssociationMember(user, correspondingAssociation, MEMBER).isPresent()) {
            return;
        }
        AssociationMember associationMember = createAssociationMember(user, correspondingAssociation, MEMBER);
        correspondingAssociation.getApartments().stream()
                .filter(apartment -> apartment.getCode().equals(code))
                .findFirst()
                .ifPresent(associationMember::setApartment);
        correspondingAssociation.getAssociationMembers().add(associationMember);
        associationRepository.save(correspondingAssociation);
    }

    private AssociationMember createAssociationMember(User user, Association association, AssociationRole role) {
        AssociationMember associationMember = new AssociationMember();
        associationMember.setRole(role);
        associationMember.setMember(user);
        associationMember.setAssociation(association);
        return associationMember;
    }

    private void checkUserIsAssociationMemberWithAdminRights(User user, Association association) {
        Optional<AssociationMember> associationMember = getAssociationMember(user, association, ADMIN);
        if (associationMember.isEmpty()) {
            throw new ForbiddenException(ErrorMessage.MUST_BE_ADMIN_ASSOCIATION_MEMBER);
        }
    }

    private Optional<AssociationMember> getAssociationMember(User user, Association association, AssociationRole role) {
        return user.getAssociationMembers().stream()
                .filter(associationMember -> associationMember.getAssociation().equals(association))
                .filter(associationMember -> Boolean.TRUE.equals(associationMember.getRole() == role))
                .findFirst();
    }

    private void copyValues(Association from, Association to) {
        to.setCountry(from.getCountry());
        to.setAdministrativeArea(from.getAdministrativeArea());
        to.setLocality(from.getLocality());
        to.setNumber(from.getNumber());
        to.setZipCode(from.getZipCode());
        to.setStaircase(from.getStaircase());
        to.setLocality(from.getLocality());
        to.setStreet(from.getStreet());
    }
}
