package com.fanimal.backend.config;

import com.fanimal.backend.model.Role;
import com.fanimal.backend.model.User;
import com.fanimal.backend.repository.RoleRepository;
import com.fanimal.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner init() {
        return args -> {
            // Ensure all roles exist
            for (Role.RoleName roleName : Role.RoleName.values()) {
                roleRepository.findByName(roleName)
                        .orElseGet(() -> {
                            System.out.println("Creating role: " + roleName);
                            return roleRepository.save(Role.builder().name(roleName).build());
                        });
            }
            // Create admin user only if it doesn't already exist
            userRepository.findByEmail("admin@fanimal.com").ifPresentOrElse(
                    existing -> System.out.println("Admin user already exists"),
                    () -> {
                        Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Admin role not found"));
                        Set<Role> roles = new HashSet<>();
                        roles.add(adminRole);
                        User admin = User.builder()
                                .name("Eric Mignardi")
                                .email("admin@fanimal.com")
                                .username("admin")
                                .password(passwordEncoder.encode("admin"))
                                .roles(roles)
                                .build();
                        userRepository.save(admin);
                        System.out.println("Admin user created successfully!");
                    }
            );
        };
    }
}
