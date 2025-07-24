import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-validation',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './validation.component.html',
  styleUrl: './validation.component.css'
})
export class ValidationComponent {

  verifyForm: FormGroup;
  message: string = '';
  isSuccess?: boolean;

  constructor(private fb: FormBuilder, private _authService: AuthService, private router:Router) {
    this.verifyForm = this.fb.group({
      token: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.verifyForm.invalid) {
      this.verifyForm.markAllAsTouched();
      alert("Ingresa un token valido");
      return;
    }

    this._authService.$validateToken(this.verifyForm.value).subscribe({
      next: () => {
        this.isSuccess = true;
        this.message = '✅ Cuenta verificada con éxito!';
        setTimeout(()=> {
          this.redirectAtLogin();
        }, 1000);
      },
      error: (err) => {
        this.isSuccess = false;
        this.message = '❌ Error: ' + (err.error?.message || 'No se pudo verificar el token');
      }
    });
  }

  redirectAtLogin(){
    this.router.navigate(['/auth/login']);
  }

}
