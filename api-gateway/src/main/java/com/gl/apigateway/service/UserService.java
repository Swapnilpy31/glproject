package com.gl.apigateway.service;

import com.gl.apigateway.model.User;
import com.gl.apigateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /**
     * Validates if the user exists and loads roles efficiently.
     * Demonstrates handling N+1 problem with JOIN FETCH.
     *
     * @param username The username from token
     */
    @Transactional(readOnly = true)
    public void validateUser(String username) {
        if ("anonymousUser".equals(username))
            return; // Skip for unauthenticated if allowed

        log.debug("Validating user: {}", username);

        // Use optimized query
        User user = userRepository.findByUsernameWithRoles(username)
                .orElse(null);

        // In real gateway, might check account status/roles
        // Here we just ensure we can load it without N+1
        if (user != null) {
            log.debug("User validated with {} roles", user.getRoles().size());
        }
    }
}
