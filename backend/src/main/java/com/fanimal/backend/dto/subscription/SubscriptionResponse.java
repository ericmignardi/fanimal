package com.fanimal.backend.dto.subscription;

import com.fanimal.backend.model.Shelter;
import com.fanimal.backend.model.Subscription;
import com.fanimal.backend.model.Subscription.Tier;
import com.fanimal.backend.model.User;
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
    private User user;
    private Shelter shelter;
    private double amount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Tier tier;
    private String clientSecret;

    public static SubscriptionResponse fromEntity(Subscription subscription) {
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .user(subscription.getUser())
                .shelter(subscription.getShelter())
                .amount(subscription.getAmount())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .tier(subscription.getTier())
                .build();
    }
}