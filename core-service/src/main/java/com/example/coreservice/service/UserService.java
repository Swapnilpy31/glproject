package com.example.coreservice.service;

import com.example.coreservice.dto.UserRequestDTO;
import com.example.coreservice.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO processAndSaveUser(UserRequestDTO request);
}
