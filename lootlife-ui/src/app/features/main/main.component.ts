import { Component } from '@angular/core';
import { WindowComponent } from "../../shared/window/window.component";

@Component({
  selector: 'app-main',
  imports: [WindowComponent],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent {
}
