package com.example.coreservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDTO {
    @NotBlank(message = "Username must not be null or blank")
    String username;

    @NotBlank(message = "Password must not be null or blank")
    String password;
}
