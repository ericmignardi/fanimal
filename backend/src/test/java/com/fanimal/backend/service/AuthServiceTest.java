package com.fanimal.backend.service;

import com.fanimal.backend.dto.user.*;
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
    @Mock
    private Authentication authMock;
    @Mock
    private UserDetails userDetailsMock;

    private static RegisterRequest registerRequest;
    private static LoginRequest loginRequest;
    private static User savedUser;

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
        // User
        savedUser = User.builder()
                .id(1L)
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password("encodedPassword")
                .build();
    }

    @BeforeEach
    void setupMocks() {
        lenient().when(userDetailsMock.getUsername()).thenReturn(registerRequest.getUsername());
        lenient().when(authMock.getPrincipal()).thenReturn(userDetailsMock);
    }

    // --------------------- POSITIVE TESTS ---------------------

    @Test
    void register() {
        Role role = Role.builder().name(Role.RoleName.USER).build();

        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(roleRepository.findByName(Role.RoleName.USER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtils.generateToken(any(UserResponse.class))).thenReturn(TOKEN);

        JwtResponse jwtResponse = authService.register(registerRequest);

        assertNotNull(jwtResponse);
        assertEquals(TOKEN, jwtResponse.getToken());
        assertEquals(registerRequest.getName(), jwtResponse.getUser().getName());
        assertEquals(registerRequest.getEmail(), jwtResponse.getUser().getEmail());
        assertEquals(registerRequest.getUsername(), jwtResponse.getUser().getUsername());

        Mockito.verify(userRepository, times(1)).save(any(User.class));
        Mockito.verify(jwtUtils, times(1)).generateToken(any(UserResponse.class));
    }

    @Test
    void login() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authMock);
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(savedUser));
        when(jwtUtils.generateToken(any(UserResponse.class))).thenReturn(TOKEN);

        JwtResponse jwtResponse = authService.login(loginRequest);

        assertNotNull(jwtResponse);
        assertEquals(TOKEN, jwtResponse.getToken());
        assertEquals(registerRequest.getName(), jwtResponse.getUser().getName());
        assertEquals(registerRequest.getEmail(), jwtResponse.getUser().getEmail());
        assertEquals(registerRequest.getUsername(), jwtResponse.getUser().getUsername());

        Mockito.verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(userRepository, times(1)).findByUsername(registerRequest.getUsername());
        Mockito.verify(jwtUtils, times(1)).generateToken(any(UserResponse.class));
    }

    @Test
    void verify() {
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.of(savedUser));

        UserResponse userResponse = authService.verify(registerRequest.getUsername());

        assertNotNull(userResponse);
        assertEquals(registerRequest.getName(), userResponse.getName());
        assertEquals(registerRequest.getEmail(), userResponse.getEmail());
        assertEquals(registerRequest.getUsername(), userResponse.getUsername());

        Mockito.verify(userRepository, times(1)).findByUsername(registerRequest.getUsername());
    }

    // --------------------- NEGATIVE TESTS ---------------------

    @Test
    void registerShouldReturn409WhenEmailAlreadyInUse() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authService.register(registerRequest));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Mockito.verify(userRepository, never()).save(any());
        Mockito.verify(jwtUtils, never()).generateToken(any());
    }

    @Test
    void registerShouldReturn409WhenUsernameAlreadyInUse() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authService.register(registerRequest));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Mockito.verify(userRepository, never()).save(any());
        Mockito.verify(jwtUtils, never()).generateToken(any());
    }

    @Test
    void loginShouldReturn404WhenUserNotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authMock);
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authService.login(loginRequest));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Mockito.verify(jwtUtils, never()).generateToken(any());
    }

    @Test
    void verifyShouldReturn404WhenUserNotFound() {
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authService.verify(registerRequest.getUsername()));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
