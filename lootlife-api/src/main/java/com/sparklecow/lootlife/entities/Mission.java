package com.sparklecow.lootlife.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparklecow.lootlife.models.mission.MissionStatus;
import com.sparklecow.lootlife.models.stats.StatType;
import com.sparklecow.lootlife.models.task.TaskDifficulty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "missions")
@EntityListeners(AuditingEntityListener.class)
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at", nullable = true, insertable = false)
    private LocalDateTime lastModifiedAt;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 800)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionStatus status = MissionStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskDifficulty difficulty;

    @Column(nullable = false)
    private LocalDateTime assignedAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    // Progress tracking
    private Integer targetQuantity; // For quantitative missions (e.g., complete 3 tasks)

    private Integer currentProgress = 0;

    // Rewards
    @Column(nullable = false)
    private Long xpReward;

    private Long bonusXpReward; // Additional XP for early completion or streak

    // Stat categories this mission affects
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "mission_stat_categories",
            joinColumns = @JoinColumn(name = "mission_id")
    )
    @Column(name = "stat_category")
    @Size(max = 3, message = "A mission cannot have more than 3 stat categories")
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)

    private Set<StatType> statsCategories = new HashSet<>();

    // AI Generation metadata
    @Column(name = "ai_generated", nullable = false)
    private Boolean aiGenerated = true;

    @Column(name = "generation_reason", length = 800)
    private String generationReason; // Why this mission was generated (based on user stats/behavior)

    @Column(name = "user_level_at_generation")
    private Integer userLevelAtGeneration; // User's level when mission was generated

    // Mission priority (1-10, where 10 is highest priority)
    @Min(1)
    @Max(10)
    private Integer priority = 5;

    // Streak tracking
    private Boolean isStreakMission = false;

    private Integer streakDay; // Day in current streak (for streak missions)

    // Completion validation
    private Boolean requiresValidation = false; // Some missions might need manual validation

    private String validationNotes; // Additional notes for validation

    // User relationship
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
