package com.sparklecow.lootlife.services.mappers;

import com.sparklecow.lootlife.entities.Stats;
import com.sparklecow.lootlife.models.stats.StatsResponseDto;
import org.springframework.stereotype.Component;

@Component
public class StatsMapper {

    public StatsResponseDto toResponseDto(Stats stats) {
        if (stats == null) return null;

        return new StatsResponseDto(
                stats.getId(),
                stats.getLevel(),
                stats.getExperiencePoints(),
                stats.getNextLevelAt(),
                stats.getStrengthLevel(),
                stats.getStrengthExperience(),
                stats.getIntelligenceLevel(),
                stats.getIntelligenceExperience(),
                stats.getWisdomLevel(),
                stats.getWisdomExperience(),
                stats.getCharismaLevel(),
                stats.getCharismaExperience(),
                stats.getDexterityLevel(),
                stats.getDexterityExperience(),
                stats.getConstitutionLevel(),
                stats.getConstitutionExperience(),
                stats.getLuckLevel(),
                stats.getLuckExperience(),
                stats.getTotalMissionsCompleted()
        );
    }
}