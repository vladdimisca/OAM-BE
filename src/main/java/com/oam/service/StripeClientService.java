package com.oam.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StripeClientService {

    public StripeClientService() {
        Stripe.apiKey = System.getenv("STRIPE_API_KEY");
    }

    public PaymentIntent createPaymentIntent(Double amount, UUID paymentId) throws StripeException {
        PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
                .setAmount((long) (amount * 100))
                .setCurrency("EUR")
                .addPaymentMethodType("card")
                .putMetadata("paymentId", paymentId.toString())
                .build();
        return PaymentIntent.create(paymentIntentParams);
    }
}
