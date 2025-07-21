package com.sparklecow.lootlife.services.mission;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparklecow.lootlife.entities.Mission;
import com.sparklecow.lootlife.entities.Stats;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.mission.MissionStatus;
import com.sparklecow.lootlife.models.stats.StatType;
import com.sparklecow.lootlife.models.task.TaskDifficulty;
import com.sparklecow.lootlife.repositories.MissionRepository;
import com.sparklecow.lootlife.repositories.UserRepository;
import com.sparklecow.lootlife.services.ia.IAService;
import com.sparklecow.lootlife.services.stats.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MissionServiceImp implements MissionService{

    private final MissionRepository missionRepository;
    private final IAService iaService;
    private final UserRepository userRepository;
    private final StatsService statsService;

    /*Utils*/
    @Override
    public boolean isCompleted(Mission mission) {
        return mission.getStatus() == MissionStatus.COMPLETED;
    }

    @Override
    public boolean isActive(Mission mission) {
        return mission.getStatus() == MissionStatus.ACTIVE;
    }

    @Override
    public boolean isExpired(Mission mission) {
        return LocalDateTime.now().isAfter(mission.getExpiresAt());
    }

    public double getProgressPercentage(Mission mission) {
        if (mission.getTargetQuantity() == null || mission.getTargetQuantity() == 0) {
            return isCompleted(mission) ? 100.0 : 0.0;
        }
        return Math.min(100.0, (mission.getCurrentProgress() * 100.0) / mission.getTargetQuantity());
    }

    public boolean canBeCompleted(Mission mission) {
        return !isExpired(mission) &&
                (mission.getStatus() == MissionStatus.ACTIVE || mission.getStatus() == MissionStatus.PENDING);
    }

    public boolean hasMissionToday(User user) {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        return missionRepository.existsByUserAndAssignedAtBetween(user, startOfDay, endOfDay);
    }

    public void cleanExpiredMissions(User user) {
        List<Mission> oldMissions = missionRepository.findByUserAndStatusIn(user, List.of(MissionStatus.PENDING, MissionStatus.ACTIVE, MissionStatus.CANCELLED));

        for (Mission m : oldMissions) {
            if (isExpired(m)) {
                missionRepository.delete(m);
                log.info("Mission with id {} deleted", m.getId());
            }
            if (m.getStatus() == MissionStatus.EXPIRED || m.getStatus() == MissionStatus.CANCELLED){
                log.info("Mission with id {} deleted", m.getId());
                missionRepository.delete(m);
            }
        }
    }

    /*CRUD Operations*/

    public Mission createMission(User user) throws JsonProcessingException {
        User userFromDb = userRepository.findByUsername(user.getUsername()).orElseThrow();

        cleanExpiredMissions(userFromDb);

        if (hasMissionToday(userFromDb)) {
            throw new IllegalStateException("There is already a mission assigned for today.");
        }

        TaskDifficulty taskDifficulty = suggestDifficultyForUser(userFromDb);
        List<StatType> userWeakStats = analyzeUserWeakStats(userFromDb);

        long xp;
        switch (taskDifficulty) {
            case EASY -> xp = 50;
            case MEDIUM -> xp = 500;
            case HARD -> xp = 1000;
            case EXTREME -> xp = 5000;
            default -> xp = 0;
        }

        String prompt = """
        Eres un NPC maestro de misiones en un videojuego de gamificación de la vida real.
        Tu trabajo es asignar misiones a los jugadores con un tono épico y motivador. Las misiones deben ser logrables y emocionantes.
        Devuélveme únicamente un JSON válido, sin texto adicional ni explicaciones.
        
        Sin embargo, las acciones que el jugador debe realizar deben ser completamente posibles en la vida real.
        Puedes usar metáforas como “dragones”, “hechizos”, “mazmorras”, etc., pero siempre debes explicar cómo se traducen a una acción cotidiana concreta.
                
        Genera una misión para el siguiente jugador:

        - Dificultad: %s
        - Categorías estadísticas en las que el jugador es débil: %s
        - Nivel del jugador: %d

        La misión debe:
        - Poder completarse en menos de 24 horas.
        - Debe tener una acción clara y realizable: caminar, ordenar, escribir, hacer ejercicio, reflexionar, socializar, etc.
        - Tener una descripción inmersiva y clara, como si fuera parte de una historia.
        - NO debe pedir cosas imposibles, como hablar con un dragón real, usar magia verdadera, ir a otro mundo, etc.    
        - Incluir una meta cuantificable (targetQuantity) como número entero >= 1.
        - Tener un motivo épico o justificación narrativa en el campo "generationReason".

        Devuelve ÚNICAMENTE un JSON con los siguientes campos:
        {
          "title": "...",
          "description": "...",
          "targetQuantity": número entero >= 1,
          "generationReason": "..."
        }
        """.formatted(
                taskDifficulty.name().toLowerCase(),
                userWeakStats.stream().limit(3).map(Enum::name).collect(Collectors.toList()),
                userFromDb.getStats().getLevel()
        );

        String rawContent = iaService.sendRequest(prompt);
        String cleanedJson = rawContent
                .replaceAll("(?s)```json\\s*", "")
                .replaceAll("(?s)```", "")
                .trim();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> iaData = objectMapper.readValue(cleanedJson, new TypeReference<>() {});

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = now.toLocalDate().atTime(23, 59, 59);

        Mission newMission = Mission.builder()
                .title((String) iaData.get("title"))
                .description((String) iaData.get("description"))
                .difficulty(taskDifficulty)
                .targetQuantity(((Number) iaData.get("targetQuantity")).intValue())
                .currentProgress(0)
                .assignedAt(now)
                .expiresAt(endOfDay)
                .status(MissionStatus.PENDING)
                .statsCategories(userWeakStats.stream()
                        .limit(3)
                        .collect(Collectors.toSet()))
                .aiGenerated(true)
                .xpReward(xp)
                .generationReason((String) iaData.get("generationReason"))
                .userLevelAtGeneration(userFromDb.getStats().getLevel())
                .user(userFromDb)
                .build();

        log.info("Creating new mission: {} for user: {}", newMission.getTitle(), newMission.getUser().getUsername());

        return missionRepository.save(newMission);
    }

    /**
     * Get all user´s missions which are currently assigned
     */
    public List<Mission> getActiveMissions(User user) {
        List<MissionStatus> activeStatuses = Arrays.asList(MissionStatus.ACTIVE, MissionStatus.PENDING);
        return missionRepository.findByUserAndStatusIn(user, activeStatuses);
    }

    /**
     * Get all user´s missions.
     */
    public List<Mission> getUserMissions(User user) {
        return missionRepository.findByUser(user);
    }

    /*Mission progress completion*/

    /**
     * Update progress
     */
    public Mission updateMissionProgress(Long missionId, int progressIncrement) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        if (!canBeCompleted(mission)) {
            throw new RuntimeException("Mission cannot be updated: " + mission.getStatus());
        }

        mission.setCurrentProgress(mission.getCurrentProgress() + progressIncrement);

        // Auto-start mission if it was pending
        if (mission.getStatus() == MissionStatus.PENDING) {
            mission.setStatus(MissionStatus.ACTIVE);
            mission.setStartedAt(LocalDateTime.now());
        }

        // Check if the mission is now completed
        if (mission.getTargetQuantity() != null &&
                mission.getCurrentProgress() >= mission.getTargetQuantity()) {
            completeMission(mission);
        }

        log.info("Updated mission progress: {} - {}/{}",
                mission.getTitle(), mission.getCurrentProgress(), mission.getTargetQuantity());

        return missionRepository.save(mission);
    }


    public Mission completeMission(Mission mission) {
        if (!canBeCompleted(mission)) {
            throw new RuntimeException("Mission cannot be completed: " + mission.getStatus());
        }

        mission.setStatus(MissionStatus.COMPLETED);
        mission.setCompletedAt(LocalDateTime.now());

        // Set progress to 100% if it wasn't already
        if (mission.getTargetQuantity() != null) {
            mission.setCurrentProgress(mission.getTargetQuantity());
        }

        log.info("Mission completed: {} by user: {}", mission.getTitle(), mission.getUser().getUsername());
        return missionRepository.save(mission);
    }

    public Mission startMission(Long missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        if (mission.getStatus() != MissionStatus.PENDING) {
            throw new RuntimeException("Mission cannot be started: " + mission.getStatus());
        }

        if (isExpired(mission)) {
            mission.setStatus(MissionStatus.EXPIRED);
            missionRepository.save(mission);
            throw new RuntimeException("Mission has expired");
        }

        mission.setStatus(MissionStatus.ACTIVE);
        mission.setStartedAt(LocalDateTime.now());

        log.info("Mission started: {} by user: {}", mission.getTitle(), mission.getUser().getUsername());
        return missionRepository.save(mission);
    }

    // Mission analytics and stats */

    /**
     * Get mission stats for a user
     */
    public Map<String, Object> getUserMissionStats(User user) {

        Map<String, Object> stats = new HashMap<>();

        // Total missions
        long totalMissions = missionRepository.countByUserAndStatus(user, MissionStatus.COMPLETED);
        stats.put("totalCompleted", totalMissions);

        // Active missions count
        List<Mission> activeMissions = getActiveMissions(user);
        stats.put("activeMissions", activeMissions.size());

        // Progress summary
        double avgProgress = activeMissions.stream()
                .mapToDouble(this::getProgressPercentage)
                .average()
                .orElse(0.0);
        stats.put("averageProgress", Math.round(avgProgress * 100.0) / 100.0);

        return stats;
    }


    /**
     * Get missions that will expire soon for notification
     */
    public List<Mission> getMissionsExpiringSoon(User user, int hoursAhead) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime soonTime = now.plusHours(hoursAhead);
        List<MissionStatus> activeStatuses = Arrays.asList(MissionStatus.ACTIVE, MissionStatus.PENDING);

        return missionRepository.findMissionsExpiringSoon(user, now, soonTime, activeStatuses);
    }

    // AI mission generation support*/

    /**
     * It analyzes user stats to suggest missions types
     */
    public List<StatType> analyzeUserWeakStats(User user) {
        Stats stats = user.getStats();
        log.info("User stats: {}", stats);
        if (stats == null) {
            return Arrays.asList(StatType.values());
        }

        Map<StatType, Integer> statLevels = Map.of(
                StatType.STRENGTH, stats.getStrengthLevel() != null ? stats.getStrengthLevel() : 1,
                StatType.INTELLIGENCE, stats.getIntelligenceLevel() != null ? stats.getIntelligenceLevel() : 1,
                StatType.WISDOM, stats.getWisdomLevel() != null ? stats.getWisdomLevel() : 1,
                StatType.CHARISMA, stats.getCharismaLevel() != null ? stats.getCharismaLevel() : 1,
                StatType.DEXTERITY, stats.getDexterityLevel() != null ? stats.getDexterityLevel() : 1,
                StatType.CONSTITUTION, stats.getConstitutionLevel() != null ? stats.getConstitutionLevel() : 1,
                StatType.LUCK, stats.getLuckLevel() != null ? stats.getLuckLevel() : 1
        );

        int averageLevel = (int) statLevels.values().stream().mapToInt(Integer::intValue).average().orElse(1);

        return statLevels.entrySet().stream()
                .filter(entry -> entry.getValue() <= averageLevel)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * It suggests a mission difficulty based in user level
     */
    public TaskDifficulty suggestDifficultyForUser(User user) {
        if (user.getStats() == null || user.getStats().getLevel() == null) {
            return TaskDifficulty.EASY;
        }

        int level = user.getStats().getLevel();

        if (level < 5) {
            return TaskDifficulty.EASY;
        } else if (level < 15) {
            return TaskDifficulty.MEDIUM;
        } else if (level < 30) {
            return TaskDifficulty.HARD;
        } else {
            return TaskDifficulty.EXTREME;
        }
    }

    /**
     * It calculates xp reward based in mission difficulty
     */
    public long calculateSuggestedXpReward(TaskDifficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 50L;
            case MEDIUM -> 100L;
            case HARD -> 200L;
            case EXTREME -> 400L;
        };
    }

    /*Schedules*/

    /**
     * Job programado para expirar misiones vencidas
     */
    @Scheduled(fixedRate = 600000) // 10 minutes
    public void expireOverdueMissions() {
        List<MissionStatus> activeStatuses = Arrays.asList(MissionStatus.ACTIVE, MissionStatus.PENDING);
        List<Mission> expiredMissions = missionRepository.findExpiredMissions(LocalDateTime.now(), activeStatuses);

        for (Mission mission : expiredMissions) {
            mission.setStatus(MissionStatus.EXPIRED);
            log.info("Mission expired: {} for user: {}", mission.getTitle(), mission.getUser().getUsername());
        }

        if (!expiredMissions.isEmpty()) {
            missionRepository.saveAll(expiredMissions);
            log.info("Expired {} overdue missions", expiredMissions.size());
        }
    }

    /* Mission validation */

    public List<Mission> getMissionsRequiringValidation() {
        return missionRepository.findByRequiresValidationTrueAndStatus(MissionStatus.COMPLETED);
    }

    /**
     * Validate a mission (Manually)
     */
    public Mission validateMission(Long missionId, boolean approved, String validationNotes) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found"));

        if (!mission.getRequiresValidation()) {
            throw new RuntimeException("Mission does not require validation");
        }

        mission.setValidationNotes(validationNotes);

        if (!approved) {
            mission.setStatus(MissionStatus.FAILED);
        }
        // If approved, status remains COMPLETED

        log.info("Mission {} validated: {} - {}",
                mission.getTitle(), approved ? "APPROVED" : "REJECTED", validationNotes);

        return missionRepository.save(mission);
    }

    /* Bulk operation */

    /**
     * It cancels all active user missions
     */
    public void cancelAllUserMissions(User user, String reason) {
        List<Mission> activeMissions = getActiveMissions(user);
        log.info("Active missions for user {}: {}",user.getUsername() ,activeMissions);

        for (Mission mission : activeMissions) {
            mission.setStatus(MissionStatus.CANCELLED);
            mission.setValidationNotes(reason);
        }

        missionRepository.saveAll(activeMissions);
        log.info("Cancelled {} missions for user: {} - Reason: {}",
                activeMissions.size(), user.getUsername(), reason);
    }

    public List<Mission> getRecentMissionHistoryForAI(User user, int daysBack) {
        LocalDateTime sinceDate = LocalDateTime.now().minusDays(daysBack);
        return missionRepository.findRecentMissionsForUser(user, sinceDate);
    }
}
