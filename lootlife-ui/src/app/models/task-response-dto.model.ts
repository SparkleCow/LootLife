import { StatType } from "./stats-response-dto.model";
import { TaskDifficulty } from "./task-request-dto.model";

export interface TaskResponseDto {
  id: number;
  deadline: string;
  isActive: boolean;
  isExpired: boolean;
  statsCategories: StatType[];
  title: string;
  description: string;
  xpReward: number;
  taskDifficulty: TaskDifficulty;
  progressRequired: number;
  currentProgress: number;
}
