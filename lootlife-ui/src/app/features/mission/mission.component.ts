import { Component, OnInit } from '@angular/core';
import { MissionService } from '../../core/services/mission.service';
import { MissionResponseDto, MissionStatus } from '../../models/mission-response-dto.model';
import { TaskDifficulty } from '../../models/task-request-dto.model';
import { StatType } from '../../models/stats-response-dto.model';
import { CommonModule } from '@angular/common';
import { WindowComponent } from "../../shared/window/window.component";

@Component({
  selector: 'app-mission',
  standalone: true,
  imports: [CommonModule, WindowComponent, WindowComponent],
  templateUrl: './mission.component.html',
  styleUrl: './mission.component.css'
})
export class MissionComponent implements OnInit{

    username = 'Jonathan';
    MissionStatus = MissionStatus;

    mission: MissionResponseDto = {
      id: 1,
      title: 'Caminar 5km',
      description: 'Sal a caminar por el parque y completa 5 km.',
      status: MissionStatus.PENDING,
      difficulty: TaskDifficulty.MEDIUM,
      assignedAt: new Date().toISOString(),
      expiresAt: new Date(new Date().setDate(new Date().getDate() + 2)).toISOString(),
      startedAt: null,
      completedAt: null,
      targetQuantity: 5,
      currentProgress: 2,
      xpReward: 200,
      bonusXpReward: 50,
      statsCategories: [StatType.CONSTITUTION],
      priority: 3,
      isStreakMission: true,
      streakDay: 2,
      requiresValidation: false,
      validationNotes: ''
    };

    missions?: MissionResponseDto[];

    constructor(private _missionService: MissionService) { }

    ngOnInit(): void {
      this._missionService.$getActiveMissions().subscribe({
        next: (missions: MissionResponseDto[]) => {
          this.missions = missions;
          if (missions.length > 0) {
            this.mission = missions[0];
          }
        },
        error: (err) => {
          console.error('Error al obtener misiones activas:', err);
        }
      });
    }

    acceptMission(): void {
      this._missionService.$activateMission(this.mission.id).subscribe({
        next: (updatedMission: MissionResponseDto) => {
          this.mission = updatedMission;
        },
        error: (err) => {
          console.error('Error al activar la misión:', err);
        }
      });
    }

    cancelMission(): void {
      this._missionService.$cancelMission(this.mission.id).subscribe({
        next: () => {
          this.mission.status = MissionStatus.CANCELLED;
          console.log('Misión cancelada');
        },
        error: (err) => {
          console.error('Error al cancelar la misión:', err);
        }
      });
    }

    completeMission(): void {
      this._missionService.$completeMission(this.mission.id).subscribe({
        next: (updatedMission) => {
          this.mission = updatedMission;
          console.log('Misión completada');
        },
        error: (err) => {
          console.error('Error al completar la misión:', err);
        }
      });
    }
}
