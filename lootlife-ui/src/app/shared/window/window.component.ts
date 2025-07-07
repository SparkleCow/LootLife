import { CommonModule } from '@angular/common';
import { Component, HostListener, Input, input, OnInit } from '@angular/core';

@Component({
  selector: 'app-window',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './window.component.html',
  styleUrl: './window.component.css',
  host: {
    '[style.position]': "'absolute'",
    '[style.left.px]': 'posX',
    '[style.top.px]': 'posY'
  }})

export class WindowComponent implements OnInit {
  imageRoute = input<string>('');
  @Input() startLeft = 0;
  @Input() startTop = 0;

  posX = 0;
  posY = 0;

  dragging = false;
  offsetX = 0;
  offsetY = 0;

  ngOnInit(): void {
    this.posX = this.startLeft;
    this.posY = this.startTop;
  }

  onMouseDown(event: MouseEvent) {
    this.dragging = true;
    this.offsetX = event.clientX - this.posX;
    this.offsetY = event.clientY - this.posY;
    event.preventDefault();
  }

  @HostListener('document:mousemove', ['$event'])
  onMouseMove(event: MouseEvent) {
    if (!this.dragging) return;

    this.posX = event.clientX - this.offsetX;
    this.posY = event.clientY - this.offsetY;
  }

  @HostListener('document:mouseup')
  onMouseUp() {
    this.dragging = false;
  }
}

