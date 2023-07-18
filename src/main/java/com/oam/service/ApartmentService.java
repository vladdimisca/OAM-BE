package com.oam.service;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.ForbiddenException;
import com.oam.exception.model.NotFoundException;
import com.oam.model.Apartment;
import com.oam.model.Association;
import com.oam.model.AssociationRole;
import com.oam.model.User;
import com.oam.repository.ApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

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

    public Apartment getById(UUID id) {
        return apartmentRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ErrorMessage.NOT_FOUND, "apartment", id));
    }

    public List<Apartment> getAll() {
        return apartmentRepository.findAll();
    }

    private boolean isUserAnAdminAssociationMember(User user, Association association) {
        return user.getAssociationMembers().stream().noneMatch(
                            member -> member.getRole() == AssociationRole.ADMIN
                            && member.getAssociation().getId().equals(association.getId()));
    }

    private String generateUniqueApartmentCode() {
        String uuidNumber = String.format("%010d", new BigInteger(UUID.randomUUID().toString().replace("-", ""),16));
        return uuidNumber.substring( uuidNumber.length() - 6);
    }
}
