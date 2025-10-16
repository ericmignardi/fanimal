package com.fanimal.backend.service;

import com.fanimal.backend.dto.user.JwtResponse;
import com.fanimal.backend.dto.user.LoginRequest;
import com.fanimal.backend.dto.user.RegisterRequest;
import com.fanimal.backend.dto.user.UserResponse;
import com.fanimal.backend.model.Role;
import com.fanimal.backend.model.User;
import com.fanimal.backend.repository.RoleRepository;
import com.fanimal.backend.repository.UserRepository;
import com.fanimal.backend.security.JwtUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private AuthenticationManager authenticationManager;
    private static RegisterRequest registerRequest;
    private static LoginRequest loginRequest;
    public static final String TOKEN = "token";

    @BeforeAll
    static void beforeAll() {
        // RegisterRequest
        registerRequest = new RegisterRequest();
        registerRequest.setName("Test User");
        registerRequest.setEmail("test@example.com");
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        // LoginRequest
        loginRequest = new LoginRequest();
        loginRequest.setUsername(registerRequest.getUsername());
        loginRequest.setPassword(registerRequest.getPassword());
    }

    // --------------------- POSITIVE TESTS ---------------------

    @Test
    void register() {
        // GIVEN
        Role role = Role.builder().name(Role.RoleName.USER).build();
        User savedUser = User.builder()
                .id(1L)
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password("encodedPassword")
                .build();
        // WHEN
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(roleRepository.findByName(Role.RoleName.USER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtils.generateToken(any(UserResponse.class))).thenReturn(TOKEN);
        // ACTION
        JwtResponse jwtResponse = authService.register(registerRequest);
        // THEN
        assertNotNull(jwtResponse);
        assertEquals(TOKEN, jwtResponse.getToken());
        assertEquals(registerRequest.getName(), jwtResponse.getUser().getName());
        assertEquals(registerRequest.getEmail(), jwtResponse.getUser().getEmail());
        assertEquals(registerRequest.getUsername(), jwtResponse.getUser().getUsername());
        // VERIFY
        Mockito.verify(userRepository, times(1)).save(any(User.class));
        Mockito.verify(jwtUtils, times(1)).generateToken(any(UserResponse.class));
    }

    @Test
    void login() {
        // GIVEN
        User savedUser = User.builder()
                .id(1L)
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password("encodedPassword")
                .build();
        Authentication mockAuth = Mockito.mock(Authentication.class);
        UserDetails mockUserDetails = Mockito.mock(UserDetails.class);
        // WHEN
        when(mockUserDetails.getUsername()).thenReturn(registerRequest.getUsername());
        when(mockAuth.getPrincipal()).thenReturn(mockUserDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(savedUser));
        when(jwtUtils.generateToken(any(UserResponse.class))).thenReturn(TOKEN);
        // ACT
        JwtResponse jwtResponse = authService.login(loginRequest);
        // THEN
        assertNotNull(jwtResponse);
        assertEquals(TOKEN, jwtResponse.getToken());
        assertEquals(registerRequest.getName(), jwtResponse.getUser().getName());
        assertEquals(registerRequest.getEmail(), jwtResponse.getUser().getEmail());
        assertEquals(registerRequest.getUsername(), jwtResponse.getUser().getUsername());
        // VERIFY
        Mockito.verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(userRepository, times(1)).findByUsername(registerRequest.getUsername());
        Mockito.verify(jwtUtils, times(1)).generateToken(any(UserResponse.class));
    }

    @Test
    void verify() {
        // GIVEN
        User savedUser = User.builder()
                .id(1L)
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password("encodedPassword")
                .build();
        // WHEN
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(savedUser));
        // ACT
        UserResponse userResponse = authService.verify(registerRequest.getUsername());
        // THEN
        assertNotNull(userResponse);
        assertEquals(registerRequest.getName(), userResponse.getName());
        assertEquals(registerRequest.getEmail(), userResponse.getEmail());
        assertEquals(registerRequest.getUsername(), userResponse.getUsername());
        // VERIFY
        Mockito.verify(userRepository, times(1)).findByUsername(registerRequest.getUsername());
    }

    // --------------------- NEGATIVE TESTS ---------------------

    @Test
    void registerShouldReturn409WhenEmailAlreadyInUse() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authService.register(registerRequest));
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Mockito.verify(userRepository, never()).save(any());
        Mockito.verify(jwtUtils, never()).generateToken(any());
    }

    @Test
    void registerShouldReturn409WhenUsernameAlreadyInUse() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(true);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authService.register(registerRequest));
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Mockito.verify(userRepository, never()).save(any());
        Mockito.verify(jwtUtils, never()).generateToken(any());
    }

    @Test
    void loginShouldReturn404WhenUserNotFound() {
        Authentication mockAuth = mock(Authentication.class);
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn(registerRequest.getUsername());
        when(mockAuth.getPrincipal()).thenReturn(mockUserDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authService.login(loginRequest));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Mockito.verify(jwtUtils, never()).generateToken(any());
    }

    @Test
    void verifyShouldReturn404WhenUserNotFound() {
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> authService.verify(registerRequest.getUsername()));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
