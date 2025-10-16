package com.fanimal.backend.controller;

import com.fanimal.backend.dto.subscription.SubscriptionRequest;
import com.fanimal.backend.dto.subscription.SubscriptionResponse;
import com.fanimal.backend.service.SubscriptionService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> subscribe(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody SubscriptionRequest subscriptionRequest) throws StripeException {
        SubscriptionResponse subscriptionResponse = subscriptionService.subscribe(userDetails, subscriptionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionResponse);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> findAllByUser(@AuthenticationPrincipal UserDetails userDetails) {
        List<SubscriptionResponse>  subscriptionResponseList = subscriptionService.findAllByUser(userDetails);
        return ResponseEntity.ok().body(subscriptionResponseList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) throws StripeException {
        subscriptionService.unsubscribe(id, userDetails);
        return ResponseEntity.noContent().build();
    }
}
