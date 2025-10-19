package com.fanimal.backend.repository;

import com.fanimal.backend.model.Subscription;
import com.fanimal.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllByUser(User user);

    Optional<Subscription> findByStripeSubscriptionId(String stripeSubscriptionId);
}
