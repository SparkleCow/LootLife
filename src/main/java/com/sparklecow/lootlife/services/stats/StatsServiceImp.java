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
    public Stats addStatExperience(Long experience, UnaryOperator<Stats> statsResolver) {
        return null;
    }

    @Override
    public Stats updateStatLevel(Long level, Long totalExperience, UnaryOperator<Stats> statsResolver) {
        return null;
    }

    @Override
    public Stats calculateLevel(Stats stats) {
        Integer level = stats.getLevel();
        return null;
    }

    @Override
    public Stats addLevelExperience(Stats stats, Long experience) {
        Integer level = stats.getLevel();
        if(level==0){

        }
        return null;
    }

    @Override
    public void resetStats(Stats stats  ) {
    }
}
