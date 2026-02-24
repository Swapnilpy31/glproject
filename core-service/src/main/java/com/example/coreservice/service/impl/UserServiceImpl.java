package com.example.coreservice.service.impl;

import com.example.coreservice.dto.UserRequestDTO;
import com.example.coreservice.dto.UserResponseDTO;
import com.example.coreservice.entity.UserEntity;
import com.example.coreservice.repository.UserRepository;
import com.example.coreservice.service.UserService;
import com.example.coreservice.service.loadbalancer.LoadBalancingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LoadBalancingService loadBalancingService;

    @Override
    @Transactional
    public UserResponseDTO processAndSaveUser(UserRequestDTO request) {
        log.info("Starting user data processing for username: {}", request.getUsername());

        // Save to MongoDB using declarative transaction
        saveUser(request);

        // Use load balancer to simulate routing/assignment
        String assignedServer = loadBalancingService.getNextServer();
        log.info("User {} successfully processed and routed via {}", request.getUsername(), assignedServer);

        return UserResponseDTO.builder()
                .message("Data saved successfully")
                .status("PROCESSING")
                .build();
    }

    /**
     * Private helper method to encapsulate specific action (Single Responsibility)
     */
    private void saveUser(UserRequestDTO request) {
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(request.getPassword()) // In production, use BCryptPasswordEncoder
                .build();

        userRepository.save(user);
    }
}
