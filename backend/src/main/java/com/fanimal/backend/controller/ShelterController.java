package com.fanimal.backend.controller;

import com.fanimal.backend.dto.shelter.ShelterRequest;
import com.fanimal.backend.dto.shelter.ShelterResponse;
import com.fanimal.backend.dto.shelter.ShelterUpdateRequest;
import com.fanimal.backend.service.ShelterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shelters")
@RequiredArgsConstructor
public class ShelterController {

    private final ShelterService shelterService;

    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    @PostMapping
    public ResponseEntity<ShelterResponse> create(@Valid @RequestBody ShelterRequest shelterRequest, @AuthenticationPrincipal UserDetails userDetails) {
        ShelterResponse shelterResponse = shelterService.create(shelterRequest, userDetails);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(shelterResponse);
    }

    @GetMapping
    public ResponseEntity<List<ShelterResponse>> findAll() {
        List<ShelterResponse> shelterResponses = shelterService.findAll();
        return ResponseEntity.ok().body(shelterResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelterResponse> findById(@PathVariable Long id) {
        ShelterResponse shelterResponse = shelterService.findById(id);
        return ResponseEntity.ok().body(shelterResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    @PutMapping("/{id}")
    public ResponseEntity<ShelterResponse> update(@PathVariable Long id, @Valid @RequestBody ShelterUpdateRequest shelterUpdateRequest, @AuthenticationPrincipal UserDetails userDetails) {
        ShelterResponse shelterResponse = shelterService.update(id, shelterUpdateRequest, userDetails);
        return ResponseEntity.ok().body(shelterResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SHELTER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        shelterService.delete(id, userDetails);
        return ResponseEntity.noContent().build();
    }
}