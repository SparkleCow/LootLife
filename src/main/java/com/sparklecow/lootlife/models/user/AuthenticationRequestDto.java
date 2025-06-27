package com.sparklecow.lootlife.models.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequestDto(
        @NotBlank(message = "Username is mandatory")
        String username,
        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password must contain at least 8 characters")
        String password
) {
}
