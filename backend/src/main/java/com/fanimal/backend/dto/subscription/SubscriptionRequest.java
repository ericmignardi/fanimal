package com.fanimal.backend.dto.subscription;

import com.fanimal.backend.model.Subscription;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequest {

    @NotNull
    private Long shelterId;
    @NotNull
    private Subscription.Tier tier;
    @NotNull
    private String paymentMethodId;
}
