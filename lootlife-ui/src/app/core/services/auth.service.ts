import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserRequest } from '../../models/user-request-dto.model';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthResponse } from '../../models/auth-response.model';
import { LoginRequest } from '../../models/login-request-dto.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl: string = 'http://localhost:8080/auth'
  private AUTH_TOKEN_KEY = 'authToken';

  constructor(private http:HttpClient) {}


  /**
  *Generic http handler
  * @param error  HttpErrorResponse
  * @returns Observer
  */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocurrió un error desconocido.';
    if (error.error instanceof ErrorEvent) {
      // Error del lado del cliente o de red
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // El backend retornó un código de respuesta de error (ej. 400, 404, 500)
      if (error.status === 400 && error.error && typeof error.error === 'object') {
        // Si el backend envía un objeto de errores de validación
        errorMessage = 'Errores de validación:';
        for (const key in error.error) {
          if (Object.prototype.hasOwnProperty.call(error.error, key)) {
            errorMessage += `\n- ${key}: ${error.error[key]}`;
          }
        }
      } else if (error.error && typeof error.error === 'string') {
        // Si el backend envía un mensaje de error en texto plano
        errorMessage = `Error del servidor (${error.status}): ${error.error}`;
      } else {
        // Otra estructura de error del backend
        errorMessage = `El servidor retornó un código ${error.status}: ${error.message}`;
      }
    }
    console.error(errorMessage);
    // Retorna un observable con un mensaje de error que el componente puede manejar
    return throwError(() => new Error(errorMessage));
  }


  /**
  * It sends a register request at backend
  * @param userRequest
  * @returns authResponse.
  */
  $registerUser(userRequest: UserRequest): Observable<AuthResponse> {
    const url = `${this.baseUrl}/register`;
    return this.http.post<AuthResponse>(url, userRequest)
      .pipe(
        catchError(this.handleError)
      );
  }

  $loginUser(userLoginRequest:LoginRequest): Observable<AuthResponse>{
    const url = `${this.baseUrl}/login`;
    return this.http.post<AuthResponse>(url, userLoginRequest)
      .pipe(
        catchError(this.handleError)
      );
  }

  $validateToken(){}

  saveToken(authResponse: AuthResponse){
    const token = authResponse.token;
    localStorage.setItem("AUTH_TOKEN_KEY", token);
  }

}
