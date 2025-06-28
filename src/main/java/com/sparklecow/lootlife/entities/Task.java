package com.sparklecow.lootlife.entities;

import com.sparklecow.lootlife.models.stats.StatType;
import com.sparklecow.lootlife.models.task.TaskDifficulty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "last_modified_at", nullable = true, insertable = false)
    private LocalDateTime lastModifiedAt;

    private LocalDateTime deadline;

    private Boolean isExpired;

    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "task_stat_categories",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name = "stat_category")
    @Size(max = 3, message = "A task cannot have more than 3 stat categories")
    private Set<StatType> statsCategories = new HashSet<>();



    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 255)
    private String description;

    private Long xpReward;

    @Enumerated(EnumType.STRING)
    private TaskDifficulty taskDifficulty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //Quantitative tasks
    private Integer progressRequired;

    private Integer currentProgress;
}
