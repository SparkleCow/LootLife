import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { TaskRequestDto } from '../../models/task-request-dto.model';
import { TaskResponseDto } from '../../models/task-response-dto.model';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private baseUrl: string = 'http://localhost:8080/task'

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

  /*This method will allow us to retrieve all the users´s request. It will recieve the
  jwt from the browser and it will send it with angular interceptor to Lootlife API.
  If the validation is successful, the API will return the values.*/
  findUserTask(): Observable<TaskResponseDto[]> {
    return this.http.get<TaskResponseDto[]>(this.baseUrl).pipe(catchError(this.handleError));
  }

  createTask(taskResquestDto:TaskRequestDto): Observable<void>{
    return this.http.post<void>(this.baseUrl, taskResquestDto).pipe(catchError(this.handleError));
  }

  completeTask(id:number): Observable<void>{
    const url = `${this.baseUrl}/complete/${id}`;
    return this.http.post<void>(url, {}).pipe(catchError(this.handleError));
  }

  deleteTask(id:number): Observable<void>{
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete<void>(url, {}).pipe(catchError(this.handleError));
  }

}
