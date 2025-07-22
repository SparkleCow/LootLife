import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, catchError, Observable, throwError } from 'rxjs';
import { UserResponseDto } from '../../models/user-response-dto.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

 private baseUrl: string = 'http://localhost:8080/user';

  private userSubject = new BehaviorSubject<UserResponseDto | null>(null);

  public user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient) {}

  /**
  *Generic http handler
  * @param error  HttpErrorResponse
  * @returns Observer
  */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocurrió un error desconocido.';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      if (error.status === 400 && error.error && typeof error.error === 'object') {
        errorMessage = 'Errores de validación:';
        for (const key in error.error) {
          if (Object.prototype.hasOwnProperty.call(error.error, key)) {
            errorMessage += `\n- ${key}: ${error.error[key]}`;
          }
        }
      } else if (error.error && typeof error.error === 'string') {
        errorMessage = `Error del servidor (${error.status}): ${error.error}`;
      } else {
        errorMessage = `El servidor retornó un código ${error.status}: ${error.message}`;
      }
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }


  fetchUserInformation(): void {
    this.http.get<UserResponseDto>(this.baseUrl)
      .pipe(catchError(this.handleError))
      .subscribe({
        next: (userData) => this.userSubject.next(userData),
        error: (err) => {
          console.error('Error al obtener información del usuario:', err.message);
          this.userSubject.next(null); // Opcional: resetear usuario en error
        }
      });
  }

  $getUserInformation(): Observable<UserResponseDto> {
    return this.http.get<UserResponseDto>(this.baseUrl).pipe(catchError(this.handleError));
  }
}
