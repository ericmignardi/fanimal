package com.fanimal.backend.service;

import com.fanimal.backend.dto.shelter.ShelterRequest;
import com.fanimal.backend.dto.shelter.ShelterResponse;
import com.fanimal.backend.dto.shelter.ShelterUpdateRequest;
import com.fanimal.backend.model.Role;
import com.fanimal.backend.model.Shelter;
import com.fanimal.backend.model.User;
import com.fanimal.backend.repository.ShelterRepository;
import com.fanimal.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
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
    private static ShelterRequest shelterRequest;

    @BeforeAll
    static void beforeAll() {
        // ShelterRequest
        shelterRequest = new ShelterRequest();
        shelterRequest.setName("Test Shelter");
        shelterRequest.setDescription("Test description");
        shelterRequest.setAddress("Test address");
    }

    // --------------------- POSITIVE TESTS ---------------------

    @WithMockUser
    @Test
    void create() {
        // GIVEN
        Role role = Role.builder().name(Role.RoleName.USER).build();
        User owner = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@test.com")
                .username("username")
                .password("password")
                .roles(Set.of(role))
                .build();
        Shelter savedShelter = Shelter.builder()
                .id(1L)
                .name(shelterRequest.getName())
                .description(shelterRequest.getDescription())
                .address(shelterRequest.getAddress())
                .owner(owner)
                .build();
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        // WHEN
        when(userDetails.getUsername()).thenReturn(owner.getUsername());
        when(userRepository.findByUsername(owner.getUsername())).thenReturn(Optional.of(owner));
        when(shelterRepository.save(any(Shelter.class))).thenReturn(savedShelter);
        // ACT
        ShelterResponse shelterResponse = shelterService.create(shelterRequest, userDetails);
        // THEN
        assertNotNull(shelterResponse);
        assertEquals(shelterRequest.getName(), shelterResponse.getName());
        assertEquals(shelterRequest.getDescription(), shelterResponse.getDescription());
        assertEquals(shelterRequest.getAddress(), shelterResponse.getAddress());
        // VERIFY
        Mockito.verify(userRepository, times(1)).findByUsername(owner.getUsername());
        Mockito.verify(shelterRepository, times(1)).save(any(Shelter.class));
    }

    @Test
    void findAll() {
        // GIVEN
        Role role = Role.builder().name(Role.RoleName.USER).build();
        User owner = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@test.com")
                .username("username")
                .password("password")
                .roles(Set.of(role))
                .build();
        Shelter shelter1 = Shelter.builder().id(1L).name("Shelter 1").description("Desc 1").address("Address 1").owner(owner).build();
        Shelter shelter2 = Shelter.builder().id(2L).name("Shelter 2").description("Desc 2").address("Address 2").owner(owner).build();
        // WHEN
        when(shelterRepository.findAll()).thenReturn(List.of(shelter1, shelter2));
        // ACT
        List<ShelterResponse> shelters = shelterService.findAll();
        // THEN
        assertNotNull(shelters);
        assertEquals(2, shelters.size());
        assertEquals("Shelter 1", shelters.get(0).getName());
        assertEquals("Shelter 2", shelters.get(1).getName());
        // VERIFY
        Mockito.verify(shelterRepository, times(1)).findAll();
    }

    @Test
    void findById() {
        // GIVEN
        Role role = Role.builder().name(Role.RoleName.USER).build();
        User owner = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@test.com")
                .username("username")
                .password("password")
                .roles(Set.of(role))
                .build();
        Shelter shelter = Shelter.builder().id(1L).name("Shelter 1").description("Desc 1").address("Address 1").owner(owner).build();
        // WHEN
        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));
        // ACT
        ShelterResponse response = shelterService.findById(1L);
        // THEN
        assertNotNull(response);
        assertEquals("Shelter 1", response.getName());
        assertEquals("Desc 1", response.getDescription());
        assertEquals("Address 1", response.getAddress());
        // VERIFY
        Mockito.verify(shelterRepository, times(1)).findById(1L);
    }

    @WithMockUser
    @Test
    void update() {
        // GIVEN
        Role role = Role.builder().name(Role.RoleName.USER).build();
        User owner = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@test.com")
                .username("username")
                .password("password")
                .roles(Set.of(role))
                .build();
        Shelter shelter = Shelter.builder().id(1L).name("Old Name").description("Old Desc").address("Old Address").owner(owner).build();
        ShelterUpdateRequest updateRequest = new ShelterUpdateRequest();
        updateRequest.setName("New Name");
        updateRequest.setDescription("New Desc");
        updateRequest.setAddress("New Address");
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        // WHEN
        when(userDetails.getUsername()).thenReturn(owner.getUsername());
        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));
        when(shelterRepository.save(any(Shelter.class))).thenReturn(shelter);
        // ACT
        ShelterResponse response = shelterService.update(1L, updateRequest, userDetails);
        // THEN
        assertNotNull(response);
        assertEquals("New Name", response.getName());
        assertEquals("New Desc", response.getDescription());
        assertEquals("New Address", response.getAddress());
        // VERIFY
        Mockito.verify(shelterRepository, times(1)).findById(1L);
        Mockito.verify(shelterRepository, times(1)).save(any(Shelter.class));
    }

    @WithMockUser
    @Test
    void delete() {
        // GIVEN
        Role role = Role.builder().name(Role.RoleName.USER).build();
        User owner = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@test.com")
                .username("username")
                .password("password")
                .roles(Set.of(role))
                .build();
        Shelter shelter = Shelter.builder().id(1L).name("Shelter 1").description("Desc 1").address("Address 1").owner(owner).build();
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        // WHEN
        when(userDetails.getUsername()).thenReturn(owner.getUsername());
        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));
        // ACT
        assertDoesNotThrow(() -> shelterService.delete(1L, userDetails));
        // VERIFY
        Mockito.verify(shelterRepository, times(1)).findById(1L);
        Mockito.verify(shelterRepository, times(1)).delete(shelter);
    }

    // --------------------- NEGATIVE TESTS ---------------------

    // --------------------- NEGATIVE TESTS ---------------------

    @WithMockUser
    @Test
    void createShouldReturn404WhenUserNotFound() {
        // GIVEN
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("nonexistent");
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        // ACT & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> shelterService.create(shelterRequest, userDetails));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());
        // VERIFY
        Mockito.verify(shelterRepository, never()).save(any(Shelter.class));
    }

    @Test
    void findByIdShouldReturn404WhenShelterNotFound() {
        // WHEN
        when(shelterRepository.findById(1L)).thenReturn(Optional.empty());
        // ACT & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> shelterService.findById(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Shelter not found", exception.getReason());
    }

    @WithMockUser
    @Test
    void updateShouldReturn404WhenShelterNotFound() {
        // GIVEN
        ShelterUpdateRequest updateRequest = new ShelterUpdateRequest();
        updateRequest.setName("New Name");
        updateRequest.setDescription("New Desc");
        updateRequest.setAddress("New Address");
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        // WHEN
        when(shelterRepository.findById(1L)).thenReturn(Optional.empty());
        // ACT & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> shelterService.update(1L, updateRequest, userDetails));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Shelter not found", exception.getReason());
    }

    @WithMockUser
    @Test
    void updateShouldReturn401WhenUnauthorized() {
        // GIVEN
        Role role = Role.builder().name(Role.RoleName.USER).build();
        User owner = User.builder().id(1L).username("owner").roles(Set.of(role)).build();
        Shelter shelter = Shelter.builder().id(1L).name("Old Name").owner(owner).build();
        ShelterUpdateRequest updateRequest = new ShelterUpdateRequest();
        updateRequest.setName("New Name");
        updateRequest.setDescription("New Desc");
        updateRequest.setAddress("New Address");
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        // WHEN
        when(userDetails.getUsername()).thenReturn("otherUser");
        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));
        // ACT & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> shelterService.update(1L, updateRequest, userDetails));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Unauthorized", exception.getReason());
    }

    @WithMockUser
    @Test
    void deleteShouldReturn404WhenShelterNotFound() {
        // GIVEN
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        // WHEN
        when(shelterRepository.findById(1L)).thenReturn(Optional.empty());
        // ACT & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> shelterService.delete(1L, userDetails));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Shelter not found", exception.getReason());
    }

    @WithMockUser
    @Test
    void deleteShouldReturn401WhenUnauthorized() {
        // GIVEN
        Role role = Role.builder().name(Role.RoleName.USER).build();
        User owner = User.builder().id(1L).username("owner").roles(Set.of(role)).build();
        Shelter shelter = Shelter.builder().id(1L).name("Shelter 1").owner(owner).build();
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        // WHEN
        when(userDetails.getUsername()).thenReturn("otherUser");
        when(shelterRepository.findById(1L)).thenReturn(Optional.of(shelter));
        // ACT & THEN
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> shelterService.delete(1L, userDetails));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Unauthorized", exception.getReason());
    }

}