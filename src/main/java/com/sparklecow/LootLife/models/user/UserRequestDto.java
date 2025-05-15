package com.sparklecow.LootLife.models.user;

import java.time.LocalDate;

public record UserRequestDto(
        String username,
        String firstName,
        String lastName,
        String password,
        String email,
        Integer age,
        LocalDate birthDate
) {}