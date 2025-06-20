package com.sparklecow.lootlife.models.user;

import java.time.LocalDate;

public record UserUpdateDto(
        String firstName,
        String lastName,
        String email,
        Integer age,
        LocalDate birthDate
) {}
