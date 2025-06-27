package com.sparklecow.lootlife.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "stats")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //General level
    private Integer level;
    private Long experiencePoints;
    private Long nextLevelAt;

    // Stats with individual levels
    private Integer strengthLevel;
    private Long strengthExperience;

    private Integer intelligenceLevel;
    private Long intelligenceExperience;

    private Integer wisdomLevel;
    private Long wisdomExperience;

    private Integer charismaLevel;
    private Long charismaExperience;

    private Integer dexterityLevel;
    private Long dexterityExperience;

    private Integer constitutionLevel;
    private Long constitutionExperience;

    private Integer luckLevel;
    private Long luckExperience;

    private Integer totalMissionsCompleted;
    private Integer streakDays;
    private Long timeInvestedMinutes;
}
