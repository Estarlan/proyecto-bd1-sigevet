import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { ClienteService, mensajeDeError } from '../../services/api.service';
import { ClienteDetalle } from '../../models/models';
import { UltimaPeticion } from '../../shared/ultima-peticion';

/**
 * Detalle de un cliente (/clientes/:id): sus datos, teléfonos, mascotas y facturas. Desde aquí se edita o se
 * elimina el cliente y se pasa a registrar una mascota o una factura suya.
 */
@Component({
  selector: 'app-cliente-detalle-component',
  standalone: false,
  templateUrl: './cliente-detalle-component.html'
})
export class ClienteDetalleComponent implements OnInit {

  /** Datos del cliente con sus listas; null mientras carga o si falló la carga. */
  detalle: ClienteDetalle | null = null;
  /** true mientras se espera la respuesta de la API. */
  cargando = true;
  /** Error de la carga. */
  error = '';

  /** Error de una acción (eliminar), separado del error de carga. */
  errorAccion = '';
  /** true mientras se elimina el cliente (deshabilita el botón). */
  eliminando = false;

  /** Controla que solo cuente la respuesta del cliente más reciente si se navega rápido entre clientes. */
  private ultima = new UltimaPeticion();

  constructor(private route: ActivatedRoute, private router: Router, private clienteService: ClienteService) {}

  /** Carga el cliente de la dirección y vuelve a cargar si el id de la dirección cambia. */
  ngOnInit(): void {
    // Se escucha paramMap (y no solo el valor inicial) para que la página se actualice
    // si se navega de un cliente a otro sin salir de esta ruta.
    this.route.paramMap.subscribe(parametros => {
      const id = Number(parametros.get('id'));
      this.cargar(id);
    });
  }

  /** Pide el detalle del cliente con ese id. */
  cargar(id: number): void {
    this.cargando = true;
    this.error = '';
    this.detalle = null;
    this.ultima.lanzar(this.clienteService.getDetalle(id), {
      next: (datos) => { this.detalle = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  /**
   * Borra el cliente. El backend solo lo permite si no tiene mascotas ni facturas (responde 409 si las tiene),
   * porque son historia clínica y contable que se conserva.
   */
  eliminar(): void {
    if (!this.detalle || !confirm('¿Eliminar al cliente ' + this.detalle.cliente.nombreCompleto + '?')) {
      return;
    }
    this.eliminando = true;
    this.errorAccion = '';
    this.clienteService.eliminar(this.detalle.cliente.idPersona).subscribe({
      next: () => this.router.navigate(['/clientes']),
      error: (e: HttpErrorResponse) => { this.errorAccion = mensajeDeError(e); this.eliminando = false; }
    });
  }
}
