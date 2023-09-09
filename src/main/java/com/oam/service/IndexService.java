package com.oam.service;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.BadRequestException;
import com.oam.exception.model.ConflictException;
import com.oam.exception.model.ForbiddenException;
import com.oam.exception.model.NotFoundException;
import com.oam.model.*;
import com.oam.repository.IndexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IndexService {

    private final IndexRepository indexRepository;
    private final ApartmentService apartmentService;
    private final SecurityService securityService;
    private final UserService userService;

    public Index create(Index index) {
        if (index.getOldIndex() > index.getNewIndex()) {
            throw new BadRequestException(ErrorMessage.NEW_INDEX_MUST_BE_GREATER_THAN_OLD_INDEX);
        }
        User user = userService.getById(securityService.getUserId());
        Apartment apartment = apartmentService.getById(index.getApartment().getId());
        if (getApartmentMember(user, index.getApartment()).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN);
        }
        index.setUser(user);
        index.setApartment(apartment);
        if (isIndexAlreadyUploaded(apartment, index)) {
            throw new ConflictException(ErrorMessage.INDEX_ALREADY_UPLOADED);
        }
        return indexRepository.save(index);
    }

    public Index getById(UUID id) {
        User user = userService.getById(securityService.getUserId());
        Index index = indexRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ErrorMessage.NOT_FOUND, "index", id));
        if (getApartmentMember(user, index.getApartment()).isEmpty()
            && getAssociationMember(user, index.getApartment().getAssociation(), AssociationRole.ADMIN).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN);
        }
        return index;
    }

    public List<Index> getAll() {
        User user = userService.getById(securityService.getUserId());
        return indexRepository.findAllByUserId(user.getId());
    }

    public void deleteById(UUID id) {
        User user = userService.getById(securityService.getUserId());
        Index index = getById(id);
        if (getApartmentMember(user, index.getApartment()).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN);
        }
        checkInvoiceNotUploaded(index);
        indexRepository.delete(index);
    }

    private Optional<AssociationMember> getApartmentMember(User user, Apartment apartment) {
        return user.getAssociationMembers().stream()
                .filter(associationMember -> associationMember.getRole() == AssociationRole.MEMBER)
                .filter(associationMember -> associationMember.getApartment().equals(apartment))
                .findFirst();
    }

    private void checkInvoiceNotUploaded(Index index) {
        if (isInvoiceAlreadyUploaded(index)) {
            throw new ForbiddenException(ErrorMessage.CANNOT_DELETE_INDEX);
        }
    }

    private boolean isIndexAlreadyUploaded(Apartment apartment, Index index) {
        return !apartment.getIndexes().stream()
                .filter(i -> i.getType() == index.getType())
                .filter(i -> Objects.equals(i.getMonth(), index.getMonth()))
                .filter(i -> Objects.equals(i.getYear(), index.getYear()))
                .toList().isEmpty();
    }

    private boolean isInvoiceAlreadyUploaded(Index index) {
        return !index.getApartment().getAssociation().getInvoices().stream()
                .filter(invoice -> invoice.getMethod() == InvoiceMethod.PER_COUNTER)
                .filter(invoice -> invoice.getType() == index.getType())
                .filter(invoice -> invoice.getMonth().equals(index.getMonth()))
                .filter(invoice -> invoice.getYear().equals(index.getYear()))
                .toList().isEmpty();
    }

    private Optional<AssociationMember> getAssociationMember(User user, Association association, AssociationRole role) {
        return user.getAssociationMembers().stream()
                .filter(associationMember -> associationMember.getAssociation().equals(association))
                .filter(associationMember -> role == null || associationMember.getRole().equals(role))
                .findFirst();
    }
}
