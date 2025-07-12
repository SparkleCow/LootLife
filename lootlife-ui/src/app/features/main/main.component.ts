import { Component } from '@angular/core';
import { WindowComponent } from "../../shared/window/window.component";

@Component({
  selector: 'app-main',
  imports: [WindowComponent],
  templateUrl: './main.component.html',
  styleUrl: './main.component.css'
})
export class MainComponent {
  //TODO implement a conection with an API that allows us get gifs with a trigger every 2 hours
}
