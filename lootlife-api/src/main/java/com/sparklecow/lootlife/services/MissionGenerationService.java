package com.sparklecow.lootlife.services;

import com.sparklecow.lootlife.entities.Mission;
import com.sparklecow.lootlife.entities.User;
import com.sparklecow.lootlife.models.mission.MissionType;
import com.sparklecow.lootlife.models.stats.StatType;
import com.sparklecow.lootlife.models.task.TaskDifficulty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionGenerationService {

    private final MissionService missionService;

    // Templates de misiones para diferentes tipos
    private static final Map<StatType, List<String>> MISSION_TEMPLATES = Map.of(
            StatType.STRENGTH, List.of(
                    "Completa {quantity} tareas de ejercicio físico",
                    "Realiza {quantity} actividades deportivas",
                    "Dedica {quantity} sesiones a entrenamiento físico"
            ),
            StatType.INTELLIGENCE, List.of(
                    "Completa {quantity} tareas de aprendizaje",
                    "Lee durante {quantity} sesiones de estudio",
                    "Resuelve {quantity} problemas o ejercicios mentales"
            ),
            StatType.WISDOM, List.of(
                    "Reflexiona y completa {quantity} tareas de meditación",
                    "Realiza {quantity} actividades de mindfulness",
                    "Dedica tiempo a {quantity} momentos de introspección"
            ),
            StatType.CHARISMA, List.of(
                    "Interactúa socialmente en {quantity} actividades",
                    "Practica habilidades de comunicación {quantity} veces",
                    "Participa en {quantity} actividades sociales"
            ),
            StatType.DEXTERITY, List.of(
                    "Completa {quantity} tareas que requieran habilidad manual",
                    "Practica {quantity} actividades de coordinación",
                    "Realiza {quantity} ejercicios de destreza"
            ),
            StatType.CONSTITUTION, List.of(
                    "Mantén {quantity} hábitos saludables",
                    "Completa {quantity} actividades de bienestar",
                    "Dedica tiempo a {quantity} prácticas de salud"
            ),
            StatType.LUCK, List.of(
                    "Intenta {quantity} nuevas experiencias",
                    "Sal de tu zona de confort {quantity} veces",
                    "Explora {quantity} oportunidades nuevas"
            )
    );

    private static final List<String> STREAK_MISSION_TEMPLATES = List.of(
            "Mantén tu racha diaria - Día {streakDay}",
            "¡No rompas la cadena! - Día {streakDay}",
            "Consistencia es clave - Día {streakDay} consecutivo"
    );

    private static final List<String> ACHIEVEMENT_MISSION_TEMPLATES = List.of(
            "¡Supérate! Completa {quantity} tareas en un día",
            "Desafío de productividad: {quantity} tareas completadas",
            "Meta diaria: alcanzar {quantity} logros"
    );

    /**
     * Genera misión diaria personalizada para un usuario
     */
    public Mission generateDailyMission(User user) {
        log.info("Generating daily mission for user: {}", user.getUsername());

        // Verificar si ya tiene misión diaria hoy
        List<Mission> todaysMissions = missionService.getTodaysDailyMissions(user);
        if (!todaysMissions.isEmpty()) {
            log.info("User {} already has daily mission for today", user.getUsername());
            return todaysMissions.get(0);
        }

        // Analizar estadísticas débiles del usuario
        List<StatType> weakStats = missionService.analyzeUserWeakStats(user);
        StatType targetStat = selectRandomStat(weakStats);

        // Generar misión basada en estadística objetivo
        TaskDifficulty difficulty = missionService.suggestDifficultyForUser(user);
        String title = generateMissionTitle(targetStat, difficulty);
        String description = generateMissionDescription(targetStat, difficulty);
        int targetQuantity = calculateTargetQuantity(difficulty);
        long xpReward = missionService.calculateSuggestedXpReward(difficulty, MissionType.DAILY);

        Mission mission = Mission.builder()
                .title(title)
                .description(description)
                .missionType(MissionType.DAILY)
                .difficulty(difficulty)
                .targetQuantity(targetQuantity)
                .xpReward(xpReward)
                .user(user)
                .assignedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(1))
                .aiGenerated(true)
                .generationReason("Generada para mejorar " + targetStat.name().toLowerCase() + " (estadística débil)")
                .userLevelAtGeneration(user.getStats() != null ? user.getStats().getLevel() : 1)
                .priority(5)
                .statsCategories(Set.of(targetStat))
                .build();

        return missionService.createMission(mission);
    }

    /**
     * Genera misión de racha si el usuario tiene racha activa
     */
    public Mission generateStreakMission(User user) {
        int currentStreak = missionService.calculateDailyStreak(user);
        
        if (currentStreak < 3) {
            return null; // Solo generar misiones de racha después de 3 días
        }

        String title = STREAK_MISSION_TEMPLATES.get(new Random().nextInt(STREAK_MISSION_TEMPLATES.size()))
                .replace("{streakDay}", String.valueOf(currentStreak + 1));

        String description = String.format(
                "Has mantenido tu racha por %d días consecutivos. ¡Mantén el momentum completando al menos una tarea hoy!",
                currentStreak
        );

        long baseXp = missionService.calculateSuggestedXpReward(TaskDifficulty.EASY, MissionType.STREAK);
        long bonusXp = currentStreak * 10L; // Bonus por cada día de racha

        Mission mission = Mission.builder()
                .title(title)
                .description(description)
                .missionType(MissionType.STREAK)
                .difficulty(TaskDifficulty.EASY)
                .targetQuantity(1)
                .xpReward(baseXp)
                .bonusXpReward(bonusXp)
                .user(user)
                .assignedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(1))
                .aiGenerated(true)
                .generationReason("Misión de racha para mantener consistencia diaria")
                .userLevelAtGeneration(user.getStats() != null ? user.getStats().getLevel() : 1)
                .priority(8)
                .isStreakMission(true)
                .streakDay(currentStreak + 1)
                .build();

        return missionService.createMission(mission);
    }

    /**
     * Genera misión de logro para usuarios activos
     */
    public Mission generateAchievementMission(User user) {
        // Solo para usuarios nivel 10+
        if (user.getStats() == null || user.getStats().getLevel() == null || user.getStats().getLevel() < 10) {
            return null;
        }

        TaskDifficulty difficulty = missionService.suggestDifficultyForUser(user);
        int targetQuantity = calculateAchievementTarget(difficulty);
        
        String title = ACHIEVEMENT_MISSION_TEMPLATES.get(new Random().nextInt(ACHIEVEMENT_MISSION_TEMPLATES.size()))
                .replace("{quantity}", String.valueOf(targetQuantity));

        String description = String.format(
                "Desafío especial: Demuestra tu productividad completando %d tareas en un solo día. " +
                "¡Recompensa extra por este logro excepcional!",
                targetQuantity
        );

        long xpReward = missionService.calculateSuggestedXpReward(difficulty, MissionType.ACHIEVEMENT);

        Mission mission = Mission.builder()
                .title(title)
                .description(description)
                .missionType(MissionType.ACHIEVEMENT)
                .difficulty(difficulty)
                .targetQuantity(targetQuantity)
                .xpReward(xpReward)
                .user(user)
                .assignedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(1))
                .aiGenerated(true)
                .generationReason("Misión de logro para usuario avanzado (nivel " + user.getStats().getLevel() + ")")
                .userLevelAtGeneration(user.getStats().getLevel())
                .priority(7)
                .requiresValidation(targetQuantity >= 8) // Validación para metas muy altas
                .build();

        return missionService.createMission(mission);
    }

    /**
     * Genera misión de mejora de habilidad específica
     */
    public Mission generateSkillBoostMission(User user, StatType specificStat) {
        TaskDifficulty difficulty = missionService.suggestDifficultyForUser(user);
        String title = generateSkillBoostTitle(specificStat);
        String description = generateSkillBoostDescription(specificStat, difficulty);
        int targetQuantity = calculateTargetQuantity(difficulty);
        long xpReward = missionService.calculateSuggestedXpReward(difficulty, MissionType.SKILL_BOOST);

        Mission mission = Mission.builder()
                .title(title)
                .description(description)
                .missionType(MissionType.SKILL_BOOST)
                .difficulty(difficulty)
                .targetQuantity(targetQuantity)
                .xpReward(xpReward)
                .user(user)
                .assignedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(2)) // Más tiempo para skill boost
                .aiGenerated(true)
                .generationReason("Misión dirigida para mejorar " + specificStat.name().toLowerCase())
                .userLevelAtGeneration(user.getStats() != null ? user.getStats().getLevel() : 1)
                .priority(6)
                .statsCategories(Set.of(specificStat))
                .build();

        return missionService.createMission(mission);
    }

    /**
     * Genera múltiples misiones automáticamente para un usuario
     */
    public List<Mission> generateDailyMissionSet(User user) {
        List<Mission> missions = new ArrayList<>();

        // 1. Misión diaria principal
        Mission dailyMission = generateDailyMission(user);
        if (dailyMission != null) {
            missions.add(dailyMission);
        }

        // 2. Misión de racha (si aplica)
        Mission streakMission = generateStreakMission(user);
        if (streakMission != null) {
            missions.add(streakMission);
        }

        // 3. Posibilidad de misión de logro (20% chance para usuarios avanzados)
        if (new Random().nextInt(100) < 20) {
            Mission achievementMission = generateAchievementMission(user);
            if (achievementMission != null) {
                missions.add(achievementMission);
            }
        }

        log.info("Generated {} missions for user: {}", missions.size(), user.getUsername());
        return missions;
    }

    // ===============================
    // MÉTODOS PRIVADOS DE GENERACIÓN
    // ===============================

    private StatType selectRandomStat(List<StatType> stats) {
        if (stats.isEmpty()) {
            return StatType.values()[new Random().nextInt(StatType.values().length)];
        }
        return stats.get(new Random().nextInt(stats.size()));
    }

    private String generateMissionTitle(StatType statType, TaskDifficulty difficulty) {
        List<String> templates = MISSION_TEMPLATES.get(statType);
        String template = templates.get(new Random().nextInt(templates.size()));
        
        int quantity = calculateTargetQuantity(difficulty);
        return template.replace("{quantity}", String.valueOf(quantity));
    }

    private String generateMissionDescription(StatType statType, TaskDifficulty difficulty) {
        String statName = translateStatType(statType);
        String difficultyDesc = translateDifficulty(difficulty);
        
        return String.format(
                "Enfócate en mejorar tu %s con esta misión %s. " +
                "Completa las actividades requeridas para ganar experiencia y subir de nivel.",
                statName, difficultyDesc
        );
    }

    private String generateSkillBoostTitle(StatType statType) {
        String statName = translateStatType(statType);
        return String.format("Impulso de %s", statName);
    }

    private String generateSkillBoostDescription(StatType statType, TaskDifficulty difficulty) {
        String statName = translateStatType(statType);
        return String.format(
                "Misión especializada para fortalecer tu %s. " +
                "Basándome en tu perfil, esta área necesita atención especial. " +
                "¡Dedica tiempo extra a desarrollar esta habilidad!",
                statName
        );
    }

    private int calculateTargetQuantity(TaskDifficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 1 + new Random().nextInt(2);      // 1-2
            case MEDIUM -> 2 + new Random().nextInt(2);    // 2-3
            case HARD -> 3 + new Random().nextInt(3);      // 3-5
            case EXTREME -> 5 + new Random().nextInt(4);   // 5-8
        };
    }

    private int calculateAchievementTarget(TaskDifficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 4 + new Random().nextInt(2);      // 4-5
            case MEDIUM -> 6 + new Random().nextInt(2);    // 6-7
            case HARD -> 8 + new Random().nextInt(3);      // 8-10
            case EXTREME -> 10 + new Random().nextInt(6);  // 10-15
        };
    }

    private String translateStatType(StatType statType) {
        return switch (statType) {
            case STRENGTH -> "fuerza";
            case INTELLIGENCE -> "inteligencia";
            case WISDOM -> "sabiduría";
            case CHARISMA -> "carisma";
            case DEXTERITY -> "destreza";
            case CONSTITUTION -> "constitución";
            case LUCK -> "suerte";
        };
    }

    private String translateDifficulty(TaskDifficulty difficulty) {
        return switch (difficulty) {
            case EASY -> "fácil";
            case MEDIUM -> "moderada";
            case HARD -> "desafiante";
            case EXTREME -> "extrema";
        };
    }
}