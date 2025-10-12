package com.fanimal.backend.service;

import com.fanimal.backend.dto.UserResponse;
import com.fanimal.backend.dto.UserUpdateRequest;
import com.fanimal.backend.model.User;
import com.fanimal.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getCurrentUser(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new InvalidParameterException("User not found"));
        return UserResponse.fromEntity(user);
    }

    public UserResponse updateCurrentUser(UserUpdateRequest userUpdateRequest, UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new InvalidParameterException("User not found"));
        if (userUpdateRequest.getName() != null) user.setName(userUpdateRequest.getName());
        userRepository.save(user);
        return UserResponse.fromEntity(user);
    }

}
