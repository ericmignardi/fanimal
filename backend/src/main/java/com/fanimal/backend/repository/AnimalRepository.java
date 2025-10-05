package com.fanimal.backend.repository;

import com.fanimal.backend.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Shelter,Long> {
}
