package com.fanimal.backend.controller;

import com.fanimal.backend.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

//| Method | Endpoint                     | Description                |
//| ------ | ---------------------------- | -------------------------- |
//| POST   | /api/shelters/{id}/subscribe | User subscribes to shelter |
//| GET    | /api/users/me/subscriptions  | List user's subscriptions  |
//| DELETE | /api/subscriptions/{id}      | Cancel subscription        |
}
