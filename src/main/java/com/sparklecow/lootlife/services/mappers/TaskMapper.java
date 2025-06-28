package com.sparklecow.lootlife.services.mappers;

import com.sparklecow.lootlife.entities.Task;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.task.TaskDifficulty;
import com.sparklecow.lootlife.models.task.TaskRequestDto;
import com.sparklecow.lootlife.models.task.TaskResponseDto;
import com.sparklecow.lootlife.models.task.TaskUpdateDto;

public class TaskMapper {
    /**
     * Converts a taskRequestDto into a Task entity
     */
    public Task toEntity(TaskRequestDto dto, User user) {
        return Task.builder()
                .title(dto.title())
                .description(dto.description())
                .taskDifficulty(dto.taskDifficulty())
                .statsCategories(dto.statsCategories())
                .deadline(dto.deadline())
                .progressRequired(dto.progressRequired())
                .currentProgress(dto.progressRequired() != null ? 0 : null)
                .xpReward(calculateXpReward(dto.taskDifficulty(), dto.progressRequired()))
                .isActive(true)
                .isExpired(false)
                .user(user)
                .build();
    }

    /**
     * Converts a task entity into a taskResponseDto
     */
    public TaskResponseDto toResponseDto(Task task) {
        return new TaskResponseDto(
                task.getId(),
                task.getDeadline(),
                task.getIsActive(),
                task.getIsExpired(),
                task.getStatsCategories(),
                task.getTitle(),
                task.getDescription(),
                task.getXpReward(),
                task.getTaskDifficulty(),
                task.getProgressRequired(),
                task.getCurrentProgress()
        );
    }

    /**
     * Updates a task and converts it into a taskUpdateDto
     */
    public Task updateEntity(Task existingTask, TaskUpdateDto dto) {
        if (dto.title() != null) {
            existingTask.setTitle(dto.title());
        }

        if (dto.description() != null) {
            existingTask.setDescription(dto.description());
        }

        if (dto.taskDifficulty() != null) {
            existingTask.setTaskDifficulty(dto.taskDifficulty());
            existingTask.setXpReward(calculateXpReward(dto.taskDifficulty(), existingTask.getProgressRequired()));
        }

        if (dto.statsCategories() != null) {
            existingTask.setStatsCategories(dto.statsCategories());
        }

        if (dto.deadline() != null) {
            existingTask.setDeadline(dto.deadline());
        }

        if (dto.progressRequired() != null) {
            existingTask.setProgressRequired(dto.progressRequired());
            if (existingTask.getCurrentProgress() == null) {
                existingTask.setCurrentProgress(0);
            }
            existingTask.setXpReward(calculateXpReward(existingTask.getTaskDifficulty(), dto.progressRequired()));
        }

        if (dto.currentProgress() != null) {
            existingTask.setCurrentProgress(dto.currentProgress());
            if (existingTask.getProgressRequired() != null &&
                    dto.currentProgress() >= existingTask.getProgressRequired()) {
                existingTask.setIsActive(false);
            }
        }

        return existingTask;
    }

    /**
     * Calculate XP reward based on difficulty and if it is a quantitative task
     */
    private Long calculateXpReward(TaskDifficulty difficulty, Integer progressRequired) {
        if (difficulty == null) {
            return 1L;
        }

        long baseXp = switch (difficulty) {
            case EASY -> 1L;
            case MEDIUM -> 2L;
            case HARD -> 3L;
            case EXTREME -> 5L;
        };

        if (progressRequired != null && progressRequired > 0) {
            double multiplier = Math.min(1.0 + (progressRequired.doubleValue() / 10.0), 3.0);
            baseXp = Math.round(baseXp * multiplier);
        }

        return baseXp;
    }

    /**
     * Check if a task is completed
     */
    public boolean isTaskCompleted(Task task) {
        if (task.getProgressRequired() == null) {
            return !task.getIsActive();
        }

        return task.getCurrentProgress() != null &&
                task.getCurrentProgress() >= task.getProgressRequired();
    }

    /**
     * Calculate the task percentage progress
     */
    public Double getProgressPercentage(Task task) {
        if (task.getProgressRequired() == null || task.getCurrentProgress() == null) {
            return null;
        }

        return (double) task.getCurrentProgress() / task.getProgressRequired() * 100;
    }
}
