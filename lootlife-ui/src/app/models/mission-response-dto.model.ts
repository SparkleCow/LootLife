import { StatType } from "./stats-response-dto.model";
import { TaskDifficulty } from "./task-request-dto.model";

export interface MissionResponseDto {
  id: number;
  title: string;
  description: string;
  status: MissionStatus;
  difficulty: TaskDifficulty;
  assignedAt: string;
  expiresAt: string;
  startedAt: string | null;
  completedAt: string | null;
  targetQuantity: number;
  currentProgress: number;
  xpReward: number;
  bonusXpReward: number;
  statsCategories: StatType[];
  priority: number;
  isStreakMission: boolean;
  streakDay: number;
  requiresValidation: boolean;
  validationNotes: string | null;
}


export enum MissionStatus {
  PENDING = 'PENDING',
  ACTIVE = 'ACTIVE',
  COMPLETED = 'COMPLETED',
  EXPIRED = 'EXPIRED',
  CANCELLED = 'CANCELLED'
}
