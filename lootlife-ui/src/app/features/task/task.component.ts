import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TaskRequestDto } from '../../models/task-request-dto.model';
import { TaskService } from '../../core/services/task.service';
import { TaskResponseDto } from '../../models/task-response-dto.model';

@Component({
  selector: 'app-task',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './task.component.html',
  styleUrl: './task.component.css'
})
export class TaskComponent implements OnInit{

  taskForm: FormGroup;
  selectedCategories: string[] = [];
  userTasks: TaskResponseDto[];

  statsOptions = [
    { label: 'Fuerza', value: 'STRENGTH' },
    { label: 'Agilidad', value: 'AGILITY' },
    { label: 'Inteligencia', value: 'INTELLIGENCE' },
    { label: 'Constitución', value: 'CONSTITUTION' },
    { label: 'Suerte', value: 'LUCK' },
    { label: 'Sabiduría', value: 'WISDOM'},
    { label: 'Carisma', value: 'CHARISMA'},
    { label: 'Destreza', value: 'DEXTERITY'},
  ];

  constructor(private fb: FormBuilder, private _taskService: TaskService){
    this.taskForm = this.fb.group({
      title: ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.maxLength(255)]],
      taskDifficulty: [null, Validators.required],
      statsCategories: [[], [Validators.required, Validators.maxLength(3)]],
      deadline: [null, Validators.required],
      progressRequired: [null, [Validators.required, Validators.min(1)]]
    });
  }

  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }

  onCheckboxChange(event: Event) {
    const checkbox = event.target as HTMLInputElement;
    const value = checkbox.value;

    if (checkbox.checked) {
      if (this.selectedCategories.length < 3) {
        this.selectedCategories.push(value);
      } else {
        checkbox.checked = false;
      }
    } else {
      this.selectedCategories = this.selectedCategories.filter(
        (v) => v !== value
      );
    }

    this.taskForm.get('statsCategories')?.setValue(this.selectedCategories);
  }

  onSubmit() {
    if (this.taskForm.invalid) {
      this.taskForm.markAllAsTouched();
      return;
    }

    const dto: TaskRequestDto = {
      ...this.taskForm.value,
      deadline: this.convertToISOString(this.taskForm.value.deadline)
    };

    console.log('DTO listo para backend:', dto);
    this._taskService.createTask(dto);
  }

  convertToISOString(dateString: string | null): string | null {
    if (!dateString) return null;
    return new Date(dateString).toISOString();
  }
}
