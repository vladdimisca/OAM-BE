package com.oam.service;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.NotFoundException;
import com.oam.model.*;
import com.oam.repository.InvoiceDistributionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceDistributionService {

    private final InvoiceDistributionRepository invoiceDistributionRepository;
    private final SecurityService securityService;
    private final UserService userService;

    @Transactional
    public void distribute(Invoice invoice) {
        List<Apartment> apartments = invoice.getAssociation().getApartments();
        Double amountPerUnit = getAmountPerUnit(invoice);
        for (Apartment apartment : apartments) {
            InvoiceDistribution invoiceDistribution = new InvoiceDistribution();
            invoiceDistribution.setInvoice(invoice);
            invoiceDistribution.setApartment(apartment);
            Double amountPerApartment = BigDecimal.valueOf(getAmountPerApartment(apartment, invoice, amountPerUnit))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
            invoiceDistribution.setAmount(amountPerApartment);
            invoiceDistributionRepository.save(invoiceDistribution);
        }
    }

    private Double getAmountPerApartment(Apartment apartment, Invoice invoice, Double amountPerUnit) {
        Double numberOfUnits = getNumberOfUnits(apartment, invoice);
        if (invoice.getMethod() == InvoiceMethod.PER_COUNTER) {
            if (numberOfUnits == null) {
                Double indexAmount = getIndexAmount(invoice, amountPerUnit);
                long numberOfRemainingApartments = getNumberOfRemainingApartments(invoice);
                return (invoice.getAmount() - indexAmount) / numberOfRemainingApartments;
            }
        }
        return amountPerUnit * numberOfUnits;
    }

    private Double getIndexAmount(Invoice invoice, Double amountPerUnit) {
        return invoice.getAssociation().getApartments().stream()
                .flatMap(ap -> ap.getIndexes().stream())
                .filter(i -> i.getMonth().equals(invoice.getMonth()))
                .filter(i -> i.getYear().equals(invoice.getYear()))
                .filter(i -> i.getType().equals(invoice.getType()))
                .map(i -> i.getNewIndex() - i.getOldIndex())
                .mapToDouble(units -> units * amountPerUnit)
                .sum();
    }

    private long getNumberOfRemainingApartments(Invoice invoice) {
        long paidApartments = invoice.getAssociation().getApartments().stream()
                .flatMap(ap -> ap.getIndexes().stream())
                .filter(i -> i.getMonth().equals(invoice.getMonth()))
                .filter(i -> i.getYear().equals(invoice.getYear()))
                .filter(i -> i.getType().equals(invoice.getType()))
                .count();
        return invoice.getAssociation().getApartments().size() - paidApartments;
    }

    private Double getAmountPerUnit(Invoice invoice) {
        return switch (invoice.getMethod()) {
            case PER_PERSON -> getAmountPerPerson(invoice);
            case PER_COUNTER -> invoice.getPricePerIndexUnit();
            case PER_APARTMENT -> getAmountPerSquaredMeter(invoice);
        };
    }

    private Double getNumberOfUnits(Apartment apartment, Invoice invoice) {
        return switch (invoice.getMethod()) {
            case PER_PERSON -> (double) apartment.getNumberOfPersons();
            case PER_COUNTER -> getApartmentIndex(apartment, invoice);
            case PER_APARTMENT -> apartment.getSurface();
        };
    }

    private Double getApartmentIndex(Apartment apartment, Invoice invoice) {
        List<Index> indexes = apartment.getIndexes().stream()
                .filter(index -> index.getMonth().equals(invoice.getMonth()))
                .filter(index -> index.getYear().equals(invoice.getYear()))
                .filter(index -> index.getType().equals(invoice.getType()))
                .toList();
        if (indexes.isEmpty()) {
            return null;
        }
        return indexes.stream()
                .map(index -> index.getNewIndex() - index.getOldIndex())
                .mapToDouble(i -> i)
                .sum();
    }

    private Double getAmountPerPerson(Invoice invoice) {
        Integer totalNumberOfPersons = invoice.getAssociation().getApartments().stream()
                .map(Apartment::getNumberOfPersons)
                .mapToInt(np -> np)
                .sum();
        return invoice.getAmount() / totalNumberOfPersons;
    }

    private Double getAmountPerSquaredMeter(Invoice invoice) {
        double totalSurface = invoice.getAssociation().getApartments().stream()
                .map(Apartment::getSurface)
                .mapToDouble(s -> s)
                .sum();
        return invoice.getAmount() / totalSurface;
    }

    public InvoiceDistribution getById(UUID id) {
        return invoiceDistributionRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ErrorMessage.NOT_FOUND, "index", id));
    }

    public List<InvoiceDistribution> getAll() {
        User user = userService.getById(securityService.getUserId());
        return invoiceDistributionRepository.findAllByUserId(user.getId());
    }
}
