package com.fanimal.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shelters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String address;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    // Stripe Price IDs for different tiers
    @Column(name = "stripe_basic_price_id")
    private String stripeBasicPriceId;
    @Column(name = "stripe_standard_price_id")
    private String stripeStandardPriceId;
    @Column(name = "stripe_premium_price_id")
    private String stripePremiumPriceId;
}
