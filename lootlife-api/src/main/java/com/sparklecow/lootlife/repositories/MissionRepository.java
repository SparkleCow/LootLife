package com.sparklecow.lootlife.repositories;

import com.sparklecow.lootlife.entities.Mission;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.mission.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    // Find missions by user
    List<Mission> findByUser(User user);

    // Find missions by user and status
    List<Mission> findByUserAndStatus(User user, MissionStatus status);

    // Find active missions for a user
    List<Mission> findByUserAndStatusIn(User user, List<MissionStatus> statuses);

    // Find expired missions that need to be updated
    @Query("SELECT m FROM Mission m WHERE m.expiresAt < :currentTime AND m.status IN :activeStatuses")
    List<Mission> findExpiredMissions(@Param("currentTime") LocalDateTime currentTime, @Param("activeStatuses") List<MissionStatus> activeStatuses);

    // Find missions expiring soon (for notifications)
    @Query("SELECT m FROM Mission m WHERE m.user = :user AND m.expiresAt BETWEEN :now AND :soonTime AND m.status IN :activeStatuses")
    List<Mission> findMissionsExpiringSoon(@Param("user") User user, @Param("now") LocalDateTime now, @Param("soonTime") LocalDateTime soonTime, @Param("activeStatuses") List<MissionStatus> activeStatuses);

    // Count completed missions for a user
    long countByUserAndStatus(User user, MissionStatus status);

    // Count missions by user and date range
    @Query("SELECT COUNT(m) FROM Mission m WHERE m.user = :user AND m.completedAt BETWEEN :startDate AND :endDate AND m.status = :status")
    long countCompletedMissionsInDateRange(@Param("user") User user, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("status") MissionStatus status);

    // Find current streak missions for a user
    List<Mission> findByUserAndIsStreakMissionTrueAndStatusIn(User user, List<MissionStatus> statuses);

    // Find missions requiring validation
    List<Mission> findByRequiresValidationTrueAndStatus(MissionStatus status);

    // Get user's recent missions for AI analysis
    @Query("SELECT m FROM Mission m WHERE m.user = :user AND m.createdAt >= :sinceDate ORDER BY m.createdAt DESC")
    List<Mission> findRecentMissionsForUser(@Param("user") User user, @Param("sinceDate") LocalDateTime sinceDate);

    Boolean existsByUserAndAssignedAtBetween(User user, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
