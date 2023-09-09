package com.oam.service;

import com.oam.exception.ErrorMessage;
import com.oam.exception.model.BadRequestException;
import com.oam.exception.model.NotFoundException;
import com.oam.model.*;
import com.oam.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.oam.model.PaymentStatus.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final StripeClientService stripeClientService;
    private final InvoiceDistributionService invoiceDistributionService;
    private final SecurityService securityService;
    private final UserService userService;
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentIntent create(Set<UUID> invoiceDistributionIds) throws StripeException {
        if (invoiceDistributionIds.isEmpty()) {
            throw new BadRequestException(ErrorMessage.EMPTY_INVOICE_DISTRIBUTION_LIST);
        }
        Payment payment = createPaymentEntity();
        Double amountToPay = 0.0;
        for (UUID id : invoiceDistributionIds) {
            InvoiceDistribution invoiceDistribution = invoiceDistributionService.getById(id);
            if (invoiceDistribution.getPayment() != null && invoiceDistribution.getPayment().getStatus() == SUCCEEDED) {
                throw new BadRequestException(ErrorMessage.PAYMENT_ALREADY_DONE);
            }
            if (invoiceDistribution.getPayment() != null) {
                paymentRepository.delete(invoiceDistribution.getPayment());
            }
            invoiceDistribution.setPayment(payment);
            payment.getInvoiceDistributions().add(invoiceDistribution);
            amountToPay += invoiceDistribution.getAmount();
        }
        payment.setAmount(amountToPay);
        Payment persistedPayment = paymentRepository.save(payment);
        String iban = payment.getInvoiceDistributions().get(0).getApartment().getAssociation().getIban();
        return stripeClientService.createPaymentIntent(amountToPay, persistedPayment.getId(), iban);
    }

    public Payment getById(UUID id) {
        return paymentRepository.findById(id).orElseThrow(() ->
                new NotFoundException(ErrorMessage.NOT_FOUND, "payment", id));
    }

    public List<Payment> getAll() {
        User user = userService.getById(securityService.getUserId());
        return paymentRepository.findAllByUserId(user.getId());
    }

    @Transactional
    public void updatePaymentStatus(UUID paymentId, PaymentStatus status) {
        Payment payment = getById(paymentId);
        payment.setStatus(status);
        paymentRepository.save(payment);
    }

    private Payment createPaymentEntity() {
        User user = userService.getById(securityService.getUserId());
        Payment payment = new Payment();
        payment.setCreatedAt(LocalDateTime.now());
        payment.setStatus(PENDING);
        payment.setUser(user);
        return payment;
    }
}
