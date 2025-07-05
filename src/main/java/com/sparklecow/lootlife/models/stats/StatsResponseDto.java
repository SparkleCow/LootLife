package com.sparklecow.lootlife.models.stats;

public record StatsResponseDto(
        Long id,

        Integer level,
        Long experiencePoints,
        Long nextLevelAt,

        Integer strengthLevel,
        Long strengthExperience,
        Long strengthNextLevelAt,

        Integer intelligenceLevel,
        Long intelligenceExperience,
        Long intelligenceNextLevelAt,

        Integer wisdomLevel,
        Long wisdomExperience,
        Long wisdomNextLevelAt,

        Integer charismaLevel,
        Long charismaExperience,
        Long charismaNextLevelAt,

        Integer dexterityLevel,
        Long dexterityExperience,
        Long dexterityNextLevelAt,

        Integer constitutionLevel,
        Long constitutionExperience,
        Long constitutionNextLevelAt,

        Integer luckLevel,
        Long luckExperience,
        Long luckNextLevelAt,

        Integer totalMissionsCompleted
) {}