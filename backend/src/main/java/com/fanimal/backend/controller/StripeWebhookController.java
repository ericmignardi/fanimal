package com.fanimal.backend.controller;

import com.fanimal.backend.model.Subscription;
import com.fanimal.backend.repository.SubscriptionRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final SubscriptionRepository subscriptionRepository;
    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeEvent(@RequestHeader("Stripe-Signature") String sigHeader,
                                                    @RequestBody String payload) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook signature verification failed");
        }
        switch (event.getType()) {
            case "invoice.paid":
                handleInvoicePaid(event);
                break;
            case "invoice.payment_failed":
                handleInvoicePaymentFailed(event);
                break;
            case "customer.subscription.deleted":
                handleSubscriptionDeleted(event);
                break;
            case "customer.subscription.updated":
                handleSubscriptionUpdated(event);
                break;
            default:
                // Ignore other events
                break;
        }
        return ResponseEntity.ok("Received");
    }

    private void handleInvoicePaid(Event event) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer()
                .getObject().orElse(null);
        if (invoice == null) return;
        com.stripe.model.Subscription stripeSub = invoice.getSubscriptionObject();
        if (stripeSub == null) return;
        Optional<Subscription> subOpt = subscriptionRepository.findByStripeSubscriptionId(stripeSub.getId());
        subOpt.ifPresent(sub -> {
            // Mark subscription as active or update fields if needed
            sub.setEndDate(sub.getEndDate().plusMonths(1)); // example: extend subscription
            subscriptionRepository.save(sub);
        });
    }

    private void handleInvoicePaymentFailed(Event event) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer()
                .getObject().orElse(null);
        if (invoice == null) return;
        com.stripe.model.Subscription stripeSub = invoice.getSubscriptionObject();
        if (stripeSub == null) return;
        subscriptionRepository.findByStripeSubscriptionId(stripeSub.getId())
                .ifPresent(sub -> {
                    // Optionally mark subscription as past due or inactive
                });
    }

    private void handleSubscriptionDeleted(Event event) {
        com.stripe.model.Subscription stripeSub = (com.stripe.model.Subscription) event.getDataObjectDeserializer()
                .getObject().orElse(null);
        if (stripeSub == null) return;
        subscriptionRepository.findByStripeSubscriptionId(stripeSub.getId())
                .ifPresent(sub -> {
                    subscriptionRepository.delete(sub);
                });
    }

    private void handleSubscriptionUpdated(Event event) {
        com.stripe.model.Subscription stripeSub = (com.stripe.model.Subscription) event.getDataObjectDeserializer()
                .getObject().orElse(null);
        if (stripeSub == null) return;
        subscriptionRepository.findByStripeSubscriptionId(stripeSub.getId())
                .ifPresent(sub -> {
                    // Update status, tier, or endDate if needed
                });
    }
}
