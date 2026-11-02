import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SesionService } from '../../../services/sesion.service';

/** Opción del menú superior. */
interface OpcionMenu {
  texto: string;
  ruta: string;
  icono: string;
}

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class NavbarComponent {
  menuAbierto = false;

  opciones: OpcionMenu[] = [
    { texto: 'Inicio',             ruta: '/',           icono: 'bi-house' },
    { texto: 'Clientes',           ruta: '/clientes',   icono: 'bi-people' },
    { texto: 'Mascotas',           ruta: '/mascotas',   icono: 'bi-heart-pulse' },
    { texto: 'Agenda',             ruta: '/agenda',     icono: 'bi-calendar-week' },
    { texto: 'Inventario',         ruta: '/inventario', icono: 'bi-capsule' },
    { texto: 'Facturas',           ruta: '/facturas',   icono: 'bi-receipt' },
    { texto: 'Personal',           ruta: '/personal',   icono: 'bi-person-badge' },
    { texto: 'Álgebra relacional', ruta: '/algebra',    icono: 'bi-diagram-3' }
  ];

  constructor(public sesion: SesionService, private router: Router) {}

  cerrarMenu(): void {
    this.menuAbierto = false;
  }

  /** Cierra la sesión en el servidor y vuelve al inicio de sesión. */
  salir(): void {
    this.cerrarMenu();
    this.sesion.cerrar().subscribe({
      next: () => this.router.navigate(['/login']),
      error: () => this.router.navigate(['/login'])
    });
  }
}
