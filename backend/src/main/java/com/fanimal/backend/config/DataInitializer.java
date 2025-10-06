package com.fanimal.backend.config;

import com.fanimal.backend.model.Role;
import com.fanimal.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Bean
    public CommandLineRunner initRoles() {
        return args -> {
            for (Role.RoleName roleName : Role.RoleName.values()) {
                roleRepository.findByName(roleName)
                        .orElseGet(() -> {
                            System.out.println("Creating role: " + roleName);
                            return roleRepository.save(Role.builder().name(roleName).build());
                        });
            }
        };
    }
}
