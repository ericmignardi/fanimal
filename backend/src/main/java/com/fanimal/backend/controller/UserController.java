package com.fanimal.backend.controller;

import com.fanimal.backend.dto.UserResponse;
import com.fanimal.backend.dto.UserUpdateRequest;
import com.fanimal.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SHELTER')")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse userResponse = userService.getCurrentUser(userDetails);
        return ResponseEntity.ok(userResponse);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'SHELTER')")
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest, @AuthenticationPrincipal UserDetails userDetails) {
        UserResponse userResponse = userService.updateCurrentUser(userUpdateRequest, userDetails);
        return ResponseEntity.ok(userResponse);
    }

//| Method | Endpoint      | Description              |
//| ------ | ------------- | ------------------------ |
//| GET    | /api/users/me | Get current user info    |
//| PUT    | /api/users/me | Update current user info |
}
