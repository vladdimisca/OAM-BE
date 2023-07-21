package com.oam.service;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.NotFoundException;
import com.oam.model.Association;
import com.oam.model.Invoice;
import com.oam.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final FirebaseStorageService firebaseStorageService;
    private final AssociationService associationService;
    private final InvoiceDistributionService invoiceDistributionService;

    @Transactional
    public Invoice create(Invoice invoice, MultipartFile document) throws IOException {
        Association association = associationService.getById(invoice.getAssociation().getId());
        URL documentUrl = firebaseStorageService.uploadFile(createUniqueName(document.getName()), document, document.getContentType());
        invoice.setDocumentUrl(documentUrl.toString());
        invoice.setAssociation(association);
        Invoice persistedInvoice = invoiceRepository.save(invoice);
        invoiceDistributionService.distribute(invoice);
        return persistedInvoice;
    }

    private String createUniqueName(String documentName) {
        return new Timestamp(System.currentTimeMillis()).getTime() + "/" + documentName;
    }

    public Invoice getById(UUID id) {
        return invoiceRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ErrorMessage.NOT_FOUND, "invoice", id));
    }

    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }
}
