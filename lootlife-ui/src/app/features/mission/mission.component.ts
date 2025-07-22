import { Component, OnInit } from '@angular/core';
import { MissionService } from '../../core/services/mission.service';
import { MissionResponseDto, MissionStatus } from '../../models/mission-response-dto.model';
import { TaskDifficulty } from '../../models/task-request-dto.model';
import { StatType } from '../../models/stats-response-dto.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-mission',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mission.component.html',
  styleUrl: './mission.component.css'
})
export class MissionComponent implements OnInit{

    username = 'Jonathan';

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

    constructor(private _missionService:MissionService){}


    ngOnInit(): void {
      this._missionService.$getActiveMissions().subscribe({
        next: (missions:MissionResponseDto[])=>{
          this.mission = missions[0]; //This uses an array because this app will require more missions per user in the future.
        }
      });
    }

    acceptMission() {
      console.log('Aceptada');
    }

    cancelMission() {
      console.log('Cancelada');
    }

    completeMission() {
      console.log('Completada');
    }
}
