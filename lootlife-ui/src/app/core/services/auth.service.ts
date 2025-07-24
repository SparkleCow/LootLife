import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserRequest } from '../../models/user-request-dto.model';
import { catchError, Observable, throwError, } from 'rxjs';
import { AuthResponse } from '../../models/auth-response.model';
import { LoginRequest } from '../../models/login-request-dto.model';
import { TokenValidationRequest } from '../../models/token-validation-request.model';
import { CustomHttpError } from '../../models/custom-error-response.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl: string = 'http://localhost:8080/auth'
  private AUTH_TOKEN_KEY = 'authToken';

  constructor(private http:HttpClient) {}

  /**
 * Generic HTTP error handler.
 * @param error HttpErrorResponse
 * @returns Observable that throws an error with a formatted message.
 */
  private handleError(error: HttpErrorResponse): Observable<never> {
    const customError: CustomHttpError = {
      status: error.status,
      title: 'Error inesperado',
      userMessage: 'Ocurrió un error inesperado.',
    };

    if (error.error instanceof ErrorEvent) {
      // Client-side or network error
      customError.title = 'Error del cliente';
      customError.userMessage = error.error.message;
      customError.rawMessage = error.error.message;
    } else {
      // Server-side error
      const err = error.error;

      if (typeof err === 'object' && err !== null) {
        customError.title = `Error ${error.status}`;

        if (err.businessErrorDescription) {
          customError.userMessage = err.businessErrorDescription;
        }

        if (err.message) {
          customError.rawMessage = err.message;

          // Traduce algunos mensajes comunes
          if (err.message.toLowerCase().includes('bad credentials')) {
            customError.userMessage = 'Usuario o contraseña incorrectos.';
          } else {
            customError.userMessage ||= err.message;
          }
        }

        if (err.businessErrorCode) {
          customError.details = { ...(customError.details || {}), code: err.businessErrorCode };
        }

        if (Array.isArray(err.validationErrors)) {
          customError.validationErrors = err.validationErrors;
        }

        if (err.errorDetails && typeof err.errorDetails === 'object') {
          customError.details = {
            ...(customError.details || {}),
            ...err.errorDetails
          };
        }
      } else if (typeof err === 'string') {
        customError.title = `Error ${error.status}`;
        customError.userMessage = err;
      } else {
        customError.title = `Error ${error.status}`;
        customError.userMessage = error.message;
      }
    }

    console.error('Full HTTP error:', error);

    return throwError(() => customError);
  }

  /**
  * It sends a register request at backend
  * @param userRequest
  * @returns authResponse.
  */
  $registerUser(userRequest: UserRequest): Observable<AuthResponse> {
    const url = `${this.baseUrl}/register`;
    return this.http.post<AuthResponse>(url, userRequest).pipe(catchError(this.handleError));
  }

  $loginUser(userLoginRequest:LoginRequest): Observable<AuthResponse>{
    const url = `${this.baseUrl}/login`;
    return this.http.post<AuthResponse>(url, userLoginRequest).pipe(catchError(this.handleError));
  }

  $validateToken(token: TokenValidationRequest): Observable<void>{
    const url = `${this.baseUrl}/validate-token`;
    return this.http.post<void>(url, token).pipe(catchError(this.handleError));
  }

  saveToken(authResponse: AuthResponse){
    const token = authResponse.token;
    localStorage.setItem(this.AUTH_TOKEN_KEY, token);
  }

  getToken(){
    return localStorage.getItem(this.AUTH_TOKEN_KEY);
  }

}
