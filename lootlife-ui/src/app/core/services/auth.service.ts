import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserRequest } from '../../models/user-request-dto.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl: string = 'http://localhost:8080/auth'

  constructor(private http:HttpClient) {}

  /**
   * Envía una petición de registro de usuario al backend.
   *
   * It sends a register request at backend
   * @param userRequest

   * @returns.
   */
  $registerUser(userRequest: UserRequest): Observable<AuthResponse> {
    const url = `${this.baseUrl}/register`; // Endpoint específico de registro
    return this.http.post<AuthResponse>(url, userRequest)
      .pipe(
        catchError(this.handleError) // Captura y maneja cualquier error HTTP
      );
  }

  $loginUser(){}

  $validateToken(){}
}
