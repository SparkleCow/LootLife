package com.sparklecow.lootlife.models.token;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TokenValidationRequest(
        @NotNull
        @NotBlank
        String token
) {
}
