import { inject } from '@angular/core';
import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { SesionService } from '../services/sesion.service';

/**
 * Si la API responde 401 (la sesión expiró o se cerró), se borra el empleado conectado y se va al inicio de sesión.
 * Las peticiones a /api/sesion se dejan pasar: la página de login muestra su propio mensaje.
 */
export const sesionInterceptor: HttpInterceptorFn = (peticion, siguiente) => {
  const sesion = inject(SesionService);
  const router = inject(Router);
  return siguiente(peticion).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !peticion.url.endsWith('/sesion')) {
        sesion.empleado.set(null);
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
