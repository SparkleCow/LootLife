package com.sparklecow.lootlife.models.task;

import com.sparklecow.lootlife.models.stats.StatType;

import java.time.LocalDateTime;
import java.util.Set;

public record TaskResponseDto(
        Long id,
        LocalDateTime deadline,
        Boolean isActive,
        Boolean isExpired,
        Set<StatType> statsCategories,
        String title,
        String description,
        Long xpReward,
        TaskDifficulty taskDifficulty,
        Integer progressRequired,
        Integer currentProgress
) {
}
