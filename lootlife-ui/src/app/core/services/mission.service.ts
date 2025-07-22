import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { MissionResponseDto } from '../../models/mission-response-dto.model';

@Injectable({
  providedIn: 'root'
})
export class MissionService {

  private baseUrl: string = 'http://localhost:8080/mission'

  constructor(private http:HttpClient) { }

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

  $createMission(): Observable<MissionResponseDto>{
    return this.http.post<MissionResponseDto>(this.baseUrl, {}).pipe(catchError(this.handleError));
  }

  $getActiveMissions(): Observable<MissionResponseDto[]>{
    return this.http.get<MissionResponseDto[]>(this.baseUrl).pipe(catchError(this.handleError));
  }

  $getAllMissions(): Observable<MissionResponseDto[]>{
    return this.http.get<MissionResponseDto[]>(`${this.baseUrl}/all`).pipe(catchError(this.handleError));
  }

  $completeMission(id:number): Observable<MissionResponseDto>{
    return this.http.post<MissionResponseDto>(`${this.baseUrl}/complete/${id}`, {}).pipe(catchError(this.handleError));
  }

  $activateMission(id: number): Observable<MissionResponseDto> {
    return this.http.post<MissionResponseDto>(`${this.baseUrl}/activate/${id}`, {}).pipe(catchError(this.handleError));
  }

  $cancelMission(id: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/cancel/${id}`, {}).pipe(catchError(this.handleError));
  }
}

