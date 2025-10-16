package com.fanimal.backend.service;

import com.fanimal.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @BeforeAll
    static void beforeAll() {

    }

    // --------------------- POSITIVE TESTS ---------------------

    @Test
    void getCurrentUser() {}

    @Test
    void updateCurrentUser() {}

    @Test
    void findById() {}

    @Test
    void delete() {}

    // --------------------- NEGATIVE TESTS ---------------------

    @Test
    void getCurrentUserShouldReturn404WhenUserNotFound() {}
    @Test
    void updateCurrentUserShouldReturn404WhenUserNotFound() {}
    @Test
    void findByIdShouldReturn404WhenUserNotFound() {}
    @Test
    void deleteShouldReturn404WhenUserNotFound() {}
}