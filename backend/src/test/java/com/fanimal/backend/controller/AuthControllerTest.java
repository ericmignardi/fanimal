package com.fanimal.backend.controller;

import com.fanimal.backend.dto.user.LoginRequest;
import com.fanimal.backend.dto.user.RegisterRequest;
import com.fanimal.backend.model.Role;
import com.fanimal.backend.repository.RoleRepository;
import com.fanimal.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("AuthController Integration Tests (H2)")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private RegisterRequest invalidRegisterRequest;
    private RegisterRequest invalidEmailRegisterRequest;
    private RegisterRequest invalidUsernameRegisterRequest;
    private LoginRequest invalidLoginRequest;

    // --------------------- Setup ---------------------
    @BeforeEach
    void setUpRoles() {
        for (Role.RoleName roleName : Role.RoleName.values()) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(Role.builder().name(roleName).build()));
        }
    }

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName("Admin");
        registerRequest.setEmail("admin@fanimal.com");
        registerRequest.setUsername("admin");
        registerRequest.setPassword("admin");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin");

        invalidRegisterRequest = new RegisterRequest();
        invalidRegisterRequest.setName(null);
        invalidRegisterRequest.setEmail(null);
        invalidRegisterRequest.setUsername(null);
        invalidRegisterRequest.setPassword(null);

        invalidEmailRegisterRequest = new RegisterRequest();
        invalidEmailRegisterRequest.setName("Admin");
        invalidEmailRegisterRequest.setEmail("admin@fanimal.com");
        invalidEmailRegisterRequest.setUsername("username");
        invalidEmailRegisterRequest.setPassword("password");

        invalidUsernameRegisterRequest = new RegisterRequest();
        invalidUsernameRegisterRequest.setName("Admin");
        invalidUsernameRegisterRequest.setEmail("test@test.com");
        invalidUsernameRegisterRequest.setUsername("admin");
        invalidUsernameRegisterRequest.setPassword("password");

        invalidLoginRequest = new LoginRequest();
        invalidLoginRequest.setUsername("admin");
        invalidLoginRequest.setPassword("wrongpassword");
    }

    // --------------------- POSITIVE TESTS ---------------------
    @Test
    void registerShouldCreateUserAndReturnToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andReturn();

        assertEquals(201, result.getResponse().getStatus());
        String response = result.getResponse().getContentAsString();

        assertTrue(response.contains("token"));
        assertTrue(response.contains("user"));
        assertTrue(userRepository.findByUsername(registerRequest.getUsername()).isPresent());
    }

    @Test
    void loginShouldReturnTokenForValidUser() throws Exception {
        // Register first
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andReturn();

        // Then login
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
        String response = result.getResponse().getContentAsString();
        assertTrue(response.contains("token"));
        assertTrue(response.contains("user"));
    }

    @Test
    void verifyShouldReturnUserWhenAuthenticated() throws Exception {
        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andReturn();

        String token = extractToken(registerResult.getResponse().getContentAsString());
        assertNotNull(token);

        MvcResult verifyResult = mockMvc.perform(get("/api/auth/verify")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, verifyResult.getResponse().getStatus());
        String content = verifyResult.getResponse().getContentAsString();

        assertAll(
                () -> assertTrue(content.contains("id")),
                () -> assertTrue(content.contains("name")),
                () -> assertTrue(content.contains("email")),
                () -> assertTrue(content.contains("username")),
                () -> assertTrue(content.contains("roles"))
        );
    }

    @Test
    void logoutShouldReturn200() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());
    }

    // --------------------- NEGATIVE TESTS ---------------------
    @Test
    void registerShouldReturn400WhenInvalidInput() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRegisterRequest)))
                .andReturn();

        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    void registerShouldReturn409WhenEmailInUse() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andReturn();

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmailRegisterRequest)))
                .andReturn();

        assertEquals(409, result.getResponse().getStatus());
    }

    @Test
    void registerShouldReturn409WhenUsernameInUse() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andReturn();

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUsernameRegisterRequest)))
                .andReturn();

        assertEquals(409, result.getResponse().getStatus());
    }

    @Test
    void loginShouldReturn401WhenInvalidPassword() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andReturn();

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLoginRequest)))
                .andReturn();

        assertEquals(401, result.getResponse().getStatus());
    }

    @Test
    void loginShouldReturn404WhenUserNotFound() throws Exception {
        LoginRequest nonExistentUser = new LoginRequest();
        nonExistentUser.setUsername("nonexistent");
        nonExistentUser.setPassword("password");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentUser)))
                .andReturn();

        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    void verifyShouldReturn401WhenNotAuthenticated() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/auth/verify")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(401, result.getResponse().getStatus());
    }

    // --------------------- UTIL ---------------------
    private String extractToken(String json) {
        try {
            return objectMapper.readTree(json).path("token").asText(null);
        } catch (Exception e) {
            return null;
        }
    }
}
