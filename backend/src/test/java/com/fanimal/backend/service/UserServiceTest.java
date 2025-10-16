package com.fanimal.backend.service;

import com.fanimal.backend.dto.user.UserResponse;
import com.fanimal.backend.dto.user.UserUpdateRequest;
import com.fanimal.backend.model.Role;
import com.fanimal.backend.model.User;
import com.fanimal.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
    @Mock
    private UserDetails userDetailsMock;

    private static Role userRole;
    private static User currentUser;
    private static User someUser;
    private static User deleteUser;

    @BeforeAll
    static void beforeAll() {
        // Role
        userRole = Role.builder().name(Role.RoleName.USER).build();
        // Current User
        currentUser = User.builder()
                .id(1L)
                .name("Current User")
                .email("current@mail.com")
                .username("username")
                .password("encodedPassword")
                .roles(Set.of(userRole))
                .build();
        // Some User
        someUser = User.builder()
                .id(1L)
                .name("Some User")
                .email("some@mail.com")
                .username("username")
                .password("encodedPassword")
                .roles(Set.of(userRole))
                .build();
        // Delete User
        deleteUser = User.builder()
                .id(1L)
                .name("Delete User")
                .email("delete@mail.com")
                .username("username")
                .password("encodedPassword")
                .roles(Set.of(userRole))
                .build();
    }

    @BeforeEach
    void setupMocks() {
        lenient().when(userDetailsMock.getUsername()).thenReturn(currentUser.getUsername());
    }

    // --------------------- POSITIVE TESTS ---------------------

    @Test
    void getCurrentUser() {
        when(userRepository.findByUsername(currentUser.getUsername())).thenReturn(Optional.of(currentUser));

        UserResponse response = userService.getCurrentUser(userDetailsMock);

        assertNotNull(response);
        assertEquals(currentUser.getName(), response.getName());
        assertEquals(currentUser.getEmail(), response.getEmail());
        assertEquals(currentUser.getUsername(), response.getUsername());
        assertEquals(currentUser.getRoles(), response.getRoles());

        verify(userRepository, times(1)).findByUsername(currentUser.getUsername());
    }

    @Test
    void updateCurrentUser() {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("Updated User");

        when(userRepository.findByUsername(currentUser.getUsername())).thenReturn(Optional.of(currentUser));

        UserResponse response = userService.updateCurrentUser(updateRequest, userDetailsMock);

        assertNotNull(response);
        assertEquals(updateRequest.getName(), response.getName());
        assertEquals(currentUser.getEmail(), response.getEmail());
        assertEquals(currentUser.getUsername(), response.getUsername());

        verify(userRepository, times(1)).findByUsername(currentUser.getUsername());
        verify(userRepository, times(1)).save(currentUser);
    }

    @Test
    void findById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(someUser));

        UserResponse response = userService.findById(1L);

        assertNotNull(response);
        assertEquals(someUser.getName(), response.getName());
        assertEquals(someUser.getEmail(), response.getEmail());
        assertEquals(someUser.getUsername(), response.getUsername());
        assertEquals(someUser.getRoles(), response.getRoles());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void delete() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(deleteUser));

        assertDoesNotThrow(() -> userService.delete(1L));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(deleteUser);
    }

    // --------------------- NEGATIVE TESTS ---------------------

    @Test
    void getCurrentUserShouldReturn404WhenUserNotFound() {
        when(userDetailsMock.getUsername()).thenReturn("nonexistent");
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.getCurrentUser(userDetailsMock));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());

        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    void updateCurrentUserShouldReturn404WhenUserNotFound() {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("Updated User");

        when(userDetailsMock.getUsername()).thenReturn("nonexistent");
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.updateCurrentUser(updateRequest, userDetailsMock));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());

        verify(userRepository, times(1)).findByUsername("nonexistent");
        verify(userRepository, never()).save(any());
    }

    @Test
    void findByIdShouldReturn404WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.findById(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }

    @Test
    void deleteShouldReturn404WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.delete(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
    }
}
