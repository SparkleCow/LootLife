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

## Métodos Helper

- **isExpired()**: Verifica si la misión ha expirado
- **isCompleted()**: Verifica si la misión está completada
- **isActive()**: Verifica si la misión está activa
- **getProgressPercentage()**: Calcula el porcentaje de progreso
- **canBeCompleted()**: Verifica si la misión puede ser completada

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

### 1. Misión Diaria Simple
```java
Mission dailyMission = Mission.builder()
    .title("Completa 3 tareas de fuerza")
    .description("Completa cualquier 3 tareas relacionadas con fuerza física")
    .missionType(MissionType.DAILY)
    .difficulty(TaskDifficulty.MEDIUM)
    .targetQuantity(3)
    .xpReward(150L)
    .user(user)
    .assignedAt(LocalDateTime.now())
    .expiresAt(LocalDateTime.now().plusDays(1))
    .build();
```

### 2. Misión de Racha
```java
Mission streakMission = Mission.builder()
    .title("Mantén tu racha diaria - Día 7")
    .description("Completa al menos una tarea hoy para mantener tu racha de 7 días")
    .missionType(MissionType.STREAK)
    .isStreakMission(true)
    .streakDay(7)
    .bonusXpReward(300L)
    .priority(9)
    .build();
```

### 3. Misión de Mejora de Habilidad
```java
Mission skillBoostMission = Mission.builder()
    .title("Enfoque en Inteligencia")
    .description("Basado en tus estadísticas, necesitas mejorar tu inteligencia. Completa 2 tareas de aprendizaje.")
    .missionType(MissionType.SKILL_BOOST)
    .generationReason("Usuario tiene inteligencia baja comparada con otras stats")
    .statsCategories(Set.of(StatType.INTELLIGENCE))
    .targetQuantity(2)
    .build();
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

1. Implementar servicio de generación de misiones por IA
2. Crear controlador REST para gestión de misiones
3. Desarrollar sistema de notificaciones
4. Implementar métricas y analytics de misiones
5. Crear UI para mostrar misiones al usuario