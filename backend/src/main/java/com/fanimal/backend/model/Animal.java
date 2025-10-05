package com.fanimal.backend.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "animals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String species;
    private String breed;
    private int age;
    private String photoUrl;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;
}
