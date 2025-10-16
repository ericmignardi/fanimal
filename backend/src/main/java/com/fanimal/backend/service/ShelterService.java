package com.fanimal.backend.service;

import com.fanimal.backend.dto.shelter.ShelterRequest;
import com.fanimal.backend.dto.shelter.ShelterResponse;
import com.fanimal.backend.dto.shelter.ShelterUpdateRequest;
import com.fanimal.backend.model.Shelter;
import com.fanimal.backend.model.User;
import com.fanimal.backend.repository.ShelterRepository;
import com.fanimal.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;
    private final UserRepository userRepository;

    public ShelterResponse create(ShelterRequest shelterRequest, UserDetails userDetails) {
        User owner = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Shelter shelter = Shelter.builder()
                .name(shelterRequest.getName())
                .description(shelterRequest.getDescription())
                .address(shelterRequest.getAddress())
                .owner(owner)
                .build();
        shelterRepository.save(shelter);
        return ShelterResponse.fromEntity(shelter);
    }

    public List<ShelterResponse> findAll() {
        return shelterRepository.findAll().stream()
                .map(ShelterResponse::fromEntity)
                .toList();
    }

    public ShelterResponse findById(Long id) {
        Shelter shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelter not found"));
        return ShelterResponse.fromEntity(shelter);
    }

    public ShelterResponse update(Long id, ShelterUpdateRequest shelterUpdateRequest, UserDetails userDetails) {
        Shelter shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelter not found"));
        if (!isOwnerOrAdmin(shelter, userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        shelter.setName(shelterUpdateRequest.getName());
        shelter.setDescription(shelterUpdateRequest.getDescription());
        shelter.setAddress(shelterUpdateRequest.getAddress());
        shelterRepository.save(shelter);
        return ShelterResponse.fromEntity(shelter);
    }

    public void delete(Long id, UserDetails userDetails) {
        Shelter shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelter not found"));
        if (!isOwnerOrAdmin(shelter, userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        shelterRepository.delete(shelter);
    }

    private boolean isOwnerOrAdmin(Shelter shelter, UserDetails userDetails) {
        return shelter.getOwner().getUsername().equals(userDetails.getUsername()) ||
                userDetails.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
