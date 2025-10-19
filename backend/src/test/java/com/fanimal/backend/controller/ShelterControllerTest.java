package com.fanimal.backend.controller;

import com.fanimal.backend.dto.shelter.ShelterRequest;
import com.fanimal.backend.dto.shelter.ShelterUpdateRequest;
import com.fanimal.backend.dto.user.LoginRequest;
import com.fanimal.backend.dto.user.RegisterRequest;
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
@DisplayName("ShelterController Integration Tests with H2")
class ShelterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;
    private ShelterRequest shelterRequest;
    private ShelterUpdateRequest shelterUpdateRequest;

    @BeforeEach
    void setUp() throws Exception {
        // Register and log in to get a token
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Shelter Owner");
        registerRequest.setEmail("shelterowner@test.com");
        registerRequest.setUsername("shelterowner");
        registerRequest.setPassword("password");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("shelterowner");
        loginRequest.setPassword("password");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn();

        token = extractToken(loginResult.getResponse().getContentAsString());
        assertNotNull(token);

        shelterRequest = new ShelterRequest();
        shelterRequest.setName("Happy Tails Shelter");
        shelterRequest.setDescription("Caring home for rescued animals");
        shelterRequest.setAddress("123 Animal Ave, Ottawa");

        shelterUpdateRequest = new ShelterUpdateRequest();
        shelterUpdateRequest.setName("Happy Paws Shelter");
        shelterUpdateRequest.setDescription("Updated caring home for rescued pets");
        shelterUpdateRequest.setAddress("456 New Hope St, Ottawa");
    }

    // --------------------- POSITIVE TESTS ---------------------
    @Test
    void create_ShouldReturn201AndShelterData() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/shelters")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shelterRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertTrue(response.contains("Happy Tails Shelter"));
        assertTrue(response.contains("123 Animal Ave"));
    }

    @Test
    void findAll_ShouldReturnListOfShelters() throws Exception {
        mockMvc.perform(post("/api/shelters")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shelterRequest)))
                .andExpect(status().isCreated());

        MvcResult mvcResult = mockMvc.perform(get("/api/shelters")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("Happy Tails Shelter"));
    }

    @Test
    void findById_ShouldReturnShelter_WhenExists() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/shelters")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shelterRequest)))
                .andReturn();

        Long createdId = extractId(createResult.getResponse().getContentAsString());
        assertNotNull(createdId);

        MvcResult mvcResult = mockMvc.perform(get("/api/shelters/" + createdId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Happy Tails Shelter"));
    }

    @Test
    void update_ShouldReturnUpdatedShelter_WhenAuthorized() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/shelters")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shelterRequest)))
                .andReturn();

        Long createdId = extractId(createResult.getResponse().getContentAsString());

        MvcResult mvcResult = mockMvc.perform(put("/api/shelters/" + createdId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shelterUpdateRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("Happy Paws Shelter"));
        assertTrue(content.contains("Updated caring home"));
    }

    @Test
    void delete_ShouldReturn204_WhenAuthorized() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/shelters")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shelterRequest)))
                .andReturn();

        Long createdId = extractId(createResult.getResponse().getContentAsString());

        mockMvc.perform(delete("/api/shelters/" + createdId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    // --------------------- NEGATIVE TESTS ---------------------
    @Test
    void create_ShouldReturn401_WhenNoAuthHeader() throws Exception {
        mockMvc.perform(post("/api/shelters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shelterRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findById_ShouldReturn404_WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/shelters/99999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_ShouldReturn404_WhenShelterNotFound() throws Exception {
        mockMvc.perform(put("/api/shelters/99999")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shelterUpdateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_ShouldReturn404_WhenShelterNotFound() throws Exception {
        mockMvc.perform(delete("/api/shelters/99999")
                        .header("Authorization", "Bearer " + token))
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

    private Long extractId(String json) {
        try {
            return objectMapper.readTree(json).get("id").asLong();
        } catch (Exception e) {
            return null;
        }
    }
}
