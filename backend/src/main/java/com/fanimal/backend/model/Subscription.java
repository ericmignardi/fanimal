package com.fanimal.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subscriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;
    @Column(name = "stripe_subscription_id")
    private String stripeSubscriptionId;

    @Enumerated(EnumType.STRING)
    private Tier tier;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @Getter
    public enum SubscriptionStatus {
        ACTIVE,
        INCOMPLETE,
        INCOMPLETE_EXPIRED,
        TRIALING,
        PAST_DUE,
        CANCELED,
        UNPAID
    }

    @Getter
    public enum Tier {
        BASIC(new BigDecimal("9.99")),
        STANDARD(new BigDecimal("14.99")),
        PREMIUM(new BigDecimal("19.99"));

        private final BigDecimal price;

        Tier(BigDecimal price) {
            this.price = price;
        }
    }
}


