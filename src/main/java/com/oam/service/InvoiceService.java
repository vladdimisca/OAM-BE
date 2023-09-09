package com.oam.service;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.BadRequestException;
import com.oam.exception.model.ForbiddenException;
import com.oam.exception.model.NotFoundException;
import com.oam.model.*;
import com.oam.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static com.oam.exception.ErrorMessage.CANNOT_DELETE_PAID_INVOICES;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final AssociationService associationService;
    private final InvoiceDistributionService invoiceDistributionService;
    private final SecurityService securityService;
    private final UserService userService;

    @Transactional
    public Invoice create(Invoice invoice, MultipartFile document) throws IOException {
        if (invoice.getMethod() == InvoiceMethod.PER_COUNTER && invoice.getPricePerIndexUnit() == null) {
            throw new BadRequestException(ErrorMessage.PRICE_PER_INDEX_UNIT_NOT_PROVIDED);
        }
        if (invoice.getMethod() != InvoiceMethod.PER_COUNTER) {
            invoice.setPricePerIndexUnit(null);
        }
        Association association = associationService.getById(invoice.getAssociation().getId());
        invoice.setAssociation(association);
        User user = userService.getById(securityService.getUserId());
        invoice.setUser(user);
        if (getAssociationMember(user, association, AssociationRole.ADMIN).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.MUST_BE_ADMIN_ASSOCIATION_MEMBER);
        }
        if (document == null) {
            List<String> documentUrls = invoiceRepository.findInvoiceUrlByNumber(invoice.getNumber());
            if (documentUrls.isEmpty()) {
                throw new BadRequestException(ErrorMessage.INVOICE_NUMBER_NOT_EXISTING);
            }
            String persistedUrl = invoiceRepository.findInvoiceUrlByNumber(invoice.getNumber()).get(0);
            invoice.setDocumentUrl(persistedUrl);
        } else {
            URL documentUrl = firebaseStorageService.uploadFile(createUniqueName(document.getName()), document, document.getContentType());
            invoice.setDocumentUrl(documentUrl.toString());
        }
        Invoice persistedInvoice = invoiceRepository.save(invoice);
        invoiceDistributionService.distribute(invoice);
        return persistedInvoice;
    }

    private String createUniqueName(String documentName) {
        return new Timestamp(System.currentTimeMillis()).getTime() + "/" + documentName;
    }

    public Invoice getById(UUID id) {
        User user = userService.getById(securityService.getUserId());
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ErrorMessage.NOT_FOUND, "invoice", id));
        if (getAssociationMember(user, invoice.getAssociation(), null).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN);
        }
        return invoice;
    }

    public List<Invoice> getAll() {
        User user = userService.getById(securityService.getUserId());
        return invoiceRepository.findAllByUserId(user.getId());
    }

    public void deleteById(UUID id) {
        User user = userService.getById(securityService.getUserId());
        Invoice invoice = getById(id);
        if (getAssociationMember(user, invoice.getAssociation(), AssociationRole.ADMIN).isEmpty()) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN);
        }
        checkInvoiceNotPaidByAnyAssociationMember(invoice);
        invoiceRepository.delete(invoice);
    }

    public Statistics getStatistics() {
        final String[] allMonths = {
                "Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"
        };

        List<Invoice> invoices = invoiceRepository.findAll();
        List<String> months = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        LocalDateTime currentDateTime = LocalDateTime.now();

        for (int month = 1; month <= currentDateTime.getMonthValue(); month++) {
            final int finalMonth = month; // variables used in lambda expression should be final or effectively final

            var newInvoices = invoices.stream()
                    .filter(invoice -> invoice.getYear() == currentDateTime.getYear() && invoice.getMonth() == finalMonth)
                    .toList();

            months.add(allMonths[finalMonth - 1]);
            values.add(newInvoices.size());
        }

        Statistics statistics = new Statistics();
        statistics.setLabels(months);
        statistics.setData(values);
        return statistics;
    }

    private Optional<AssociationMember> getAssociationMember(User user, Association association, AssociationRole role) {
        return user.getAssociationMembers().stream()
                .filter(associationMember -> associationMember.getAssociation().equals(association))
                .filter(associationMember -> role == null || associationMember.getRole().equals(role))
                .findFirst();
    }

    private void checkInvoiceNotPaidByAnyAssociationMember(Invoice invoice) {
        if (invoice.getInvoiceDistributions().stream().map(InvoiceDistribution::getPayment).anyMatch(Objects::nonNull)) {
            throw new BadRequestException(CANNOT_DELETE_PAID_INVOICES);
        }
    }
}
