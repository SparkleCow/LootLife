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
/*This class contains the user stats (Level, Badges, Experience, etc)*/
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
    private Integer level = 0;
    private Long experiencePoints = 0L;
    private Long nextLevelAt = 0L;

    // Stats with individual levels
    private Integer strengthLevel = 0;
    private Long strengthExperience = 0L;

    private Integer intelligenceLevel = 0;
    private Long intelligenceExperience = 0L;

    private Integer wisdomLevel = 0;
    private Long wisdomExperience = 0L;

    private Integer charismaLevel = 0;
    private Long charismaExperience = 0L;

    private Integer dexterityLevel = 0;
    private Long dexterityExperience = 0L;

    private Integer constitutionLevel = 0;
    private Long constitutionExperience = 0L;

    private Integer luckLevel = 0;
    private Long luckExperience = 0L;

    private Integer totalMissionsCompleted = 0;
}
