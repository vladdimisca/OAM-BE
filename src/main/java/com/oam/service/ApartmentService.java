package com.oam.service;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.ForbiddenException;
import com.oam.exception.model.NotFoundException;
import com.oam.model.*;
import com.oam.repository.ApartmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.oam.model.AssociationRole.ADMIN;

@Service
@RequiredArgsConstructor
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final AssociationService associationService;
    private final UserService userService;
    private final SecurityService securityService;

    public Apartment create(Apartment apartment) {
        Association association = associationService.getById(apartment.getAssociation().getId());
        User user = userService.getById(securityService.getUserId());
        if (!isUserAnAdminAssociationMember(user, association)) {
            throw new ForbiddenException(ErrorMessage.NOT_ALLOWED_TO_CREATE_APARTMENTS);
        }
        apartment.setAssociation(association);
        apartment.setCode(generateUniqueApartmentCode());
        return apartmentRepository.save(apartment);
    }

    public Apartment updateById(UUID id, Apartment apartment) {
        User user = userService.getById(securityService.getUserId());
        Apartment existingApartment = getById(id);
        checkUserIsAssociationMemberWithAdminRights(user, existingApartment.getAssociation());
        existingApartment.setNumber(apartment.getNumber());
        existingApartment.setNumberOfPersons(apartment.getNumberOfPersons());
        existingApartment.setSurface(apartment.getSurface());

        return apartmentRepository.save(existingApartment);
    }

    public Apartment getById(UUID id) {
        return apartmentRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ErrorMessage.NOT_FOUND, "apartment", id));
    }

    public List<Apartment> getAll(UUID associationId) {
        if (associationId != null) {
            return apartmentRepository.findAllByAssociation_Id(associationId);
        }
        User user = userService.getById(securityService.getUserId());
        return apartmentRepository.findAllByUserId(user.getId());
    }

    @Transactional
    public void deleteById(UUID id) {
        User user = userService.getById(securityService.getUserId());
        Apartment apartment = getById(id);
        checkUserIsAssociationMemberWithAdminRights(user, apartment.getAssociation());

        apartmentRepository.deleteApartmentMembersByApartmentId(apartment.getId());
        apartment.getAssociationMembers().clear();

        apartmentRepository.delete(apartment);
    }

    @Transactional
    public void leave(UUID apartmentId) {
        Apartment apartment = getById(apartmentId);
        User user = userService.getById(securityService.getUserId());
        apartment.getAssociationMembers().removeIf(am -> am.getMember().getId().equals(user.getId()));
        apartment.getAssociation().getAssociationMembers().removeIf(am -> am.getMember().getId().equals(user.getId()));
        apartmentRepository.save(apartment);
    }

    private boolean isUserAnAdminAssociationMember(User user, Association association) {
        return user.getAssociationMembers().stream().anyMatch(
                            member -> member.getRole() == AssociationRole.ADMIN
                            && member.getAssociation().getId().equals(association.getId()));
    }

    private void checkUserIsAssociationMemberWithAdminRights(User user, Association association) {
        Optional<AssociationMember> associationMember = getAssociationMember(user, association, ADMIN);
        if (associationMember.isEmpty()) {
            throw new ForbiddenException(ErrorMessage.MUST_BE_ADMIN_ASSOCIATION_MEMBER);
        }
    }

    private String generateUniqueApartmentCode() {
        String uuidNumber = String.format("%010d", new BigInteger(UUID.randomUUID().toString().replace("-", ""),16));
        return uuidNumber.substring( uuidNumber.length() - 6);
    }

    private Optional<AssociationMember> getAssociationMember(User user, Association association, AssociationRole role) {
        return user.getAssociationMembers().stream()
                .filter(associationMember -> associationMember.getAssociation().equals(association))
                .filter(associationMember -> Boolean.TRUE.equals(associationMember.getRole() == role))
                .findFirst();
    }
}
