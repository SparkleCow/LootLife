package com.sparklecow.LootLife.models.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record UserResponseDto(
        Long id,
        String username,
        String firstName,
        String lastName,
        String email,
        String profileImageUrl,
        String bannerImageUrl,
        Integer age,
        LocalDate birthDate,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt,
        boolean isVerified,
        boolean isEnabled,
        List<String> roles
) {
}
