import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { InicioService, mensajeDeError } from '../../services/api.service';
import { Inicio as DatosInicio, Resumen } from '../../models/models';

/** Tarjeta de resumen de la parte superior. */
interface Indicador {
  /** Nombre de la tabla de la que sale el conteo (CLIENTE, MASCOTA...). */
  tabla: string;
  /** Texto que se ve en la tarjeta. */
  texto: string;
  /** Clase del icono de Bootstrap Icons. */
  icono: string;
  /** Página a la que lleva la tarjeta. */
  ruta: string;
}

/**
 * Página de inicio (/): tarjetas con el número de registros por tabla, ingresos por método de pago, estado de las
 * citas, medicamentos con stock bajo y próximas citas. Todos los datos vienen de una sola llamada a la API.
 */
@Component({
  selector: 'app-inicio',
  standalone: false,
  templateUrl: './inicio.html',
  styleUrl: './inicio.css'
})
export class Inicio implements OnInit {

  /** Resumen devuelto por la API; null mientras carga o si falló la carga. */
  datos: DatosInicio | null = null;
  /** true mientras se espera la respuesta de la API. */
  cargando = true;
  /** Error de la carga. */
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

  /** Pide el resumen a la API al abrir la página. */
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
