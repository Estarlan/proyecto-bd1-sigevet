import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { map } from 'rxjs';
import { SesionService } from '../services/sesion.service';

/**
 * Deja entrar a una página solo si hay sesión; si no, lleva al inicio de sesión.
 * La protección real está en el servidor (FiltroSesion responde 401); esto solo evita mostrar páginas vacías.
 */
export const sesionGuard: CanActivateFn = () => {
  const sesion = inject(SesionService);
  const router = inject(Router);
  if (sesion.empleado()) {
    return true;
  }
  return sesion.consultar().pipe(map(hay => hay ? true : router.createUrlTree(['/login'])));
};
