package com.sparklecow.LootLife.models.user;

public record UserPasswordUpdateDto(
        String currentPassword,
        String newPassword
) {}
