export interface StatsResponseDto {
  id: number;

  level: number;
  experiencePoints: number;
  nextLevelAt: number;

  strengthLevel: number;
  strengthExperience: number;
  strengthNextLevelAt: number;

  intelligenceLevel: number;
  intelligenceExperience: number;
  intelligenceNextLevelAt: number;

  wisdomLevel: number;
  wisdomExperience: number;
  wisdomNextLevelAt: number;

  charismaLevel: number;
  charismaExperience: number;
  charismaNextLevelAt: number;

  dexterityLevel: number;
  dexterityExperience: number;
  dexterityNextLevelAt: number;

  constitutionLevel: number;
  constitutionExperience: number;
  constitutionNextLevelAt: number;

  luckLevel: number;
  luckExperience: number;
  luckNextLevelAt: number;

  totalMissionsCompleted: number;
}
