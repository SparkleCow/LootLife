package com.sparklecow.lootlife.services.mappers;

import com.sparklecow.lootlife.entities.Mission;
import com.sparklecow.lootlife.models.mission.MissionResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MissionMapper {

    public static MissionResponseDto toDto(Mission mission) {
        if (mission == null) return null;

        return new MissionResponseDto(
                mission.getId(),
                mission.getTitle(),
                mission.getDescription(),
                mission.getStatus(),
                mission.getDifficulty(),
                mission.getAssignedAt(),
                mission.getExpiresAt(),
                mission.getStartedAt(),
                mission.getCompletedAt(),
                mission.getTargetQuantity(),
                mission.getCurrentProgress(),
                mission.getXpReward(),
                mission.getBonusXpReward(),
                mission.getStatsCategories(),
                mission.getPriority(),
                mission.getIsStreakMission(),
                mission.getStreakDay(),
                mission.getRequiresValidation(),
                mission.getValidationNotes()
        );
    }
}
