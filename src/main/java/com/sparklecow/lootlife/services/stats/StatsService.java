package com.sparklecow.lootlife.services.stats;

import com.sparklecow.lootlife.entities.Stats;
import com.sparklecow.lootlife.models.stats.StatType;

import java.util.function.UnaryOperator;

public interface StatsService {

     // Create a blank stats entity
     Stats createStats();

     // General level methods

     /**
      * Calculates and updates the general level of the user
      * based on their total experience.
      */
     Stats calculateLevel(Stats stats);

     /**
      * Adds experience points to the general level,
      * and updates level if necessary.
      */
     Stats addLevelExperience(Stats stats, Long experience);

     /**
      * Calculates how many XP points are required to reach the next general level.
      */
     Long calculateXpToNextLevel(Stats stats);


     // Individual stat methods

     /**
      * Adds experience to specific stats using a lambda resolver.
      * This also triggers level-up logic for any stat that exceeds the XP required.
      */
     Stats addStatExperience(Stats stats, UnaryOperator<Stats> statsResolver);

     /**
      * Calculates how many XP points are required to reach the next level
      * for the given stat type.
      */
     Long calculateXpToNextStatLevel(Stats stats, StatType statType);

     /**
      * Adds experience to a specific stat and updates its level if necessary.
      */
     Stats addExperienceToSingleStat(Stats stats, StatType statType, Long experience);

     /**
      * Calculates and updates the level of a single stat if it has enough experience.
      */
     Stats calculateSingleStatLevel(Stats stats, StatType statType);

     // Utility

     /**
      * Resets all stats to default values (e.g. level = 0, xp = 0).
      */
     void resetStats(Stats stats);
}