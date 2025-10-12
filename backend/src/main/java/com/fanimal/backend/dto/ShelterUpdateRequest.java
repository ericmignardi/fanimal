package com.fanimal.backend.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShelterUpdateRequest {

    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;
}
