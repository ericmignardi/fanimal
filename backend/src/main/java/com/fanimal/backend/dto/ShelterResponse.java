package com.fanimal.backend.dto;

import com.fanimal.backend.model.Shelter;
import com.fanimal.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShelterResponse {

    private Long id;
    private String name;
    private String description;
    private String address;
    private User owner;

    public static ShelterResponse fromEntity(Shelter shelter) {
        return ShelterResponse.builder()
                .id(shelter.getId())
                .name(shelter.getName())
                .description(shelter.getDescription())
                .address(shelter.getAddress())
                .owner(shelter.getOwner())
                .build();
    }
}
