package com.sparklecow.lootlife.models.task;

import com.sparklecow.lootlife.models.stats.StatType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

public record TaskUpdateDto(
        @Size(max = 100, message = "Title cannot exceed 100 characters")
        String title,

        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description,

        TaskDifficulty taskDifficulty,

        @Size(max = 3, message = "A task cannot have more than 3 stat categories")
        Set<StatType> statsCategories,

        @Future(message = "Deadline must be in the future")
        LocalDateTime deadline,

        @Min(value = 1, message = "Progress required must be at least 1")
        Integer progressRequired,

        @Min(value = 0, message = "Current progress cannot be negative")
        Integer currentProgress
) {
}
