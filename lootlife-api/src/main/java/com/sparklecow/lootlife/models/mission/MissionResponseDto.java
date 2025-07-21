package com.sparklecow.lootlife.models.mission;

import com.sparklecow.lootlife.models.stats.StatType;
import com.sparklecow.lootlife.models.task.TaskDifficulty;

import java.time.LocalDateTime;
import java.util.Set;

public record MissionResponseDto(
        Long id,
        String title,
        String description,
        MissionStatus status,
        TaskDifficulty difficulty,
        LocalDateTime assignedAt,
        LocalDateTime expiresAt,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        Integer targetQuantity,
        Integer currentProgress,
        Long xpReward,
        Long bonusXpReward,
        Set<StatType> statsCategories,
        Integer priority,
        Boolean isStreakMission,
        Integer streakDay,
        Boolean requiresValidation,
        String validationNotes
) {
}
