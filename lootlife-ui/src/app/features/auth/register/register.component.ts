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
    RouterLink
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

      // Llama al servicio de autenticación para registrar el usuario
      this.authService.$registerUser(userRequest).subscribe({
        next: (response: AuthResponse) => {
          // Si el registro es exitoso
          console.log('Registro exitoso:', response);
          //this.authService.$saveToken(response.token); // Guarda el token si la respuesta lo incluye
          alert('¡Registro exitoso! Ahora puedes iniciar sesión.');
          this.router.navigate(['/auth/login']); // Redirige al login
        },
        error: (error: any) => {
          // Si ocurre un error durante el registro
          console.error('Error durante el registro:', error.message);
          // Muestra un mensaje de error al usuario
          alert('Error en el registro: ' + error.message);
        }
        // complete: () => {
        //   console.log('Petición de registro completada.');
        // }
      });

    } else {
      // El formulario es inválido, muestra un mensaje o resalta los errores
      console.log('Formulario de registro inválido.');
      this.registerForm.markAllAsTouched(); // Para mostrar los mensajes de error en los campos
      alert('Por favor, completa todos los campos correctamente.');
    }
  }
}
