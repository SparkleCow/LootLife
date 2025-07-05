package com.sparklecow.lootlife.models.user;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserRequestDto(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String username,

        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must be at most 50 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name must be at most 50 characters")
        String lastName,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must contain at least 8 characters")
        String password,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotNull(message = "Age is required")
        @Max(value = 150, message = "Age must be realistic")
        Integer age,

        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate
) {}