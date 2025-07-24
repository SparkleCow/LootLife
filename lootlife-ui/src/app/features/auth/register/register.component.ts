import { Component } from '@angular/core';
import { WindowComponent } from "../../../shared/window/window.component";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { UserRequest } from '../../../models/user-request-dto.model';
import { AuthResponse } from '../../../models/auth-response.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    WindowComponent,
    ReactiveFormsModule,
    RouterLink,
],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  registerForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      age: [null, [Validators.required, Validators.min(1), Validators.max(150)]],
      birthDate: ['', [Validators.required]]
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {

      const formValue = this.registerForm.value;

      const userRequest: UserRequest = {
        username: formValue.username,
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        email: formValue.email,
        password: formValue.password,
        age: formValue.age,
        birthDate: formValue.birthDate
    };

      console.log('Objeto UserRequest a enviar:', userRequest);

      this.authService.$registerUser(userRequest).subscribe({
        next: (response: AuthResponse) => {
          console.log('Registro exitoso:', response);
          alert('¡Registro exitoso! Ingresa el código enviado a tu correo.');
          this.router.navigate(['/auth/validation']);
        },
        error: (error: any) => {
          console.error('Error durante el registro:', error.message);
          alert('Error en el registro: ' + error.message);
        }
      });

    } else {
      console.log('Formulario de registro inválido.');
      this.registerForm.markAllAsTouched();
      alert('Por favor, completa todos los campos correctamente.');
    }
  }

  registerGithub() {
    window.open(
      'http://localhost:8080/oauth2/authorization/github',
      'oauthLogin',
      'width=600,height=700'
    );
  }
}
