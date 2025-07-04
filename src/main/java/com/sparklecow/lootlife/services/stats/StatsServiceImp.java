package com.sparklecow.lootlife.services.stats;

import com.sparklecow.lootlife.entities.Stats;
import com.sparklecow.lootlife.models.stats.StatType;
import com.sparklecow.lootlife.repositories.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class StatsServiceImp implements StatsService{

    private final StatsRepository statsRepository;
    private static final long EXPERIENCE_FIRST_LEVEL = 100L;

    @Override
    public Stats createStats() {
        return statsRepository.save(
                Stats.builder()
                        .level(0)
                        .experiencePoints(0L)
                        .nextLevelAt(EXPERIENCE_FIRST_LEVEL) // general XP needed for level 1

                        .strengthLevel(0)
                        .strengthExperience(0L)
                        .strengthNextLevelAt(EXPERIENCE_FIRST_LEVEL)

                        .intelligenceLevel(0)
                        .intelligenceExperience(0L)
                        .intelligenceNextLevelAt(EXPERIENCE_FIRST_LEVEL)

                        .wisdomLevel(0)
                        .wisdomExperience(0L)
                        .wisdomNextLevelAt(EXPERIENCE_FIRST_LEVEL)

                        .charismaLevel(0)
                        .charismaExperience(0L)
                        .charismaNextLevelAt(EXPERIENCE_FIRST_LEVEL)

                        .dexterityLevel(0)
                        .dexterityExperience(0L)
                        .dexterityNextLevelAt(EXPERIENCE_FIRST_LEVEL)

                        .constitutionLevel(0)
                        .constitutionExperience(0L)
                        .constitutionNextLevelAt(EXPERIENCE_FIRST_LEVEL)

                        .luckLevel(0)
                        .luckExperience(0L)
                        .luckNextLevelAt(EXPERIENCE_FIRST_LEVEL)

                        .totalMissionsCompleted(0)
                        .build()
        );
    }


    @Override
    public Stats addStatExperience(Stats stats, UnaryOperator<Stats> statsResolver) {
        Stats updatedStats = statsResolver.apply(stats);
        statsRepository.save(updatedStats);
        return updatedStats;
    }

    @Override
    public Stats addLevelExperience(Stats stats, Long experience) {
        stats.setExperiencePoints(stats.getExperiencePoints()+experience);
        stats = calculateLevel(stats);
        stats.setNextLevelAt(calculateXpToNextLevel(stats));
        return statsRepository.save(stats);
    }

    @Override
    public Long calculateXpToNextLevel(Stats stats) {
        int currentLevel = stats.getLevel();
        long currentXp = stats.getExperiencePoints();

        long xpForNextLevel = (currentLevel == 0)
                ? 100L
                : 100L * (1L << currentLevel);

        return xpForNextLevel - currentXp;
    }

    @Override
    public Stats calculateLevel(Stats stats) {
        Integer level = stats.getLevel();
        Long totalExperience = stats.getExperiencePoints();

        while (true) {
            long xpForNextLevel = (level == 0)
                    ? 100L
                    : 100L * (1L << level); //It calculates xp in an exponential way (2^level)

            if (totalExperience >= xpForNextLevel) {
                totalExperience -= xpForNextLevel;
                level++;
            } else {
                break;
            }
        }

        stats.setLevel(level);
        stats.setExperiencePoints(totalExperience);
        return stats;
    }

    @Override
    public Long calculateXpToNextStatLevel(Stats stats, StatType statType) {
        return 0L;
    }

    @Override
    public Stats addExperienceToSingleStat(Stats stats, StatType statType, Long experience) {
        return null;
    }

    @Override
    public Stats calculateSingleStatLevel(Stats stats, StatType statType) {
        return null;
    }

    @Override
    public void resetStats(Stats stats  ) {
    }
}
