package com.fanimal.backend.controller;

import com.fanimal.backend.model.Subscription;
import com.fanimal.backend.repository.SubscriptionRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
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
        String stripeSubscriptionId = invoice.getSubscription();
        if (stripeSubscriptionId == null) return;
        subscriptionRepository.findByStripeSubscriptionId(stripeSubscriptionId)
                .ifPresent(sub -> {
                    try {
                        com.stripe.model.Subscription stripeSub = com.stripe.model.Subscription.retrieve(stripeSubscriptionId);
                        sub.setStatus(Subscription.SubscriptionStatus.valueOf(stripeSub.getStatus().toUpperCase()));
                        sub.setStartDate(Instant.ofEpochSecond(stripeSub.getCurrentPeriodStart()).atZone(ZoneId.systemDefault()).toLocalDate());
                        sub.setEndDate(Instant.ofEpochSecond(stripeSub.getCurrentPeriodEnd()).atZone(ZoneId.systemDefault()).toLocalDate());
                        subscriptionRepository.save(sub);
                    } catch (StripeException e) {
                        // Handle exception
                    }
                });
    }

    private void handleInvoicePaymentFailed(Event event) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer()
                .getObject().orElse(null);
        if (invoice == null) return;
        String stripeSubscriptionId = invoice.getSubscription();
        if (stripeSubscriptionId == null) return;
        subscriptionRepository.findByStripeSubscriptionId(stripeSubscriptionId)
                .ifPresent(sub -> {
                    sub.setStatus(Subscription.SubscriptionStatus.PAST_DUE);
                    subscriptionRepository.save(sub);
                });
    }

    private void handleSubscriptionDeleted(Event event) {
        com.stripe.model.Subscription stripeSub = (com.stripe.model.Subscription) event.getDataObjectDeserializer()
                .getObject().orElse(null);
        if (stripeSub == null) return;
        subscriptionRepository.findByStripeSubscriptionId(stripeSub.getId())
                .ifPresent(sub -> {
                    sub.setStatus(Subscription.SubscriptionStatus.CANCELED);
                    subscriptionRepository.save(sub);
                });
    }

    private void handleSubscriptionUpdated(Event event) {
        com.stripe.model.Subscription stripeSub = (com.stripe.model.Subscription) event.getDataObjectDeserializer()
                .getObject().orElse(null);
        if (stripeSub == null) return;
        subscriptionRepository.findByStripeSubscriptionId(stripeSub.getId())
                .ifPresent(sub -> {
                    sub.setStatus(Subscription.SubscriptionStatus.valueOf(stripeSub.getStatus().toUpperCase()));
                    sub.setStartDate(Instant.ofEpochSecond(stripeSub.getCurrentPeriodStart()).atZone(ZoneId.systemDefault()).toLocalDate());
                    sub.setEndDate(Instant.ofEpochSecond(stripeSub.getCurrentPeriodEnd()).atZone(ZoneId.systemDefault()).toLocalDate());
                    // This part is a bit tricky, as we need to map the Stripe price ID back to our Tier enum.
                    // This assumes that the shelter's price IDs are up-to-date.
                    String priceId = stripeSub.getItems().getData().get(0).getPrice().getId();
                    if (priceId.equals(sub.getShelter().getStripeBasicPriceId())) {
                        sub.setTier(Subscription.Tier.BASIC);
                    } else if (priceId.equals(sub.getShelter().getStripeStandardPriceId())) {
                        sub.setTier(Subscription.Tier.STANDARD);
                    } else if (priceId.equals(sub.getShelter().getStripePremiumPriceId())) {
                        sub.setTier(Subscription.Tier.PREMIUM);
                    }
                    subscriptionRepository.save(sub);
                });
    }
}
