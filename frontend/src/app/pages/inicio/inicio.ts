import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { InicioService, mensajeDeError } from '../../services/api.service';
import { Inicio as DatosInicio, Resumen } from '../../models/models';

/** Tarjeta de resumen de la parte superior. */
interface Indicador {
  tabla: string;
  texto: string;
  icono: string;
  ruta: string;
}

@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [RouterLink, CurrencyPipe, DecimalPipe],
  templateUrl: './inicio.html',
  styleUrl: './inicio.css'
})
export class Inicio implements OnInit {

  datos: DatosInicio | null = null;
  cargando = true;
  error = '';

  /** Tablas que se muestran como tarjetas; el número sale de los conteos de la API. */
  indicadores: Indicador[] = [
    { tabla: 'CLIENTE',  texto: 'Clientes',  icono: 'bi-people-fill',       ruta: '/clientes' },
    { tabla: 'MASCOTA',  texto: 'Mascotas',  icono: 'bi-heart-pulse-fill',  ruta: '/mascotas' },
    { tabla: 'CITA',     texto: 'Citas',     icono: 'bi-calendar-week-fill', ruta: '/agenda' },
    { tabla: 'FACTURA',  texto: 'Facturas',  icono: 'bi-receipt',           ruta: '/facturas' },
    { tabla: 'EMPLEADO', texto: 'Empleados', icono: 'bi-person-badge-fill', ruta: '/personal' }
  ];

  constructor(private inicioService: InicioService) {}

  ngOnInit(): void {
    this.inicioService.getResumen().subscribe({
      next: (d) => { this.datos = d; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  /** Registros de una tabla según los conteos (0 si no viene). */
  conteo(tabla: string): number {
    const fila = this.datos?.conteos.find(c => c.nombre === tabla);
    return fila?.total ?? 0;
  }

  /** Suma de una lista de resúmenes (total de registros, total de ingresos o total de citas). */
  suma(lista: Resumen[]): number {
    let total = 0;
    for (const fila of lista) {
      total += fila.total ?? 0;
    }
    return total;
  }

  /** Porcentaje de una fila frente al total, para el ancho de las barras. */
  porcentaje(valor: number | undefined, lista: Resumen[]): number {
    const total = this.suma(lista);
    return total === 0 ? 0 : Math.round(((valor ?? 0) * 100) / total);
  }
}
