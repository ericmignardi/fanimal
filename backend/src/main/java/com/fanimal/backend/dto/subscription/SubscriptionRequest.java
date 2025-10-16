package com.fanimal.backend.dto.subscription;

import com.fanimal.backend.dto.shelter.ShelterRequest;
import com.fanimal.backend.model.Subscription;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubscriptionRequest {

    @NotNull
    private ShelterRequest shelterRequest;
    @NotNull
    private Subscription.Tier tier;
}
