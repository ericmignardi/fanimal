package com.fanimal.backend.service;

import com.fanimal.backend.dto.subscription.SubscriptionRequest;
import com.fanimal.backend.dto.subscription.SubscriptionResponse;
import com.fanimal.backend.model.Shelter;
import com.fanimal.backend.model.User;
import com.fanimal.backend.repository.ShelterRepository;
import com.fanimal.backend.repository.SubscriptionRepository;
import com.fanimal.backend.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Subscription;
import com.stripe.param.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final ShelterRepository shelterRepository;

    // Create or retrieve Stripe Customer
    public Customer getOrCreateCustomer(UserDetails userDetails) throws StripeException {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getStripeCustomerId() != null) {
            return Customer.retrieve(user.getStripeCustomerId());
        }
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(user.getEmail())
                .setName(user.getName())
                .build();
        Customer customer = Customer.create(params);
        user.setStripeCustomerId(customer.getId());
        userRepository.save(user);
        return customer;
    }

    public SubscriptionResponse subscribe(UserDetails userDetails, SubscriptionRequest subscriptionRequest) throws StripeException {
        // Fetch user and shelter
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Shelter shelter = shelterRepository.findByName(subscriptionRequest.getShelterRequest().getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelter not found"));
        // Get or create Stripe customer
        Customer customer = getOrCreateCustomer(userDetails);
        // Attach payment method to customer
        PaymentMethod pm = PaymentMethod.retrieve(subscriptionRequest.getPaymentMethodId());
        pm.attach(PaymentMethodAttachParams.builder()
                .setCustomer(customer.getId())
                .build());
        // Set default payment method for invoices
        customer.update(CustomerUpdateParams.builder()
                .setInvoiceSettings(CustomerUpdateParams.InvoiceSettings.builder()
                        .setDefaultPaymentMethod(subscriptionRequest.getPaymentMethodId())
                        .build())
                .build());
        // Determine Stripe price ID based on selected tier
        String priceId = switch (subscriptionRequest.getTier()) {
            case BASIC -> shelter.getStripeBasicPriceId();
            case STANDARD -> shelter.getStripeStandardPriceId();
            case PREMIUM -> shelter.getStripePremiumPriceId();
        };
        // Create Stripe subscription
        SubscriptionCreateParams subParams = SubscriptionCreateParams.builder()
                .setCustomer(customer.getId())
                .addItem(SubscriptionCreateParams.Item.builder()
                        .setPrice(priceId)
                        .build())
                .setPaymentBehavior(SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE)
                .setPaymentSettings(
                        SubscriptionCreateParams.PaymentSettings.builder()
                                .setSaveDefaultPaymentMethod(
                                        SubscriptionCreateParams.PaymentSettings.SaveDefaultPaymentMethod.ON_SUBSCRIPTION)
                                .build())
                .addExpand("latest_invoice.payment_intent") // important to get client secret
                .build();
        Subscription stripeSubscription = Subscription.create(subParams);
        // Extract client secret for frontend payment confirmation
        var paymentIntent = (com.stripe.model.PaymentIntent)
                stripeSubscription.getLatestInvoiceObject().getPaymentIntentObject();
        String clientSecret = paymentIntent != null ? paymentIntent.getClientSecret() : null;
        // Save subscription locally
        com.fanimal.backend.model.Subscription subscription = com.fanimal.backend.model.Subscription.builder()
                .user(user)
                .shelter(shelter)
                .tier(subscriptionRequest.getTier())
                .amount(subscriptionRequest.getTier().getPrice())
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .stripeSubscriptionId(stripeSubscription.getId())
                .build();
        subscriptionRepository.save(subscription);
        // Return subscription response
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .amount(subscription.getAmount())
                .startDate(subscription.getStartDate())
                .tier(subscription.getTier())
                .shelter(shelter)
                .user(user)
                .clientSecret(clientSecret)
                .build();
    }


    public List<SubscriptionResponse> findAllByUser(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return subscriptionRepository.findAllByUser(user).stream()
                .map(SubscriptionResponse::fromEntity)
                .toList();
    }

    public void unsubscribe(Long id, UserDetails userDetails) throws StripeException {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        com.fanimal.backend.model.Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscription not found"));
        if (!subscription.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized to cancel this subscription");
        }
        // Cancel subscription in Stripe
        Subscription stripeSubscription = Subscription.retrieve(subscription.getStripeSubscriptionId());
        stripeSubscription.cancel(SubscriptionCancelParams.builder().build());
        // Remove from local DB
        subscriptionRepository.delete(subscription);
    }
}
