import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { WindowComponent } from '../../../shared/window/window.component';
import { AuthService } from '../../../core/services/auth.service';
import { LoginRequest } from '../../../models/login-request-dto.model';
import { AuthResponse } from '../../../models/auth-response.model';
import { MissionService } from '../../../core/services/mission.service';
import { UserService } from '../../../core/services/user.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    WindowComponent
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private _authService: AuthService,
    private router: Router,
    private _missionService: MissionService,
    private _userService: UserService
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(25)]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(20)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const loginRequest: LoginRequest = {
        username: this.loginForm.value.username,
        password: this.loginForm.value.password
      };

      this._authService.$loginUser(loginRequest).subscribe({
        next: (response: AuthResponse) => {
          this._authService.saveToken(response);
          this._userService.fetchUserInformation();
          this._missionService.$createMission().subscribe({
            next: () => {
            },
            error: (err) => {
              console.error('Error, mission could not be created: ', err);
              // Try again
              this._missionService.$createMission().subscribe({
                next: () => console.log(',Mission created'),
                error: (err) => console.error('Eror, mission could not be created after try again: ', err)
              });
            }
          });
          setTimeout(() => {
            this.router.navigate(['']);
          }, 400);
        },
        error: (error: any) => {
          console.error('Loggin error: ', error.message);
          alert('Loggin error: ' + error.message);
        }
      });

    } else {
      console.log('Invalid forms');
      this.loginForm.markAllAsTouched();
      alert('Por favor, ingresa un usuario/email y contraseña válidos.');
    }
  }
}
