package com.gl.apigateway.controller;

import com.gl.apigateway.dto.AuthRequest;
import com.gl.apigateway.dto.AuthResponse;
import com.gl.apigateway.model.User;
import com.gl.apigateway.repository.UserRepository;
import com.gl.apigateway.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        // Find user by username
        User user = userRepository.findByUsernameWithRoles(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // Simple password check (In production, use BCrypt)
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate Token
        String token = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(new AuthResponse(token));
    }
}
