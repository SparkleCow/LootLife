import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { WindowComponent } from '../../../shared/window/window.component';
import { AuthService } from '../../../core/services/auth.service';
import { LoginRequest } from '../../../models/login-request-dto.model';
import { AuthResponse } from '../../../models/auth-response.model';

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
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {

    if (this.loginForm.valid) {

      const loginRequest: LoginRequest = {
        username: this.loginForm.value.username,
        password: this.loginForm.value.password
      };

      this.authService.$loginUser(loginRequest).subscribe({
        next: (response: AuthResponse) => {
          this.authService.saveToken(response);
          console.log(loginRequest);
          alert('¡Inicio de sesión exitoso!');
          this.router.navigate(['']);
        },
        error: (error: any) => {
          console.error('Error durante el inicio de sesión:', error.message);
          alert('Error al iniciar sesión: ' + error.message);
        }
      });

    } else {
      console.log('Formulario de login inválido');
      this.loginForm.markAllAsTouched();
      alert('Por favor, ingresa un usuario/email y contraseña válidos.');
    }
  }
}
