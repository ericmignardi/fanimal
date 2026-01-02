package com.fanimal.backend.dto.subscription;

import com.fanimal.backend.dto.shelter.ShelterResponse;
import com.fanimal.backend.dto.user.UserResponse;
import com.fanimal.backend.model.Subscription.SubscriptionStatus;
import com.fanimal.backend.model.Subscription.Tier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionResponse {

    private Long id;
    private UserResponse user;
    private ShelterResponse shelter;
    private LocalDate startDate;
    private LocalDate endDate;
    private Tier tier;
    private SubscriptionStatus status;
}