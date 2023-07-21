package com.oam.service;

import com.oam.model.Apartment;
import com.oam.model.Invoice;
import com.oam.model.InvoiceDistribution;
import com.oam.repository.InvoiceDistributionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceDistributionService {

    private final InvoiceDistributionRepository invoiceDistributionRepository;

    @Transactional
    public List<InvoiceDistribution> distribute(Invoice invoice) {
        List<InvoiceDistribution> invoiceDistributions = new ArrayList<>();
        List<Apartment> apartments = invoice.getAssociation().getApartments();
        for (Apartment apartment : apartments) {
            InvoiceDistribution invoiceDistribution = new InvoiceDistribution();
            invoiceDistribution.setInvoice(invoice);
            invoiceDistribution.setApartment(apartment);
            invoiceDistribution.setIsPaid(false);
            invoiceDistribution.setAmount(getApartmentDistribution(invoice, apartment));
            invoiceDistributions.add(invoiceDistributionRepository.save(invoiceDistribution));
        }
        return invoiceDistributions;
    }

    private Double getApartmentDistribution(Invoice invoice, Apartment apartment) {
        return switch (invoice.getMethod()) {
            case PER_PERSON -> getAmountPerPerson(invoice);
            case PER_COUNTER -> getAmountPerCounter(invoice);
            case PER_APARTMENT -> getAmountPerApartment(invoice, apartment);
        };
    }

    private Double getAmountPerPerson(Invoice invoice) {
        Integer totalNumberOfPersons = invoice.getAssociation().getApartments().stream()
                .map(Apartment::getNumberOfPersons)
                .mapToInt(np -> np)
                .sum();
        return invoice.getAmount() / totalNumberOfPersons;
    }

    private Double getAmountPerCounter(Invoice invoice) {
        return 0.0;
    }

    private Double getAmountPerApartment(Invoice invoice, Apartment apartment) {
        Double totalSurface = getTotalSurface(invoice.getAssociation().getApartments());
        return apartment.getSurface() * invoice.getAmount() / totalSurface;
    }

    private Double getTotalSurface(List<Apartment> apartments) {
        return apartments.stream()
                .map(Apartment::getSurface)
                .mapToDouble(s -> s)
                .sum();
    }
}
