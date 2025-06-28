package com.sparklecow.lootlife.services.stats;

import com.sparklecow.lootlife.entities.Stats;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.UnaryOperator;

public interface StatsService {
     Stats createStats(UserDetails userDetails);
     Stats addStatExperience(Long experience, UnaryOperator<Stats> statsResolver);
     Stats updateStatLevel(Long level, Long totalExperience, UnaryOperator<Stats> statsResolver);
     Stats resetStats(UserDetails userDetails);
}
