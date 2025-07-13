import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  constructor(private router:Router){}

  redirectAtLogin(){
    this.router.navigate(['/auth/login']);
  }

  redirectAtRegister(){
    this.router.navigate(['/auth/register']);
  }

  redirectAtProfile(){
    this.router.navigate(['/profile']);
  }

  redirectAtTask(){
    this.router.navigate(['/task']);
  }
}
