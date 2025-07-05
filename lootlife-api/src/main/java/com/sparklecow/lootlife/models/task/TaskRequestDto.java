package com.sparklecow.lootlife.models.task;

import com.sparklecow.lootlife.models.stats.StatType;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Set;

public record TaskRequestDto(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title cannot exceed 100 characters")
        String title,

        @NotBlank(message = "Description is required")
        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description,

        @NotNull(message = "Task difficulty is required")
        TaskDifficulty taskDifficulty,

        @NotEmpty(message = "At least one stat category is required")
        @Size(max = 3, message = "A task cannot have more than 3 stat categories")
        Set<StatType> statsCategories,

        @Future(message = "Deadline must be in the future")
        LocalDateTime deadline,

        @Min(value = 1, message = "Progress required must be at least 1")
        Integer progressRequired
) {
}
