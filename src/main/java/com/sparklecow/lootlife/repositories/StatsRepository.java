package com.sparklecow.lootlife.repositories;

import com.sparklecow.lootlife.entities.Stats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
}
