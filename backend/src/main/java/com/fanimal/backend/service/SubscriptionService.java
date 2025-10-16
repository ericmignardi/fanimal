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
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCancelParams;
import com.stripe.param.SubscriptionCreateParams;
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
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Shelter shelter = shelterRepository.findByName(subscriptionRequest.getShelterRequest().getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelter not found"));
        Customer customer = getOrCreateCustomer(userDetails);
        // Create subscription in Stripe
//        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
//                .setCustomer(customer.getId())
//                .addItem(SubscriptionCreateParams.Item.builder()
//                        .setPrice(shelter.getStripePriceId())
//                        .build())
//                .build();
        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(customer.getId())
                .addItem(SubscriptionCreateParams.Item.builder()
                        .setPrice(String.valueOf(subscriptionRequest.getTier().getPrice()))
                        .setPlan(String.valueOf(subscriptionRequest.getTier()))
                        .build())
                .build();
        Subscription stripeSubscription = Subscription.create(params);
        // Save subscription in database
        com.fanimal.backend.model.Subscription subscription = com.fanimal.backend.model.Subscription.builder()
                .user(user)
                .shelter(shelter)
                .tier(subscriptionRequest.getTier())
                .amount(subscriptionRequest.getTier().getPrice())
                .startDate(LocalDate.now())
                .stripeSubscriptionId(stripeSubscription.getId())
                .build();
        subscriptionRepository.save(subscription);
        return SubscriptionResponse.fromEntity(subscription);
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
