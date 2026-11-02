import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

/** Opción del menú superior. */
interface OpcionMenu {
  texto: string;
  ruta: string;
  icono: string;
}

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class NavbarComponent {
  menuAbierto = false;

  opciones: OpcionMenu[] = [
    { texto: 'Inicio',             ruta: '/',           icono: 'bi-house' },
    { texto: 'Clientes',           ruta: '/clientes',   icono: 'bi-people' },
    { texto: 'Agenda',             ruta: '/agenda',     icono: 'bi-calendar-week' },
    { texto: 'Inventario',         ruta: '/inventario', icono: 'bi-capsule' },
    { texto: 'Facturas',           ruta: '/facturas',   icono: 'bi-receipt' },
    { texto: 'Personal',           ruta: '/personal',   icono: 'bi-person-badge' },
    { texto: 'Álgebra relacional', ruta: '/algebra',    icono: 'bi-diagram-3' }
  ];

  cerrarMenu(): void {
    this.menuAbierto = false;
  }
}
