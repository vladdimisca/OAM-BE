package com.oam.service;

import com.google.auth.oauth2.ExternalAccountCredentials;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.ExternalAccountCollectionCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PayoutCreateParams;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class StripeClientService {

    public StripeClientService() {
        Stripe.apiKey = System.getenv("STRIPE_API_KEY");
    }

    public PaymentIntent createPaymentIntent(Double amount, UUID paymentId, String iban) throws StripeException {
        PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
                .setAmount((long) (amount * 100))
                .setCurrency("EUR")
                .addPaymentMethodType("card")
                .putMetadata("paymentId", paymentId.toString())
                .putMetadata("iban", iban)
                .build();
        return PaymentIntent.create(paymentIntentParams);
    }

    public Payout initiatePayout(String iban, Long amount) throws StripeException {
        Account account = createAccount(iban);
        PayoutCreateParams payoutCreateParams = PayoutCreateParams.builder()
                .setAmount(amount)
                .setCurrency("EUR")
                .setDestination(account.getExternalAccounts().getData().get(0).getId())
                .setSourceType(PayoutCreateParams.SourceType.BANK_ACCOUNT)
                .build();
        return Payout.create(payoutCreateParams);
    }

    private Account createAccount(String iban) throws StripeException {
        AccountCreateParams accountCreateParams = AccountCreateParams.builder()
                .setType(AccountCreateParams.Type.CUSTOM)
                .setCapabilities(AccountCreateParams.Capabilities.builder()
                        .setTransfers(AccountCreateParams.Capabilities.Transfers.builder()
                                .setRequested(true)
                                .build())
                        .setBankTransferPayments(AccountCreateParams.Capabilities.BankTransferPayments.builder()
                                .setRequested(true)
                                .build())
                        .build())
                .putExtraParam("external_account", Map.of(
                        "object", "bank_account",
                        "country", iban.substring(0, 2),
                        "currency", "EUR",
                        "account_number", iban))
                .build();
        return Account.create(accountCreateParams);
    }
}
