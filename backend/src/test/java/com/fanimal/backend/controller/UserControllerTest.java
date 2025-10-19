package com.fanimal.backend.controller;

import com.fanimal.backend.dto.user.LoginRequest;
import com.fanimal.backend.dto.user.RegisterRequest;
import com.fanimal.backend.dto.user.UserUpdateRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("UserController Integration Tests with H2")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;
    private String adminToken;

    // --------------------- Setup ---------------------
    @BeforeEach
    void setUp() throws Exception {
        // ---------- Register & Login a regular USER ----------
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Regular User");
        registerRequest.setEmail("user@test.com");
        registerRequest.setUsername("user");
        registerRequest.setPassword("password");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("password");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        userToken = extractToken(loginResult.getResponse().getContentAsString());
        assertNotNull(userToken);

        // ---------- Register & Login an ADMIN ----------
        RegisterRequest adminRegister = new RegisterRequest();
        adminRegister.setName("Admin User");
        adminRegister.setEmail("admin@test.com");
        adminRegister.setUsername("admin");
        adminRegister.setPassword("adminpass");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminRegister)))
                .andExpect(status().isCreated());

        LoginRequest adminLogin = new LoginRequest();
        adminLogin.setUsername("admin");
        adminLogin.setPassword("adminpass");

        MvcResult adminLoginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andReturn();

        adminToken = extractToken(adminLoginResult.getResponse().getContentAsString());
        assertNotNull(adminToken);
    }

    // --------------------- POSITIVE TESTS ---------------------
    @Test
    @DisplayName("GET /api/users/me → should return current user profile")
    void getCurrentUser_ShouldReturn200AndUserData() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("Regular User"));
        assertTrue(content.contains("user@test.com"));
    }

    @Test
    @DisplayName("PUT /api/users/me → should update current user")
    void updateCurrentUser_ShouldReturn200AndUpdatedData() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("Updated User");

        MvcResult mvcResult = mockMvc.perform(put("/api/users/me")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("Updated User"));
    }

    @Test
    @DisplayName("GET /api/users/{id} → should return user info when admin")
    void findById_ShouldReturn200_WhenAdminAccesses() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/users/1")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("Regular User"));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} → should delete user when admin")
    void delete_ShouldReturn204_WhenAdminDeletesUser() throws Exception {
        mockMvc.perform(delete("/api/users/1")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

    // --------------------- NEGATIVE TESTS ---------------------
    @Test
    @DisplayName("GET /api/users/me → should return 401 when missing auth header")
    void getCurrentUser_ShouldReturn401_WhenNoAuth() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("PUT /api/users/me → should return 401 when missing auth header")
    void updateCurrentUser_ShouldReturn401_WhenNoAuth() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("Unauthorized Update");

        mockMvc.perform(put("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/users/{id} → should return 403 for non-admin users")
    void findById_ShouldReturn403_WhenUserNotAdmin() throws Exception {
        mockMvc.perform(get("/api/users/1")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /api/users/{id} → should return 403 for non-admin users")
    void delete_ShouldReturn403_WhenUserNotAdmin() throws Exception {
        mockMvc.perform(delete("/api/users/1")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /api/users/{id} → should return 404 if user not found")
    void findById_ShouldReturn404_WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/users/9999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    // --------------------- UTIL ---------------------
    private String extractToken(String json) {
        try {
            return objectMapper.readTree(json).get("token").asText();
        } catch (Exception e) {
            return null;
        }
    }
}
