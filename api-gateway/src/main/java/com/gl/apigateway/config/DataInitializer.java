package com.gl.apigateway.config;

import com.gl.apigateway.model.Role;
import com.gl.apigateway.model.User;
import com.gl.apigateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
        // Check if admin verification data exists
        if (userRepository.findByUsernameWithRoles("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("password"); // In production, hash this!

            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setUser(admin);

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);
            System.out.println("User 'admin' created with password 'password'");
        }
    }
}
