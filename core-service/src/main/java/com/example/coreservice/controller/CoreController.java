package com.example.coreservice.controller;

import com.example.coreservice.dto.UserRequestDTO;
import com.example.coreservice.dto.UserResponseDTO;
import com.example.coreservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CoreController {

    private final UserService userService;

    /**
     * Endpoint to save user data.
     * Validates input and delegates to service layer.
     */
    @PostMapping("/core")
    public ResponseEntity<UserResponseDTO> createCore(@Valid @RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO response = userService.processAndSaveUser(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
