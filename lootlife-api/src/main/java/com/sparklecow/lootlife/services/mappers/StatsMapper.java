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
                stats.getStrengthNextLevelAt(),

                stats.getIntelligenceLevel(),
                stats.getIntelligenceExperience(),
                stats.getIntelligenceNextLevelAt(),

                stats.getWisdomLevel(),
                stats.getWisdomExperience(),
                stats.getWisdomNextLevelAt(),

                stats.getCharismaLevel(),
                stats.getCharismaExperience(),
                stats.getCharismaNextLevelAt(),

                stats.getDexterityLevel(),
                stats.getDexterityExperience(),
                stats.getDexterityNextLevelAt(),

                stats.getConstitutionLevel(),
                stats.getConstitutionExperience(),
                stats.getConstitutionNextLevelAt(),

                stats.getLuckLevel(),
                stats.getLuckExperience(),
                stats.getLuckNextLevelAt(),

                stats.getTotalMissionsCompleted()
        );
    }
}