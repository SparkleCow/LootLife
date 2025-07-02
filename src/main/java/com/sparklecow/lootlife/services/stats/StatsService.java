package com.sparklecow.lootlife.services.stats;

import com.sparklecow.lootlife.entities.Stats;

import java.util.function.UnaryOperator;

public interface StatsService {

     Stats createStats();

     //These methods allow control each stat level and experience
     Stats addStatExperience(Stats stats, UnaryOperator<Stats> statsResolver);
     Stats updateStatLevel(Long level, Long totalExperience, UnaryOperator<Stats> statsResolver);
     Long calculateXpToNextStatLevel(Stats stats);

     //These methods allow control the general level and experience
     Stats calculateLevel(Stats stats);
     Stats addLevelExperience(Stats stats, Long experience);
     Long calculateXpToNextLevel(Stats stats);

     void resetStats(Stats stats);
}
