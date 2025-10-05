package com.fanimal.backend.controller;

import com.fanimal.backend.service.ShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shelters")
@RequiredArgsConstructor
public class ShelterController {

    private final ShelterService shelterService;

//| Method | Endpoint           | Description                         |
//| ------ | ------------------ | ----------------------------------- |
//| GET    | /api/shelters      | List all shelters                   |
//| GET    | /api/shelters/{id} | Get shelter by ID                   |
//| POST   | /api/shelters      | Create shelter (shelter/admin only) |
//| PUT    | /api/shelters/{id} | Update shelter (owner/admin only)   |
//| DELETE | /api/shelters/{id} | Delete shelter (owner/admin only)   |
}
