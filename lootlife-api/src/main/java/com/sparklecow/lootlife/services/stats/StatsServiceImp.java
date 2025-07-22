package com.sparklecow.lootlife.services.stats;

import com.sparklecow.lootlife.entities.Stats;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.stats.StatType;
import com.sparklecow.lootlife.models.stats.StatsResponseDto;
import com.sparklecow.lootlife.repositories.StatsRepository;
import com.sparklecow.lootlife.services.mappers.StatsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsServiceImp implements StatsService{

    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;
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
    public StatsResponseDto findStatsByUser(User user) {
        Long statsId = user.getStats().getId();
        Stats stats = statsRepository.findById(statsId)
                .orElseThrow(() -> new RuntimeException("Stats not found for id: " + statsId));
        return statsMapper.toResponseDto(stats);
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
                ? EXPERIENCE_FIRST_LEVEL
                : EXPERIENCE_FIRST_LEVEL * (1L << currentLevel);

        return xpForNextLevel - currentXp;
    }

    @Override
    public Stats calculateLevel(Stats stats) {
        Integer level = stats.getLevel();
        Long totalExperience = stats.getExperiencePoints();

        while (true) {
            long xpForNextLevel = (level == 0)
                    ? EXPERIENCE_FIRST_LEVEL
                    : EXPERIENCE_FIRST_LEVEL * (1L << level); //It calculates xp in an exponential way (2^level)

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
    public Stats addExperienceToSingleStat(Stats stats, StatType statType, Long experience) {
        switch (statType) {
            case STRENGTH -> {
                stats.setStrengthExperience(stats.getStrengthExperience() + experience);
                stats = calculateSingleStatLevel(stats, statType);
            }
            case INTELLIGENCE -> {
                stats.setIntelligenceExperience(stats.getIntelligenceExperience() + experience);
                stats = calculateSingleStatLevel(stats, statType);
            }
            case WISDOM -> {
                stats.setWisdomExperience(stats.getWisdomExperience() + experience);
                stats = calculateSingleStatLevel(stats, statType);
            }
            case CHARISMA -> {
                stats.setCharismaExperience(stats.getCharismaExperience() + experience);
                stats = calculateSingleStatLevel(stats, statType);
            }
            case DEXTERITY -> {
                stats.setDexterityExperience(stats.getDexterityExperience() + experience);
                stats = calculateSingleStatLevel(stats, statType);
            }
            case CONSTITUTION -> {
                stats.setConstitutionExperience(stats.getConstitutionExperience() + experience);
                stats = calculateSingleStatLevel(stats, statType);
            }
            case LUCK -> {
                stats.setLuckExperience(stats.getLuckExperience() + experience);
                stats = calculateSingleStatLevel(stats, statType);
            }
            default -> throw new IllegalArgumentException("Unknown stat type: " + statType);
        }
        return statsRepository.save(stats);
    }

    @Override
    public Stats calculateSingleStatLevel(Stats stats, StatType statType) {
        Integer level;
        Long experience;

        switch (statType) {
            case STRENGTH -> {
                level = stats.getStrengthLevel();
                experience = stats.getStrengthExperience();
            }
            case INTELLIGENCE -> {
                level = stats.getIntelligenceLevel();
                experience = stats.getIntelligenceExperience();
            }
            case WISDOM -> {
                level = stats.getWisdomLevel();
                experience = stats.getWisdomExperience();
            }
            case CHARISMA -> {
                level = stats.getCharismaLevel();
                experience = stats.getCharismaExperience();
            }
            case DEXTERITY -> {
                level = stats.getDexterityLevel();
                experience = stats.getDexterityExperience();
            }
            case CONSTITUTION -> {
                level = stats.getConstitutionLevel();
                experience = stats.getConstitutionExperience();
            }
            case LUCK -> {
                level = stats.getLuckLevel();
                experience = stats.getLuckExperience();
            }
            default -> throw new IllegalArgumentException("Unknown stat type: " + statType);
        }

        while (true) {
            long xpForNextLevel = (level == 0)
                    ? EXPERIENCE_FIRST_LEVEL
                    : EXPERIENCE_FIRST_LEVEL * (1L << level);

            if (experience >= xpForNextLevel) {
                experience -= xpForNextLevel;
                level++;
            } else {
                break;
            }
        }

        long xpForNextLevel = (level == 0)
                ? EXPERIENCE_FIRST_LEVEL
                : EXPERIENCE_FIRST_LEVEL * (1L << level);

        long nextLevelAt = xpForNextLevel - experience;

        switch (statType) {
            case STRENGTH -> {
                stats.setStrengthLevel(level);
                stats.setStrengthExperience(experience);
                stats.setStrengthNextLevelAt(nextLevelAt);
            }
            case INTELLIGENCE -> {
                stats.setIntelligenceLevel(level);
                stats.setIntelligenceExperience(experience);
                stats.setIntelligenceNextLevelAt(nextLevelAt);
            }
            case WISDOM -> {
                stats.setWisdomLevel(level);
                stats.setWisdomExperience(experience);
                stats.setWisdomNextLevelAt(nextLevelAt);
            }
            case CHARISMA -> {
                stats.setCharismaLevel(level);
                stats.setCharismaExperience(experience);
                stats.setCharismaNextLevelAt(nextLevelAt);
            }
            case DEXTERITY -> {
                stats.setDexterityLevel(level);
                stats.setDexterityExperience(experience);
                stats.setDexterityNextLevelAt(nextLevelAt);
            }
            case CONSTITUTION -> {
                stats.setConstitutionLevel(level);
                stats.setConstitutionExperience(experience);
                stats.setConstitutionNextLevelAt(nextLevelAt);
            }
            case LUCK -> {
                stats.setLuckLevel(level);
                stats.setLuckExperience(experience);
                stats.setLuckNextLevelAt(nextLevelAt);
            }
        }
        return statsRepository.save(stats);
    }

    @Override
    public void resetStats(Stats stats) {
        stats.setLevel(0);
        stats.setExperiencePoints(0L);
        stats.setNextLevelAt(EXPERIENCE_FIRST_LEVEL);

        stats.setStrengthLevel(0);
        stats.setStrengthExperience(0L);
        stats.setStrengthNextLevelAt(EXPERIENCE_FIRST_LEVEL);

        stats.setIntelligenceLevel(0);
        stats.setIntelligenceExperience(0L);
        stats.setIntelligenceNextLevelAt(EXPERIENCE_FIRST_LEVEL);

        stats.setWisdomLevel(0);
        stats.setWisdomExperience(0L);
        stats.setWisdomNextLevelAt(EXPERIENCE_FIRST_LEVEL);

        stats.setCharismaLevel(0);
        stats.setCharismaExperience(0L);
        stats.setCharismaNextLevelAt(EXPERIENCE_FIRST_LEVEL);

        stats.setDexterityLevel(0);
        stats.setDexterityExperience(0L);
        stats.setDexterityNextLevelAt(EXPERIENCE_FIRST_LEVEL);

        stats.setConstitutionLevel(0);
        stats.setConstitutionExperience(0L);
        stats.setConstitutionNextLevelAt(EXPERIENCE_FIRST_LEVEL);

        stats.setLuckLevel(0);
        stats.setLuckExperience(0L);
        stats.setLuckNextLevelAt(EXPERIENCE_FIRST_LEVEL);

        stats.setTotalMissionsCompleted(0);

        statsRepository.save(stats);
    }

    @Override
    public Stats addMissionComplete(Stats stats) {
        stats.setTotalMissionsCompleted(stats.getTotalMissionsCompleted() + 1);
        return statsRepository.save(stats);
    }
}
