package com.fanimal.backend.model;

import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;
    private double amount;
    private LocalDate startDate;
    private LocalDate endDate;
    @Column(name = "stripe_subscription_id")
    private String stripeSubscriptionId;
    @Enumerated(EnumType.STRING)
    private Tier tier;

    @Getter
    public enum Tier {
        BASIC(9.99),
        STANDARD(14.99),
        PREMIUM(19.99);

        private final double price;

        Tier(double price) {
            this.price = price;
        }
    }
}


