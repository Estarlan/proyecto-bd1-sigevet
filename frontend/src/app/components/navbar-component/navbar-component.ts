import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SesionService } from '../../services/sesion.service';

/** Opción del menú superior. */
interface OpcionMenu {
  /** Texto que se ve en el menú. */
  texto: string;
  /** Dirección a la que lleva. */
  ruta: string;
  /** Clase del icono de Bootstrap Icons. */
  icono: string;
}

/**
 * Menú superior de la aplicación. Se dibuja una sola vez en AppComponent y solo cuando hay un empleado conectado;
 * muestra sus opciones, el nombre del empleado y el botón para cerrar la sesión.
 */
@Component({
  selector: 'app-navbar-component',
  standalone: false,
  templateUrl: './navbar-component.html',
  styleUrl: './navbar-component.css'
})
export class NavbarComponent {
  /** true cuando el menú desplegable (pantallas pequeñas) está abierto. */
  menuAbierto = false;

  /** Opciones del menú, en el orden en que se muestran. */
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

  /** Cierra el menú desplegable (se llama al elegir una opción). */
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
