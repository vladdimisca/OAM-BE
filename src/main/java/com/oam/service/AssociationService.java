package com.oam.service;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.BadRequestException;
import com.oam.exception.model.ForbiddenException;
import com.oam.exception.model.NotFoundException;
import com.oam.model.*;
import com.oam.repository.AssociationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.oam.exception.ErrorMessage.CANNOT_REMOVE_LAST_ADMIN;
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

        associationRepository.deleteAssociationMembersByAssociationId(association.getId());
        association.getAssociationMembers().clear();

        associationRepository.delete(association);
    }

    public void join(String code) {
        Association correspondingAssociation = associationRepository.findAssociationByApartmentCode(code)
                .orElseThrow(() -> new BadRequestException(ErrorMessage.INVALID_ASSOCIATION_CODE));
        User user = userService.getById(securityService.getUserId());
        if (getAssociationMember(user, correspondingAssociation, MEMBER).isPresent()) {
            return;
        }
        AssociationMember associationMember = createAssociationMember(user, correspondingAssociation, MEMBER);
        correspondingAssociation.getApartments().stream()
                .filter(apartment -> apartment.getCode().equals(code))
                .findFirst()
                .ifPresent((apartment) -> {
                    associationMember.setApartment(apartment);
                    apartment.getAssociationMembers().add(associationMember);
                });
        correspondingAssociation.getAssociationMembers().add(associationMember);
        associationRepository.save(correspondingAssociation);
    }

    @Transactional
    public void removeMemberFromAssociation(UUID userId, UUID associationId) {
        User authenticatedUser = userService.getById(securityService.getUserId());
        Association association = getById(associationId);
        checkUserIsAssociationMemberWithAdminRights(authenticatedUser, association);
        User user = userService.getById(userId);
        if (association.getAssociationMembers().stream().filter(am -> am.getRole() == ADMIN).count() == 1
            && getAssociationMember(user, association, ADMIN).isPresent()) {
            throw new ForbiddenException(CANNOT_REMOVE_LAST_ADMIN);
        }
        for (AssociationMember associationMember : association.getAssociationMembers()) {
            if (associationMember.getMember().getId().equals(user.getId())) {
                if (associationMember.getApartment() != null) {
                    associationMember.getApartment().setCode(generateUniqueApartmentCode());
                    associationMember.getApartment().getAssociationMembers().remove(associationMember);
                }
                association.getAssociationMembers().remove(associationMember);
            }
        }
        associationRepository.save(association);
    }

    @Transactional
    public void addAdminMember(String email, UUID associationId) {
        User user = userService.getByEmail(email);
        if (user == null) {
            throw new NotFoundException(ErrorMessage.EMAIL_NOT_ASSOCIATED);
        }
        Association association = getById(associationId);
        if (getAssociationMember(user, association, ADMIN).isPresent()) {
            return;
        }
        AssociationMember associationMember = createAssociationMember(user, association, ADMIN);
        association.getAssociationMembers().add(associationMember);
        associationRepository.save(association);
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

    private String generateUniqueApartmentCode() {
        String uuidNumber = String.format("%010d", new BigInteger(UUID.randomUUID().toString().replace("-", ""),16));
        return uuidNumber.substring( uuidNumber.length() - 6);
    }

    private void copyValues(Association from, Association to) {
        to.setCountry(from.getCountry());
        to.setAdministrativeArea(from.getAdministrativeArea());
        to.setLocality(from.getLocality());
        to.setNumber(from.getNumber());
        to.setZipCode(from.getZipCode());
        to.setBlock(from.getBlock());
        to.setStaircase(from.getStaircase());
        to.setLocality(from.getLocality());
        to.setStreet(from.getStreet());
        to.setIban(from.getIban());
    }
}
