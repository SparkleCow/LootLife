package com.sparklecow.lootlife.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "stats")
/*This class contains the user stats (Level, Badges, Experience, etc.)*/
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at", nullable = true, insertable = false)
    private LocalDateTime lastModifiedAt;

    //General level
    private Integer level;
    private Long experiencePoints;
    private Long nextLevelAt;

    // Stats with individual levels
    private Integer strengthLevel;
    private Long strengthExperience;
    private Long strengthNextLevelAt;

    private Integer intelligenceLevel;
    private Long intelligenceExperience;
    private Long intelligenceNextLevelAt;

    private Integer wisdomLevel;
    private Long wisdomExperience;
    private Long wisdomNextLevelAt;

    private Integer charismaLevel;
    private Long charismaExperience;
    private Long charismaNextLevelAt;

    private Integer dexterityLevel;
    private Long dexterityExperience;
    private Long dexterityNextLevelAt;

    private Integer constitutionLevel;
    private Long constitutionExperience;
    private Long constitutionNextLevelAt;

    private Integer luckLevel;
    private Long luckExperience;
    private Long luckNextLevelAt;

    private Integer totalMissionsCompleted;
}
