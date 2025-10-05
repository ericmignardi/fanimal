package com.fanimal.backend.service;

import com.fanimal.backend.dto.JwtResponse;
import com.fanimal.backend.dto.LoginRequest;
import com.fanimal.backend.dto.RegisterRequest;
import com.fanimal.backend.dto.UserResponse;
import com.fanimal.backend.model.Role;
import com.fanimal.backend.model.Role.RoleName;
import com.fanimal.backend.model.User;
import com.fanimal.backend.repository.RoleRepository;
import com.fanimal.backend.repository.UserRepository;
import com.fanimal.backend.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public JwtResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
        }
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use");
        }
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Default role not found"));
        user.getRoles().add(userRole);
        user = userRepository.save(user);
        UserResponse userResponse = new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getUsername(), user.getRoles());
        String token = jwtUtils.generateToken(user);
        return new JwtResponse(token, userResponse);
    }

    public JwtResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        UserResponse userResponse = new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getUsername(), user.getRoles());
        String token = jwtUtils.generateToken(user);
        return new JwtResponse(token, userResponse);
    }

//    public void logout() {}
}
