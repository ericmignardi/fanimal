package com.fanimal.backend.controller;

import com.fanimal.backend.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/animals")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

//| Method | Endpoint                   | Description                        |
//| ------ | -------------------------- | ---------------------------------- |
//| GET    | /api/shelters/{id}/animals | List all animals in a shelter      |
//| GET    | /api/animals/{id}          | Get animal details                 |
//| POST   | /api/shelters/{id}/animals | Add animal to shelter (owner only) |
//| PUT    | /api/animals/{id}          | Update animal info                 |
//| DELETE | /api/animals/{id}          | Delete animal (owner only)         |
}
