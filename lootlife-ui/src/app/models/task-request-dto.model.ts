import { StatType } from "./stats-response-dto.model";

export interface TaskRequestDto {
  title: string;
  description: string;
  taskDifficulty: TaskDifficulty;
  statsCategories: StatType[];
  deadline: string;
  progressRequired: number;
}

export enum TaskDifficulty {
  EASY = 'EASY',
  MEDIUM = 'MEDIUM',
  HARD = 'HARD'
}
