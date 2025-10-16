package com.fanimal.backend.service;

import com.fanimal.backend.dto.user.UserResponse;
import com.fanimal.backend.dto.user.UserUpdateRequest;
import com.fanimal.backend.model.Role;
import com.fanimal.backend.model.User;
import com.fanimal.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @BeforeAll
    static void beforeAll() {}

    // --------------------- POSITIVE TESTS ---------------------

    @Test
    void getCurrentUser() {
        // GIVEN
        Role role = Role.builder().name(Role.RoleName.USER).build();
        User user = User.builder()
                .id(1L)
                .name("Current User")
                .email("current@mail.com")
                .username("username")
                .password("encodedPassword")
                .roles(Set.of(role))
                .build();
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        // WHEN
        when(userDetails.getUsername()).thenReturn(user.getUsername());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        // ACT
        UserResponse userResponse = userService.getCurrentUser(userDetails);
        // THEN
        assertNotNull(userResponse);
        assertEquals(user.getName(), userResponse.getName());
        assertEquals(user.getEmail(), userResponse.getEmail());
        assertEquals(user.getUsername(), userResponse.getUsername());
        assertEquals(user.getRoles(), userResponse.getRoles());
        assertNotSame(user, userResponse);
        // VERIFY
        verify(userRepository, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void updateCurrentUser() {
        // GIVEN
        Role role = Role.builder().name(Role.RoleName.USER).build();
        User user = User.builder()
                .id(1L)
                .name("Current User")
                .email("current@mail.com")
                .username("username")
                .password("encodedPassword")
                .roles(Set.of(role))
                .build();
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setName("Updated User");
        // WHEN
        when(userDetails.getUsername()).thenReturn(user.getUsername());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        // ACT
        UserResponse userResponse = userService.updateCurrentUser(userUpdateRequest, userDetails);
        // THEN
        assertNotNull(userResponse);
        assertEquals(userUpdateRequest.getName(), userResponse.getName());
        assertEquals(user.getEmail(), userResponse.getEmail());
        assertEquals(user.getUsername(), userResponse.getUsername());
        assertNotSame(user, userResponse);
        // VERIFY
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void findById() {
        // GIVEN
        Role role = Role.builder().name(Role.RoleName.USER).build();
        User user = User.builder()
                .id(1L)
                .name("Some User")
                .email("some@mail.com")
                .username("username")
                .password("encodedPassword")
                .roles(Set.of(role))
                .build();
        // WHEN
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // ACT
        UserResponse response = userService.findById(1L);
        // THEN
        assertNotNull(response);
        assertEquals(user.getName(), response.getName());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(user.getRoles(), response.getRoles());
        // VERIFY
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void delete() {
        // GIVEN
        Role role = Role.builder().name(Role.RoleName.USER).build();
        User user = User.builder()
                .id(1L)
                .name("Delete User")
                .email("delete@mail.com")
                .username("username")
                .password("encodedPassword")
                .roles(Set.of(role))
                .build();
        // WHEN
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // ACT & THEN
        assertDoesNotThrow(() -> userService.delete(1L));
        // VERIFY
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }

    // --------------------- NEGATIVE TESTS ---------------------

    @Test
    void getCurrentUserShouldReturn404WhenUserNotFound() {
        // GIVEN
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("nonexistent");
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        // ACT & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.getCurrentUser(userDetails));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
        // VERIFY
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    void updateCurrentUserShouldReturn404WhenUserNotFound() {
        // GIVEN
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setName("Updated User");
        when(userDetails.getUsername()).thenReturn("nonexistent");
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        // ACT & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.updateCurrentUser(userUpdateRequest, userDetails));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
        // VERIFY
        verify(userRepository, times(1)).findByUsername("nonexistent");
        verify(userRepository, never()).save(any());
    }

    @Test
    void findByIdShouldReturn404WhenUserNotFound() {
        // WHEN
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        // ACT & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.findById(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }

    @Test
    void deleteShouldReturn404WhenUserNotFound() {
        // WHEN
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        // ACT & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.delete(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }
}
