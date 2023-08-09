package com.oam.controller;

import com.google.gson.JsonSyntaxException;
import com.oam.model.PaymentStatus;
import com.oam.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/stripe/webhook")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> webhook(@RequestHeader("Stripe-Signature") String signatureHeader,
                                     @RequestBody String payload) {
        Event event;
        try {
            String endpointSecret = System.getenv("STRIPE_ENDPOINT_SECRET");
            event = Webhook.constructEvent(payload, signatureHeader, endpointSecret);
        } catch (JsonSyntaxException | SignatureVerificationException e) {
            return ResponseEntity.badRequest().build();
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (dataObjectDeserializer.getObject().isPresent()) {
            StripeObject stripeObject = dataObjectDeserializer.getObject().get();
            if ("payment_intent.succeeded".equals(event.getType())) {
                if (stripeObject instanceof PaymentIntent paymentIntent) {
                    UUID paymentId = UUID.fromString(paymentIntent.getMetadata().get("paymentId"));
                    paymentService.updatePaymentStatus(paymentId, PaymentStatus.SUCCEEDED);
                }
            } else if ("payment_intent.payment_failed".equals(event.getType())) {
                if (stripeObject instanceof PaymentIntent paymentIntent) {
                    UUID paymentId = UUID.fromString(paymentIntent.getMetadata().get("paymentId"));
                    paymentService.updatePaymentStatus(paymentId, PaymentStatus.FAILED);
                }
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }

}
