package com.sparklecow.lootlife.models.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordUpdateDto(
        @NotBlank(message = "Current password is required")
        String currentPassword,

        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "New password must contain at least 8 characters")
        String newPassword
) {}
