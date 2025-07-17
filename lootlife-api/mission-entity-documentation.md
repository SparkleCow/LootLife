# Mission Entity Documentation

## Overview
La entidad `Mission` está diseñada para manejar misiones diarias generadas por IA basadas en las estadísticas y comportamiento del usuario. Proporciona un sistema completo de misiones gamificadas que se adapta al progreso individual de cada usuario.

## Estructura de la Entidad

### Atributos Básicos
- **id**: Identificador único de la misión
- **title**: Título descriptivo de la misión
- **description**: Descripción detallada de lo que debe hacer el usuario
- **createdAt/lastModifiedAt**: Timestamps de auditoría

### Clasificación de Misiones
- **missionType**: Tipo de misión (DAILY, WEEKLY, SPECIAL, ACHIEVEMENT, STREAK, SKILL_BOOST)
- **status**: Estado actual (PENDING, ACTIVE, COMPLETED, FAILED, EXPIRED, CANCELLED)
- **difficulty**: Dificultad basada en TaskDifficulty (EASY, MEDIUM, HARD, EXTREME)

### Gestión de Tiempo
- **assignedAt**: Cuándo se asignó la misión al usuario
- **expiresAt**: Fecha límite para completar la misión
- **startedAt**: Cuándo el usuario comenzó la misión
- **completedAt**: Cuándo se completó la misión

### Seguimiento de Progreso
- **targetQuantity**: Cantidad objetivo para misiones cuantitativas
- **currentProgress**: Progreso actual del usuario
- **getProgressPercentage()**: Método helper para calcular porcentaje

### Sistema de Recompensas
- **xpReward**: Experiencia base otorgada al completar
- **bonusXpReward**: XP adicional por completar temprano o mantener rachas
- **statsCategories**: Categorías de estadísticas que mejora la misión

### Metadatos de IA
- **aiGenerated**: Flag indicando que fue generada por IA
- **generationReason**: Razón por la cual la IA generó esta misión
- **userLevelAtGeneration**: Nivel del usuario cuando se generó

### Sistema de Prioridades y Rachas
- **priority**: Prioridad de 1-10 (10 = más alta)
- **isStreakMission**: Si es parte de una racha
- **streakDay**: Día en la racha actual

### Validación
- **requiresValidation**: Si requiere validación manual
- **validationNotes**: Notas adicionales para validación

### Relaciones
- **user**: Usuario asignado (ManyToOne)
- **relatedTasks**: Tareas relacionadas (ManyToMany)

## Enums Creados

### MissionType
```java
DAILY,          // Misiones diarias recurrentes
WEEKLY,         // Misiones semanales
SPECIAL,        // Misiones de eventos especiales
ACHIEVEMENT,    // Misiones basadas en logros
STREAK,         // Misiones de mantenimiento de rachas
SKILL_BOOST     // Misiones enfocadas en mejora de habilidades específicas
```

### MissionStatus
```java
PENDING,        // Misión asignada pero no iniciada
ACTIVE,         // Misión en progreso
COMPLETED,      // Misión completada exitosamente
FAILED,         // Misión fallida (expirada o no completada)
EXPIRED,        // Misión expirada sin completar
CANCELLED       // Misión cancelada por sistema o usuario
```

## Servicios Creados

### MissionService
Servicio principal que maneja toda la lógica de negocio de misiones:

#### Métodos Utility (ex-helper methods):
- **isExpired(Mission)**: Verifica si la misión ha expirado
- **isCompleted(Mission)**: Verifica si la misión está completada
- **isActive(Mission)**: Verifica si la misión está activa
- **getProgressPercentage(Mission)**: Calcula el porcentaje de progreso
- **canBeCompleted(Mission)**: Verifica si la misión puede ser completada

#### CRUD Operations:
- **createMission()**: Crea nueva misión con validaciones
- **getActiveMissions()**: Obtiene misiones activas del usuario
- **getTodaysDailyMissions()**: Misiones diarias de hoy
- **getUserMissions()**: Todas las misiones del usuario

#### Progress & Completion:
- **updateMissionProgress()**: Actualiza progreso con auto-completado
- **completeMission()**: Completa misión manualmente
- **startMission()**: Inicia misión pendiente

#### Analytics & Stats:
- **getUserMissionStats()**: Estadísticas completas del usuario
- **calculateDailyStreak()**: Calcula racha diaria actual
- **getMissionsExpiringSoon()**: Para notificaciones

#### AI Support:
- **analyzeUserWeakStats()**: Analiza stats débiles para IA
- **suggestDifficultyForUser()**: Sugiere dificultad apropiada
- **calculateSuggestedXpReward()**: Calcula XP basado en tipo/dificultad

#### Scheduled Tasks:
- **expireOverdueMissions()**: Job automático cada 5 minutos

#### Validation:
- **getMissionsRequiringValidation()**: Misiones pendientes de validar
- **validateMission()**: Valida misión manualmente

#### Bulk Operations:
- **cancelAllUserMissions()**: Cancela todas las misiones activas
- **getRecentMissionHistoryForAI()**: Historial para análisis de IA

### MissionGenerationService
Servicio especializado para generación inteligente de misiones:

#### Generación por Tipo:
- **generateDailyMission()**: Misión diaria personalizada
- **generateStreakMission()**: Misión de racha (después de 3 días)
- **generateAchievementMission()**: Misión de logro (usuarios nivel 10+)
- **generateSkillBoostMission()**: Misión de mejora específica
- **generateDailyMissionSet()**: Set completo de misiones diarias

#### Features Inteligentes:
- Templates personalizados por StatType
- Análisis de estadísticas débiles
- Dificultad adaptativa por nivel de usuario
- Sistema de bonificaciones por rachas
- Validación automática para metas altas
- Generación probabilística de misiones especiales

## Repository Personalizado

El `MissionRepository` incluye consultas especializadas para:
- Encontrar misiones diarias de hoy
- Identificar misiones expiradas
- Buscar misiones que expiran pronto
- Contar misiones completadas en rangos de fecha
- Gestionar rachas de misiones
- Encontrar misiones que requieren validación
- Obtener historial para análisis de IA

## Casos de Uso

### 1. Generar Misiones Automáticamente con IA
```java
@Autowired
private MissionGenerationService missionGenerationService;

// Genera set completo de misiones diarias para un usuario
List<Mission> dailyMissions = missionGenerationService.generateDailyMissionSet(user);

// Genera solo misión diaria principal
Mission dailyMission = missionGenerationService.generateDailyMission(user);

// Genera misión de mejora específica
Mission skillMission = missionGenerationService.generateSkillBoostMission(user, StatType.INTELLIGENCE);
```

### 2. Gestión de Progreso de Misiones
```java
@Autowired
private MissionService missionService;

// Actualizar progreso de misión
Mission updatedMission = missionService.updateMissionProgress(missionId, 1);

// Completar misión manualmente
Mission completedMission = missionService.completeMission(mission);

// Verificar si puede ser completada
boolean canComplete = missionService.canBeCompleted(mission);

// Obtener porcentaje de progreso
double progress = missionService.getProgressPercentage(mission);
```

### 3. Analytics y Estadísticas
```java
// Obtener estadísticas completas del usuario
Map<String, Object> stats = missionService.getUserMissionStats(user);
// Contiene: totalCompleted, weeklyCompleted, currentStreak, activeMissions, averageProgress

// Calcular racha diaria
int streak = missionService.calculateDailyStreak(user);

// Obtener misiones que expiran pronto (para notificaciones)
List<Mission> expiringSoon = missionService.getMissionsExpiringSoon(user, 2); // próximas 2 horas
```

### 4. Análisis para IA
```java
// Analizar estadísticas débiles del usuario
List<StatType> weakStats = missionService.analyzeUserWeakStats(user);

// Sugerir dificultad apropiada
TaskDifficulty suggestedDifficulty = missionService.suggestDifficultyForUser(user);

// Calcular XP sugerido
long xpReward = missionService.calculateSuggestedXpReward(TaskDifficulty.MEDIUM, MissionType.DAILY);

// Obtener historial para análisis de IA
List<Mission> recentHistory = missionService.getRecentMissionHistoryForAI(user, 30); // últimos 30 días
```

### 5. Validación y Administración
```java
// Obtener misiones que requieren validación manual
List<Mission> pendingValidation = missionService.getMissionsRequiringValidation();

// Validar misión
Mission validatedMission = missionService.validateMission(missionId, true, "Aprobada por moderador");

// Cancelar todas las misiones de un usuario
missionService.cancelAllUserMissions(user, "Usuario suspendido temporalmente");
```

## Integración con IA

La entidad está diseñada para soportar generación inteligente de misiones:

1. **Análisis de estadísticas**: La IA puede analizar las stats del usuario para generar misiones que mejoren áreas débiles
2. **Historial de comportamiento**: Usar misiones pasadas para personalizar nuevas misiones
3. **Dificultad adaptativa**: Ajustar dificultad basada en el nivel y desempeño del usuario
4. **Rachas inteligentes**: Mantener engagement con misiones de racha personalizadas

## Consideraciones Técnicas

- **Índices recomendados**: user_id, status, missionType, assignedAt
- **Cleanup automático**: Implementar job para limpiar misiones expiradas antiguas
- **Notificaciones**: Usar findMissionsExpiringSoon() para notificar usuarios
- **Validación**: Algunas misiones pueden requerir validación manual por moderadores

## Próximos Pasos

1. ✅ **Implementar servicios de misiones** - MissionService y MissionGenerationService creados
2. **Crear controlador REST para gestión de misiones** - Endpoints para CRUD y gestión
3. **Desarrollar sistema de notificaciones** - Usar getMissionsExpiringSoon() 
4. **Configurar jobs programados** - Habilitar @EnableScheduling para expireOverdueMissions()
5. **Implementar métricas y analytics avanzados** - Dashboard de admin
6. **Crear UI para mostrar misiones al usuario** - Frontend con progreso visual
7. **Integrar con sistema de tareas existente** - Conectar completion de tasks con missions
8. **Implementar sistema de recompensas** - Aplicar XP rewards al completar misiones
9. **Crear sistema de notificaciones push/email** - Alertas de misiones que expiran
10. **Desarrollar algoritmos de IA más avanzados** - Machine learning para personalización

## Archivos Creados
- ✅ `Mission.java` - Entidad principal
- ✅ `MissionType.java` - Enum de tipos de misión
- ✅ `MissionStatus.java` - Enum de estados
- ✅ `MissionRepository.java` - Repository con consultas especializadas
- ✅ `MissionService.java` - Servicio principal con toda la lógica
- ✅ `MissionGenerationService.java` - Servicio de generación inteligente
- ✅ Actualizada `User.java` - Relación con misiones
- ✅ `mission-entity-documentation.md` - Documentación completa