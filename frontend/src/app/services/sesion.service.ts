import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map, of, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { LoginForm, Sesion } from '../models/models';

const API = environment.apiUrl;

/**
 * Inicio de sesión del personal. La sesión vive en el servidor (cookie JSESSIONID); aquí solo se guarda quién está
 * conectado para mostrarlo en el menú.
 */
@Injectable({ providedIn: 'root' })
export class SesionService {
  /** Empleado conectado, o null si no hay sesión. */
  readonly empleado = signal<Sesion | null>(null);

  constructor(private http: HttpClient) {}

  /** Pregunta al servidor si hay sesión. Devuelve true o false; nunca lanza error. */
  consultar(): Observable<boolean> {
    return this.http.get<Sesion>(`${API}/sesion`).pipe(
      tap(datos => this.empleado.set(datos)),
      map(() => true),
      catchError(() => {
        this.empleado.set(null);
        return of(false);
      })
    );
  }

  /** POST /api/sesion con el correo y el documento. */
  iniciar(form: LoginForm): Observable<Sesion> {
    return this.http.post<Sesion>(`${API}/sesion`, form).pipe(tap(datos => this.empleado.set(datos)));
  }

  /** DELETE /api/sesion: cierra la sesión en el servidor. */
  cerrar(): Observable<void> {
    return this.http.delete<void>(`${API}/sesion`).pipe(tap(() => this.empleado.set(null)));
  }
}
