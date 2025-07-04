package com.sparklecow.lootlife.services.stats;

import com.sparklecow.lootlife.entities.Stats;
import com.sparklecow.lootlife.repositories.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class StatsServiceImp implements StatsService{

    private final StatsRepository statsRepository;

    @Override
    public Stats createStats() {
        return statsRepository.save(
                Stats.builder()
                        .level(0)
                        .experiencePoints(0L)
                        .nextLevelAt(0L)
                        .strengthLevel(0)
                        .strengthExperience(0L)
                        .intelligenceLevel(0)
                        .intelligenceExperience(0L)
                        .wisdomLevel(0)
                        .wisdomExperience(0L)
                        .charismaLevel(0)
                        .charismaExperience(0L)
                        .dexterityLevel(0)
                        .dexterityExperience(0L)
                        .constitutionLevel(0)
                        .constitutionExperience(0L)
                        .luckLevel(0)
                        .luckExperience(0L)
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
    public Stats updateStatLevel(Long level, Long totalExperience, UnaryOperator<Stats> statsResolver) {
        return null;
    }

    @Override
    public Long calculateXpToNextStatLevel(Stats stats) {
        return 0L;
    }

    @Override
    public Stats addLevelExperience(Stats stats, Long experience) {
        Integer level = stats.getLevel();
        if(level==0){

        }
        return null;
    }

    @Override
    public Stats calculateLevel(Stats stats) {
        Integer level = stats.getLevel();
        Long totalExperience = stats.getExperiencePoints();

        while (true) {
            long xpForNextLevel = (level == 0)
                    ? 100L
                    : 100L * (1L << level); //It calculates xp in an exponential way

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
    public Long calculateXpToNextLevel(Stats stats) {
        if(stats.getLevel()==0){
            return 100L;
        }
        Integer level = stats.getLevel();
        Long totalExperience = stats.getExperiencePoints();
        Long experienceForNextLevel = (long) (100 * Math.pow(2, level));
        return experienceForNextLevel - totalExperience;
    }

    @Override
    public void resetStats(Stats stats  ) {
    }
}
