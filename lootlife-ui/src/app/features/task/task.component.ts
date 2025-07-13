import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'app-task',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './task.component.html',
  styleUrl: './task.component.css'
})
export class TaskComponent {

   statsOptions = [
    { label: 'Fuerza', value: 'STRENGTH' },
    { label: 'Agilidad', value: 'AGILITY' },
    { label: 'Inteligencia', value: 'INTELLIGENCE' },
    { label: 'Constitución', value: 'CONSTITUTION' },
    { label: 'Suerte', value: 'LUCK' },
    { label: 'Sabiduria', value: 'WISDOM'},
    { label: 'Carisma', value: 'CHARISMA'},
    { label: 'Destreza', value: 'DEXTERITY'},
  ];

  selectedCategories: string[] = [];

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
      this.selectedCategories = this.selectedCategories.filter(v => v !== value);
    }
  }
}
