package com.sparklecow.lootlife.models.stats;

public record StatsResponseDto(
        Long id,

        Integer level,
        Long experiencePoints,
        Long nextLevelAt,

        Integer strengthLevel,
        Long strengthExperience,

        Integer intelligenceLevel,
        Long intelligenceExperience,

        Integer wisdomLevel,
        Long wisdomExperience,

        Integer charismaLevel,
        Long charismaExperience,

        Integer dexterityLevel,
        Long dexterityExperience,

        Integer constitutionLevel,
        Long constitutionExperience,

        Integer luckLevel,
        Long luckExperience,

        Integer totalMissionsCompleted
) {}