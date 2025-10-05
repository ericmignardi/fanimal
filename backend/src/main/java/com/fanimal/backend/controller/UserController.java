package com.fanimal.backend.controller;

import com.fanimal.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//| Method | Endpoint      | Description              |
//| ------ | ------------- | ------------------------ |
//| GET    | /api/users/me | Get current user info    |
//| PUT    | /api/users/me | Update current user info |
}
