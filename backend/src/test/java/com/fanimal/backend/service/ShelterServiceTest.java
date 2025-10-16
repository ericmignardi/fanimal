package com.fanimal.backend.service;

import com.fanimal.backend.dto.shelter.*;
import com.fanimal.backend.model.Role;
import com.fanimal.backend.model.Shelter;
import com.fanimal.backend.model.User;
import com.fanimal.backend.repository.ShelterRepository;
import com.fanimal.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ShelterService Unit Tests")
class ShelterServiceTest {

    @InjectMocks
    private ShelterService shelterService;
    @Mock
    private ShelterRepository shelterRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserDetails userDetailsMock;

    private static ShelterRequest shelterRequest;
    private static User owner;
    private static Role userRole;

    @BeforeAll
    static void beforeAll() {
        // Role
        userRole = Role.builder().name(Role.RoleName.USER).build();
        // Owner User
        owner = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@test.com")
                .username("username")
                .password("password")
                .roles(Set.of(userRole))
                .build();
        // ShelterRequest
        shelterRequest = new ShelterRequest();
        shelterRequest.setName("Test Shelter");
        shelterRequest.setDescription("Test description");
        shelterRequest.setAddress("Test address");
    }

    @BeforeEach
    void setupMocks() {
        lenient().when(userDetailsMock.getUsername()).thenReturn(owner.getUsername());
    }

    // --------------------- POSITIVE TESTS ---------------------

    @WithMockUser
    @Test
    void create() {
        Shelter savedShelter = Shelter.builder()
                .id(1L)
                .name(shelterRequest.getName())
                .description(shelterRequest.getDescription())
                .address(shelterRequest.getAddress())
                .owner(owner)
                .build();

        when(userRepository.findByUsername(owner.getUsername())).thenReturn(Optional.of(owner));
        when(shelterRepository.save(any(Shelter.class))).thenReturn(savedShelter);

        ShelterResponse response = shelterService.create(shelterRequest, userDetailsMock);

        assertNotNull(response);
        assertEquals(shelterRequest.getName(), response.getName());
        assertEquals(shelterRequest.getDescription(), response.getDescription());
        assertEquals(shelterRequest.getAddress(), response.getAddress());

        verify(userRepository, times(1)).findByUsername(owner.getUsername());
        verify(shelterRepository, times(1)).save(any(Shelter.class));
    }

    @Test
    void findAll() {
        Shelter shelter1 = Shelter.builder().id(1L).name("Shelter 1").description("Desc 1").address("Address 1").owner(owner).build();
        Shelter shelter2 = Shelter.builder().id(2L).name("Shelter 2").description("Desc 2").address("Address 2").owner(owner).build();

        when(shelterRepository.findAll()).thenReturn(List.of(shelter1, shelter2));

        List<ShelterResponse> shelters = shelterService.findAll();

        assertNotNull(shelters);
        assertEquals(2, shelters.size());
        assertEquals("Shelter 1", shelters.get(0).getName());
        assertEquals("Shelter 2", shelters.get(1).getName());

        verify(shelterRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        Shelter shelter = Shelter.builder().id(1L).name("Shelter 1").description("Desc 1").address("Address 1").owner(owner).build();

        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));

        ShelterResponse response = shelterService.findById(1L);

        assertNotNull(response);
        assertEquals("Shelter 1", response.getName());
        assertEquals("Desc 1", response.getDescription());
        assertEquals("Address 1", response.getAddress());

        verify(shelterRepository, times(1)).findById(1L);
    }

    @WithMockUser
    @Test
    void update() {
        Shelter shelter = Shelter.builder().id(1L).name("Old Name").description("Old Desc").address("Old Address").owner(owner).build();
        ShelterUpdateRequest updateRequest = new ShelterUpdateRequest();
        updateRequest.setName("New Name");
        updateRequest.setDescription("New Desc");
        updateRequest.setAddress("New Address");

        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));
        when(shelterRepository.save(any(Shelter.class))).thenReturn(shelter);

        ShelterResponse response = shelterService.update(1L, updateRequest, userDetailsMock);

        assertNotNull(response);
        assertEquals("New Name", response.getName());
        assertEquals("New Desc", response.getDescription());
        assertEquals("New Address", response.getAddress());

        verify(shelterRepository, times(1)).findById(1L);
        verify(shelterRepository, times(1)).save(any(Shelter.class));
    }

    @WithMockUser
    @Test
    void delete() {
        Shelter shelter = Shelter.builder().id(1L).name("Shelter 1").description("Desc 1").address("Address 1").owner(owner).build();

        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));

        assertDoesNotThrow(() -> shelterService.delete(1L, userDetailsMock));

        verify(shelterRepository, times(1)).findById(1L);
        verify(shelterRepository, times(1)).delete(shelter);
    }

    // --------------------- NEGATIVE TESTS ---------------------

    @WithMockUser
    @Test
    void createShouldReturn404WhenUserNotFound() {
        when(userDetailsMock.getUsername()).thenReturn("nonexistent");
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> shelterService.create(shelterRequest, userDetailsMock));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
        verify(shelterRepository, never()).save(any(Shelter.class));
    }

    @Test
    void findByIdShouldReturn404WhenShelterNotFound() {
        when(shelterRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> shelterService.findById(1L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Shelter not found", exception.getReason());
    }

    @WithMockUser
    @Test
    void updateShouldReturn404WhenShelterNotFound() {
        ShelterUpdateRequest updateRequest = new ShelterUpdateRequest();
        updateRequest.setName("New Name");
        updateRequest.setDescription("New Desc");
        updateRequest.setAddress("New Address");

        when(shelterRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> shelterService.update(1L, updateRequest, userDetailsMock));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Shelter not found", exception.getReason());
    }

    @WithMockUser
    @Test
    void updateShouldReturn401WhenUnauthorized() {
        User anotherUser = User.builder().id(2L).username("otherUser").roles(Set.of(userRole)).build();
        Shelter shelter = Shelter.builder().id(1L).name("Old Name").owner(owner).build();
        ShelterUpdateRequest updateRequest = new ShelterUpdateRequest();
        updateRequest.setName("New Name");
        updateRequest.setDescription("New Desc");
        updateRequest.setAddress("New Address");

        when(userDetailsMock.getUsername()).thenReturn("otherUser");
        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> shelterService.update(1L, updateRequest, userDetailsMock));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Unauthorized", exception.getReason());
    }

    @WithMockUser
    @Test
    void deleteShouldReturn404WhenShelterNotFound() {
        when(shelterRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> shelterService.delete(1L, userDetailsMock));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Shelter not found", exception.getReason());
    }

    @WithMockUser
    @Test
    void deleteShouldReturn401WhenUnauthorized() {
        Shelter shelter = Shelter.builder().id(1L).name("Shelter 1").owner(owner).build();

        when(userDetailsMock.getUsername()).thenReturn("otherUser");
        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> shelterService.delete(1L, userDetailsMock));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Unauthorized", exception.getReason());
    }
}
